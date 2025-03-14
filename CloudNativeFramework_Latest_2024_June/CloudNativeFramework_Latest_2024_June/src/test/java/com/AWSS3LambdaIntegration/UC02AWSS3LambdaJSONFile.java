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
import com.google.gson.JsonElement;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;


import ResuableLibrary.AWSResuables;
import ResuableLibrary.ResponseComparatorResuables;
import baseTestPackage.BaseTest_TestNG;


public class UC02AWSS3LambdaJSONFile extends BaseTest_TestNG {
	AWSResuables resuableComponents = new AWSResuables();
	ResponseComparatorResuables resuableComponentsXML = new ResponseComparatorResuables();
	ResponseComparatorResuables comparatorResuableComponents = new ResponseComparatorResuables();
	 /**
	  * Test method to Upload file to AWS S3
	 * @throws Exception
	 */
	@Test
	public  void jsonFileS3LambdaIntegration() throws Exception {
		test = report.createTest("AWS_S3_LambdaIntegration_Test");
		test.log(Status.INFO, "*********************Upload file to AWS S3 - Starts***********************");
		
		String sourceBucketName = "cloudnativeframeworkbucket";
		String destinationBucketName= "cloudnativeframeworkdemodestinationbucket";
		test.log(Status.INFO, "Load AWS Credentials and connect to AWS");
		BasicAWSCredentials credentials=resuableComponents.loadAWSKeys(globalProp,test);
		test.log(Status.INFO, "Invoking AWS S3 Client");
		AmazonS3 s3client = resuableComponents.coonectAWSS3Client(credentials);
		test.log(Status.INFO, "Uploading file ***"+globalProp.getProperty("UC02sourcejsonfilename")+"***to bucket ***"+sourceBucketName+"*** in AWS S3");
		resuableComponents.uploadFiletoS3(sourceBucketName, s3client, globalProp.getProperty("JSONFilePath"),globalProp.getProperty("UC02sourcejsonfilename"));
		test.log(Status.INFO, "Validating file ***"+globalProp.getProperty("UC02sourcejsonfilename")+"*** in AWS S3 bucket ***"+sourceBucketName+"***");
		resuableComponents.ValidateUploadedFileinS3(sourceBucketName, s3client, globalProp.getProperty("UC02sourcejsonfilename"), test);
		test.log(Status.INFO, "*********************Moving file from AWS S3 - Starts***********************");
		test.log(Status.INFO, "Moving file ***"+globalProp.getProperty("UC02sourcejsonfilename")+"***to bucket ***"+destinationBucketName+"*** in AWS S3");
		resuableComponents.moveFilebetweenS3(sourceBucketName, destinationBucketName, s3client, globalProp,globalProp.getProperty("UC02sourcejsonfilename"), globalProp.getProperty("UC02sourcejsonfilename"));
		test.log(Status.INFO, "Validating file ***"+globalProp.getProperty("UC02sourcejsonfilename")+"*** in AWS S3 bucket ***"+destinationBucketName+"***");
		test.log(Status.INFO, "Validating the updated file ***"+globalProp.getProperty("UC02sourcejsonfilename")+"*** in AsourceBucketNameWS S3 bucket ***"+destinationBucketName+"***");
		resuableComponents.ValidateUploadedFileinS3(destinationBucketName, s3client, globalProp.getProperty("UC02sourcejsonfilename"), test);
		test.log(Status.INFO, "*********************Download file from AWS S3 - Starts***********************");
		
		resuableComponents.downloadFileFromS3(destinationBucketName, s3client, globalProp.getProperty("UC02sourcejsonfilename"),globalProp.getProperty("DownloadedJSONFilePath"));
		resuableComponents.downloadFileFromS3(destinationBucketName, s3client,globalProp.getProperty("UC02targetjsonfilename"),globalProp.getProperty("DownloadedJSONFilePath") );
		resuableComponents.prepareForJson(new File(globalProp.getProperty("DownloadedJSONFilePath")+"//"+globalProp.getProperty("UC02sourcejsonfilename")),new FileWriter(globalProp.getProperty("DownloadedJSONFilePath")+"//AWSS3sources2.json"));
		resuableComponents.prepareForJson(new File(globalProp.getProperty("DownloadedJSONFilePath")+"//"+globalProp.getProperty("UC02targetjsonfilename")),new FileWriter(globalProp.getProperty("DownloadedJSONFilePath")+"//AWSS3targets2.json"));
		test.log(Status.INFO, "*********************JSON Files Comparison - Starts***********************");
		test.log(Status.INFO, "Load source and target JSON files");
		JsonElement	srcJsonElement = comparatorResuableComponents
				.loadJSONFile(globalProp.getProperty("DownloadedJSONFilePath")+"//AWSS3sources2.json");
		JsonElement	tgtJsonElement = comparatorResuableComponents
				.loadJSONFile(globalProp.getProperty("DownloadedJSONFilePath")+"//AWSS3targets2.json");
		test.log(Status.INFO, "Read and Retieve the details from Source and Target File systems");
		Map<String, Object> srcJSONMap = comparatorResuableComponents.readJSONFile(srcJsonElement);
		Map<String, Object> tgtJSONMap = comparatorResuableComponents.readJSONFile(tgtJsonElement);
		test.log(Status.INFO, "Comparison of Source and Target File system starts-------------------------");
		test.log(Status.INFO, "*******************************************");
		comparatorResuableComponents.compareJSONKeys(srcJSONMap, tgtJSONMap, test);
		test.log(Status.INFO, "*******************************************");
		comparatorResuableComponents.compareJSONKeysValues(srcJSONMap, tgtJSONMap, test);
		test.log(Status.INFO, "*******************************************");
		comparatorResuableComponents.compareJSONSequence(srcJSONMap, tgtJSONMap, test);
		test.log(Status.INFO, "*******************************************");
		test.log(Status.INFO, "Comparison of Source and Target File system ends-------------------------");
	}

	

	

	

	

	
	
}
