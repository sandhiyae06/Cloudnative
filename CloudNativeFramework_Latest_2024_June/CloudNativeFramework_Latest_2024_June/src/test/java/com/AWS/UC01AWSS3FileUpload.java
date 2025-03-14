package com.AWS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.aventstack.extentreports.Status;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;


import ResuableLibrary.AWSResuables;
import ResuableLibrary.ResponseComparatorResuables;
import baseTestPackage.BaseTest_TestNG;


public class UC01AWSS3FileUpload extends BaseTest_TestNG {
	AWSResuables resuableComponents = new AWSResuables();
	ResponseComparatorResuables resuableComponentsXML = new ResponseComparatorResuables();
	 /**
	  * Test method to Upload file to AWS S3
	 * @throws Exception
	 */
	@Test
	public  void PublishMessgae() throws Exception {
		test = report.createTest("AWS_FileUpload_S3BUcket");
		test.log(Status.INFO, "*********************Upload file to AWS S3 - Starts***********************");
		
		String bucketName = "cloudnativeframeworkbucket";
		test.log(Status.INFO, "Load AWS Credentials and connect to AWS");
		BasicAWSCredentials credentials=resuableComponents.loadAWSKeys(globalProp,test);
		test.log(Status.INFO, "Invoking AWS S3 Client");
		AmazonS3 s3client = resuableComponents.coonectAWSS3Client(credentials);
		test.log(Status.INFO, "Uploading file ***"+globalProp.getProperty("fileName")+"***to bucket ***"+bucketName+"*** in AWS S3");
		resuableComponents.uploadFiletoS3(bucketName, s3client, globalProp);
		test.log(Status.INFO, "Validating file ***"+globalProp.getProperty("fileName")+"*** in AWS S3 bucket ***"+bucketName+"***");
		resuableComponents.ValidateUploadedFileinS3(bucketName, s3client, globalProp, test);
		resuableComponents.downloadFileFromS3(bucketName, s3client,globalProp);
		test.log(Status.INFO, "*********************Upload file to AWS S3 - Ends***********************");

	}

	

	

	

	

	
	
}
