package com.PUBSUBTesting;

import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import ResuableLibrary.AWSMsgQueue;
import baseTestPackage.BaseTest_TestNG;
//import no.sysco.testing.kafka.embedded.AWSMsgQueue;

public class AWSMessageQueuePublisher extends BaseTest_TestNG {

	/**
	 * Test method to publish message from text file to AWS
	 * 
	 * @throws Exception
	 */
	@Test
	public static void PublishMessgae() throws Exception {
		test = report.createTest("AWS_Message_Publish");
		test.log(Status.INFO, "*********************Publish Message to AWS Portal - Starts***********************");
		AWSMsgQueue.Publish(test, report, globalProp);
		test.log(Status.INFO, "*********************Publish Message to AWS Portal - Ends***********************");

	}

}
