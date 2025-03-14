package com.AWSS3LambdaIntegration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

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


public class UC01AWSS3LambdaXMLFile extends BaseTest_TestNG {
	AWSResuables resuableComponents = new AWSResuables();
	ResponseComparatorResuables resuableComponentsXML = new ResponseComparatorResuables();
	ResponseComparatorResuables comparatorResuableComponents = new ResponseComparatorResuables();
	 /**
	  * Test method to Upload file to AWS S3
	 * @throws Exception
	 */
	@Test
	public  void xmlFileS3LambdaIntegration() throws Exception {
		test = report.createTest("AWS_S3_LambdaIntegration_Test");
		test.log(Status.INFO, "*********************Upload file to AWS S3 - Starts***********************");
		
		String sourceBucketName = "cloudnativeframeworkbucket";
		String destinationBucketName= "cloudnativeframeworkdemodestinationbucket";
		test.log(Status.INFO, "Load AWS Credentials and connect to AWS");
		BasicAWSCredentials credentials=resuableComponents.loadAWSKeys(globalProp,test);
		test.log(Status.INFO, "Invoking AWS S3 Client");
		AmazonS3 s3client = resuableComponents.coonectAWSS3Client(credentials);
		test.log(Status.INFO, "Uploading file ***"+globalProp.getProperty("UC01sourcefilename")+"***to bucket ***"+sourceBucketName+"*** in AWS S3");
		resuableComponents.uploadFiletoS3(sourceBucketName, s3client, globalProp.getProperty("XMLFilePath"),globalProp.getProperty("UC01sourcefilename"));
		test.log(Status.INFO, "Validating file ***"+globalProp.getProperty("UC01sourcefilename")+"*** in AWS S3 bucket ***"+sourceBucketName+"***");
		resuableComponents.ValidateUploadedFileinS3(sourceBucketName, s3client, globalProp.getProperty("UC01sourcefilename"), test);
		
		
		
		test.log(Status.INFO, "Moving file ***"+globalProp.getProperty("UC01sourcefilename")+"***to bucket ***"+destinationBucketName+"*** in AWS S3");
		resuableComponents.moveFilebetweenS3(sourceBucketName, destinationBucketName, s3client, globalProp,globalProp.getProperty("UC01sourcefilename"), globalProp.getProperty("UC01sourcefilename"));
		test.log(Status.INFO, "Validating file ***"+globalProp.getProperty("UC01sourcefilename")+"*** in AWS S3 bucket ***"+destinationBucketName+"***");
		test.log(Status.INFO, "Validating the updated file ***"+globalProp.getProperty("UC01sourcefilename")+"*** in AsourceBucketNameWS S3 bucket ***"+destinationBucketName+"***");
		resuableComponents.ValidateUploadedFileinS3(destinationBucketName, s3client, globalProp.getProperty("UC01sourcefilename"), test);
		test.log(Status.INFO, "*********************Download file from AWS S3 - Starts***********************");
		
		resuableComponents.downloadFileFromS3(destinationBucketName, s3client, globalProp.getProperty("UC01sourcefilename"),globalProp.getProperty("DownloadedXMLFilePath"));
		resuableComponents.downloadFileFromS3(destinationBucketName, s3client,globalProp.getProperty("UC01targetfilename"),globalProp.getProperty("DownloadedXMLFilePath") );
		
		BufferedReader srcBuffReader = comparatorResuableComponents
				.loadXMLFile(globalProp.getProperty("DownloadedXMLFilePath")+ "\\"+globalProp.getProperty("UC01sourcefilename"));
		BufferedReader tgtBuffReader = comparatorResuableComponents
				.loadXMLFile(globalProp.getProperty("DownloadedXMLFilePath")+ "\\"+globalProp.getProperty("UC01targetfilename"));
		test.log(Status.INFO, "Read the XML Document from Source and Target File systems");
		String srcXMLString = comparatorResuableComponents.readXMLFile(srcBuffReader);
		String tgtXMLString = comparatorResuableComponents.readXMLFile(tgtBuffReader);
		Document srcDoc = comparatorResuableComponents.getXMLDocument(srcXMLString);
		Document tgtDoc = comparatorResuableComponents.getXMLDocument(tgtXMLString);
		test.log(Status.INFO, "Retieve the Node details from Source and Target File systems");
		List<Map<String, String>> srcNodeMapList = comparatorResuableComponents.getNodeMapList(srcDoc);
		List<Map<String, String>> tgtNodeMapList = comparatorResuableComponents.getNodeMapList(tgtDoc);
		test.log(Status.INFO, "Comparison of Source and Target File system starts-------------------------");
		test.log(Status.INFO, "*******************************************");
		comparatorResuableComponents.compareXMLKeysValues(srcNodeMapList, tgtNodeMapList, test);
		test.log(Status.INFO, "*******************************************");
		comparatorResuableComponents.compareXMLKeys(srcNodeMapList, tgtNodeMapList, test);
		test.log(Status.INFO, "*******************************************");
		comparatorResuableComponents.compareXMLSequence(srcNodeMapList, tgtNodeMapList, test);
		test.log(Status.INFO, "*******************************************");
		test.log(Status.INFO, "Comparison of Source and Target File system ends-------------------------");
		
		test.log(Status.INFO, "*********************Download file from AWS S3 - Ends***********************");
	}

	

	

	

	

	
	
}
