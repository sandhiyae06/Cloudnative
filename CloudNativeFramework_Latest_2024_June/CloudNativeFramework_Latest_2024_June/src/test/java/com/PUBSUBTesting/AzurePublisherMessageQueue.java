package com.PUBSUBTesting;


import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.azure.messaging.servicebus.ServiceBusSenderClient;


import ResuableLibrary.AzureResuableComponents;
import baseTestPackage.BaseTest_TestNG;

public class AzurePublisherMessageQueue extends BaseTest_TestNG {
	AzureResuableComponents resuableComponents = new AzureResuableComponents();
	/**
	 * Test method to publish message to Azure Portal
	 * @throws Exception
	 */
	 @Test
	public void sendMessage() throws Exception {
		test = report.createTest("Azure_Message_Publisher");
	//	freezeTestDataRow(this.getClass().getSimpleName());
		String fileName=globalProp.getProperty("textFile")+"src\\test\\resources\\Files\\Text_File.txt";
		File file = new File((fileName));
		test.log(Status.INFO, "*********************Publish Message to AZURE Portal - Starts***********************");
		test.log(Status.INFO, "Topic name to which message is to be sent -----------" + globalProp.getProperty("topicName"));
		String content = resuableComponents.extractMessageFromFile(file);
		test.log(Status.PASS, "Message to be published in Azure client ************"+content);
		ServiceBusSenderClient senderClient = resuableComponents.createServiceBusClientObject(globalProp.getProperty("connectionString"),globalProp.getProperty("topicName"));
		test.log(Status.PASS, "Connection  with the Azure client established successfully");
		resuableComponents.publishMessage(content, senderClient);
		test.log(Status.PASS, "Message published to the topic: " + globalProp.getProperty("topicName")+" successfully with out any errors");
		resuableComponents.closeConnection(senderClient);
		test.log(Status.PASS, "Azure client connection closed successfully");
		test.log(Status.INFO, "*********************Publish Message to AZURE Portal - Ends***********************");
	}
	
	 private static String sanitizePathTraversal(String filename) {
		 Path p = Paths.get(filename);
		 return p.getFileName().toString();
		}
	
	}
