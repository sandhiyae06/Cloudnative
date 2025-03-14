package com.PUBSUBTesting;

import org.testng.annotations.Test;
import com.google.cloud.ServiceOptions;

import ResuableLibrary.GoogleResuableComponents;


public class GoogleMessgaeSubscribe {
	GoogleResuableComponents googleResuableComponents = new GoogleResuableComponents();
	@Test
	public void subscribeMessageToGCP() throws Exception {
		String projectId = ServiceOptions.getDefaultProjectId();
		String subscriptionId = "TCOE-SQS-POC-sub";
		googleResuableComponents.subscribeAsyncExample(projectId, subscriptionId);
	}

}