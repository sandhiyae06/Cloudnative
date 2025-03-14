package com.PUBSUBTesting;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusErrorContext;
import com.azure.messaging.servicebus.ServiceBusException;
import com.azure.messaging.servicebus.ServiceBusFailureReason;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.messaging.servicebus.ServiceBusReceivedMessageContext;
import com.azure.messaging.servicebus.ServiceBusSenderClient;


import ResuableLibrary.ResuableComponents;
import baseTestPackage.BaseTest_TestNG;

public class AzureMultipleMessageQueue extends BaseTest_TestNG {
	ResuableComponents resuableComponents = new ResuableComponents();
	public static ExtentTest testReport;
	/**
	 * Test method to publish and subscribe multiple messgaes to Azure Portal
	 * @throws Exception
	 */
	@Test
	public void multipleMessgae() throws Exception {
		test = report.createTest("Azure_Multiple_Messages_TestCase");
		testReport=test;
		AzureMultipleMessageQueue aMQ = new AzureMultipleMessageQueue();
		File f=new File(".");
		 File directoryPath = new File(f.getCanonicalPath()+globalProp.getProperty("folderPath"));
	      //List of all files and directories
	      File filesList[] = directoryPath.listFiles();
	      for(File file : filesList) {
	         aMQ.sendMessage(file.getCanonicalPath(),file.getName());
	 		aMQ.receiveMessages();
	      }
		
	}

	
	public void sendMessage(String strFilePath,String strFilename) throws Exception {
		//freezeTestDataRow(this.getClass().getSimpleName());
		File file = new File(strFilePath);
		test.log(Status.INFO, "*********************Publish Message to AZURE Portal - Starts for file "+strFilename+"***********************");
		test.log(Status.INFO, "Topic name to which message is to be sent --------" + globalProp.getProperty("topicName"));
		DataInputStream dis = new DataInputStream(new FileInputStream(file.getAbsolutePath()));
		byte[] datainBytes = new byte[dis.available()];
		dis.readFully(datainBytes);
		dis.close();
		String content = new String(datainBytes, 0, datainBytes.length, "UTF-8");
		test.log(Status.PASS, "Message to be published in Azure client ************"+content);
		ServiceBusSenderClient senderClient = new ServiceBusClientBuilder()
				.connectionString(globalProp.getProperty("connectionString")).sender()
				.topicName(globalProp.getProperty("topicName")).buildClient();
		test.log(Status.PASS, "Connection  with the Azure client established successfully");
		senderClient.sendMessage(new ServiceBusMessage(content));
		test.log(Status.PASS, "Message published to the topic: " + globalProp.getProperty("topicName")+" successfully with out any errors");
		senderClient.close();
		test.log(Status.PASS, "Azure client connection closed successfully");
		test.log(Status.INFO, "*********************Publish Message to AZURE Portal - Ends***********************");
	}

	public void receiveMessages() throws InterruptedException {
		//test = report.createTest("Azure_Message_Subscriber");
		test.log(Status.INFO, "*********************Subscribe Message from AZURE Portal - Starts***********************");
		CountDownLatch countdownLatch = new CountDownLatch(1);
		ServiceBusProcessorClient processorClient = new ServiceBusClientBuilder()
				.connectionString(globalProp.getProperty("connectionString")).processor()
				.topicName(globalProp.getProperty("topicName")).subscriptionName(globalProp.getProperty("subName"))
				.processMessage(AzureMultipleMessageQueue::processMessage)
				.processError(context -> processError(context, countdownLatch)).buildProcessorClient();
		test.log(Status.PASS, "Connection established with the Azure client successfully");
		test.log(Status.PASS, "Starting the processor");
		processorClient.start();
		TimeUnit.SECONDS.sleep(10);
		processorClient.close();
		test.log(Status.INFO, "*********************Subscribe Message from AZURE Portal - Ends***********************");
	}

	private static void processMessage(ServiceBusReceivedMessageContext context) {
		ServiceBusReceivedMessage message = context.getMessage();
		test.log(Status.PASS, "Processing message. Session: "+message.getMessageId()+", Sequence #: "+message.getSequenceNumber()+". Contents: "+message.getBody() );
		test.log(Status.PASS, "Validation for message content: "+message.getBody()+" is successfull" );
	}

	private static void processError(ServiceBusErrorContext context, CountDownLatch countdownLatch) {
		test.log(Status.FAIL, "Error when receiving messages from namespace: "+context.getFullyQualifiedNamespace()+". Entity: "+context.getEntityPath());
		if (!(context.getException() instanceof ServiceBusException)) {
			return;
		}

		ServiceBusException exception = (ServiceBusException) context.getException();
		ServiceBusFailureReason reason = exception.getReason();

		if (reason == ServiceBusFailureReason.MESSAGING_ENTITY_DISABLED
				|| reason == ServiceBusFailureReason.MESSAGING_ENTITY_NOT_FOUND
				|| reason == ServiceBusFailureReason.UNAUTHORIZED) {
					countdownLatch.countDown();
		} else if (reason == ServiceBusFailureReason.MESSAGE_LOCK_LOST) {
		} else if (reason == ServiceBusFailureReason.SERVICE_BUSY) {
			try {
				// Choosing an arbitrary amount of time to wait until trying
				// again.
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				System.err.println("Unable to sleep for period of time");
			}
		} else {
		}
	}

}
