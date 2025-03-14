package com.PUBSUBTesting;

import java.io.File;

import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import baseTestPackage.BaseTest_TestNG;
import ResuableLibrary.AWSMsgQueue;
import ResuableLibrary.ResuableComponents;

public class AWSMultipleMessageQueue extends BaseTest_TestNG {
	ResuableComponents resuableComponents = new ResuableComponents();

	/**
	 * Test method to publish and subscribe multiple messages to AWS
	 * @throws Exception
	 */
	@Test
	public void multipleMessgae() throws Exception {
		test = report.createTest("AWS_Multiple_Messages_TestCase");
		File f = new File(".");
		File directoryPath = new File(f.getCanonicalPath() + globalProp.getProperty("folderPath"));
		File filesList[] = directoryPath.listFiles();
		for (File file : filesList) {
			 PublishMessgae(file.getCanonicalPath(),file.getName());
			SubscribeMessgae(globalProp.getProperty("validate"));
		}

	}

	public static void PublishMessgae(String strFilePath, String strFileName) throws Exception {
		test.log(Status.INFO, "*********************Publish Message to AWS Portal for " + strFileName
				+ " - Starts***********************");
		AWSMsgQueue.Publish(test, report, globalProp, strFilePath);
		test.log(Status.INFO, "*********************Publish Message to AWS Portal - Ends***********************");

	}

	public static void SubscribeMessgae(String strValidationCOntent) throws Exception {
		test.log(Status.INFO,
				"*********************Subscribe Message from AWS Portal - Starts***********************");

		AWSMsgQueue.Subscribe(test, report, globalProp, strValidationCOntent);
		test.log(Status.INFO,
				"*********************Subscribe Message from AWS Portal - Ends***********************");
	}

}
