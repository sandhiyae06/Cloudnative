package com.PUBSUBTesting;

import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import ResuableLibrary.AWSMsgQueue;
import baseTestPackage.BaseTest_TestNG;
//import no.sysco.testing.kafka.embedded.AWSMsgQueue;

public class AWSMessageQueueSubscriber extends BaseTest_TestNG {
	/**
	 * Test method to subscribe message from AWS Queue and validate
	 * @throws Exception
	 */
	@Test
	public static void SubscribeMessgae() throws Exception {
		test = report.createTest("AWS_Message_Subscription");
		test.log(Status.INFO, "*********************Subscribe Message from AWS Portal - Starts***********************");

		AWSMsgQueue.Subscribe(test, report, globalProp);
		//get the value and validate
		test.log(Status.INFO, "*********************Subscribe Message from AWS Portal - Ends***********************");
	}

	
}
