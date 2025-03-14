package com.AWS;

import java.io.BufferedReader;
import org.testng.annotations.Test;

import com.amazonaws.auth.BasicAWSCredentials;
import com.aventstack.extentreports.Status;
import com.amazonaws.services.s3.AmazonS3;
import ResuableLibrary.AWSResuables;
import ResuableLibrary.ResponseComparatorResuables;
import baseTestPackage.BaseTest_TestNG;


public class UC03AWSS3FileUpdateandUpload extends BaseTest_TestNG {
	AWSResuables resuableComponents = new AWSResuables();
	ResponseComparatorResuables resuableComponentsXML = new ResponseComparatorResuables();
	 /**
	  * Test method to Update and Upload file in AWS S3
	 * @throws Exception
	 */
	@Test
	public  void fileUpdateandUploadinAWSS3() throws Exception {
		test = report.createTest("AWS_FileUpdateandUpload_S3Bucket");
		test.log(Status.INFO, "*********************Update and Upload file in AWS S3 - Starts***********************");
		
		String bucketName = "cloudnativeframeworkbucket";
		test.log(Status.INFO, "Load AWS Credentials and connect to AWS");
		BasicAWSCredentials credentials=resuableComponents.loadAWSKeys(globalProp,test);
		test.log(Status.INFO, "Invoking AWS S3 Client");
		AmazonS3 s3client = resuableComponents.coonectAWSS3Client(credentials);
		test.log(Status.INFO, "Downloading file ***"+globalProp.getProperty("fileName")+"*** from bucket ***"+bucketName+"*** in AWS S3");
		resuableComponents.downloadFileFromS3(bucketName, s3client,globalProp);
		test.log(Status.INFO, "Extarcting the file contents from ***"+globalProp.getProperty("fileName")+"***");	
		BufferedReader srcBuffReader = resuableComponentsXML
				.loadXMLFile(globalProp.getProperty("XMLFilePath") + "\\AWSS3source.xml");
		String srcXMLString = resuableComponentsXML.readXMLFile(srcBuffReader);
		test.log(Status.INFO, "Updating the file content in the ***"+globalProp.getProperty("fileName")+"*** file");	
		resuableComponents.writeFileContents(srcXMLString,globalProp);
        test.log(Status.INFO, "Writing the file content to the ***"+globalProp.getProperty("fileName")+"*** file");	
        test.log(Status.INFO, "Uploading file ***"+globalProp.getProperty("fileName")+"***to bucket ***"+bucketName+"*** in AWS S3");
		resuableComponents.uploadFiletoS3(bucketName, s3client, globalProp);
		test.log(Status.INFO, "Validating the updated file ***"+globalProp.getProperty("fileName")+"*** in AWS S3 bucket ***"+bucketName+"***");
		resuableComponents.ValidateUploadedFileinS3(bucketName, s3client, globalProp, test);
		test.log(Status.INFO, "*********************Update and Upload file in AWS S3 - Ends***********************");
	}
	
	

	

	

	

	
	
}
