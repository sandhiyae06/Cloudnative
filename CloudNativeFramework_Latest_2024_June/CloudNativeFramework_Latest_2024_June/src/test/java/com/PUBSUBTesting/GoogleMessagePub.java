package com.PUBSUBTesting;

import org.testng.annotations.Test;
import com.google.cloud.ServiceOptions;

import ResuableLibrary.GoogleResuableComponents;

public class GoogleMessagePub {
	static String projectID="";
	static String subscriptionID="TCOE-SQS-POC-sub";
	GoogleResuableComponents googleResuableComponents=new GoogleResuableComponents();
	@Test
	public void publishMessageToGCP() throws Exception {
		 projectID=ServiceOptions.getDefaultProjectId();
	       googleResuableComponents.publishWithErrorHandlerExample(projectID,"TCOE-SQS-POC","");	
	}
	
	
}
