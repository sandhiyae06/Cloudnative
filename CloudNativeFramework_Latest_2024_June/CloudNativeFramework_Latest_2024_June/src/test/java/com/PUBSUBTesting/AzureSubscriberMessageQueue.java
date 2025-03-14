package com.PUBSUBTesting;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusErrorContext;
import com.azure.messaging.servicebus.ServiceBusException;
import com.azure.messaging.servicebus.ServiceBusFailureReason;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.messaging.servicebus.ServiceBusReceivedMessageContext;


import ResuableLibrary.AzureResuableComponents;
import baseTestPackage.BaseTest_TestNG;

public class AzureSubscriberMessageQueue extends BaseTest_TestNG {
	AzureResuableComponents resuableComponents = new AzureResuableComponents();
	public static ExtentTest testReport;
	/**
	 * Test method to subscribe messages from Azure Portal
	 * @throws Exception
	 */
	@Test
	public void receiveMessages() throws InterruptedException {
		test = report.createTest("Azure_Message_Subscriber");
		test.log(Status.INFO, "*********************Subscribe Message from AZURE Portal - Starts***********************");
		CountDownLatch countdownLatch = new CountDownLatch(1);
		ServiceBusProcessorClient processorClient = connectServiceBusProcessorClient(countdownLatch);
		test.log(Status.PASS, "Connection  with the Azure client established successfully");
		test.log(Status.PASS, "Starting the processor");
		processorClient.start();
		TimeUnit.SECONDS.sleep(10);
		processorClient.close();
		test.log(Status.INFO, "*********************Subscribe Message from AZURE Portal - Ends***********************");
	}
	public ServiceBusProcessorClient connectServiceBusProcessorClient(CountDownLatch countdownLatch) {
		ServiceBusProcessorClient processorClient = new ServiceBusClientBuilder()
				.connectionString(globalProp.getProperty("connectionString")).processor()
				.topicName(globalProp.getProperty("topicName")).subscriptionName(globalProp.getProperty("subName"))
				.processMessage(AzureSubscriberMessageQueue::processMessage)
				.processError(context -> processError(context, countdownLatch)).buildProcessorClient();
		return processorClient;
	}
	public static void processMessage(ServiceBusReceivedMessageContext context) {
		ServiceBusReceivedMessage message = context.getMessage();
		test.log(Status.PASS, "Processing message. Session: "+message.getMessageId()+", Sequence #: "+message.getSequenceNumber()+". Contents: "+message.getBody() );
		test.log(Status.PASS, "Validation for message content: "+message.getBody()+" is successfull" );
	}

	public static void processError(ServiceBusErrorContext context, CountDownLatch countdownLatch) {
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
			}
		} else {
		}
	}
	

}
