package com.JSONComparator;

import java.io.File;
import java.util.Map;

import org.testng.annotations.Test;

import com.google.gson.JsonElement;
import com.aventstack.extentreports.Status;

import ResuableLibrary.ResponseComparatorResuables;
import baseTestPackage.BaseTest_TestNG;

public class UC14XMLJSONFileComparisonPass extends BaseTest_TestNG {
	ResponseComparatorResuables resuableComponents = new ResponseComparatorResuables();

	/**
	 * Test method to Compare two JSON files
	 * 
	 * @throws Exception
	 */
	@Test
	public void compareJSONFiles() throws Exception {
		test = report.createTest("JSON FIles Comparison");
		test.log(Status.INFO, "*********************XML Files Comparison - Starts***********************");
		test.log(Status.INFO, "Load source and target JSON files");
		//XMLFilePath
		//File sourcexmlfile = new File(globalProp.getProperty("XMLFilePath") + "\\source.xml");
	//	String targetJsonFile=globalProp.getProperty("JSONFilePath") + "\\source_converted_xml.json" ;
		//JsonElement	tgtJsonElement = resuableComponents.loadJSONFile(globalProp.getProperty("JSONFilePath") + "\\target.json");
		File sourcexmlfile = new File("C:\\Project\\CloudNative\\Adhoc\\Sampler XML.xml");
		String targetJsonFile="C:\\Project\\CloudNative\\Adhoc\\source_converted_xmltojson.json" ;
		
		resuableComponents.convertXmlToJSON(sourcexmlfile, targetJsonFile);
		
		JsonElement	srcJsonElement = resuableComponents
				.loadJSONFile(targetJsonFile);	
		JsonElement	tgtJsonElement = resuableComponents
				.loadJSONFile("C:\\Project\\CloudNative\\Adhoc\\Sample JSON.json");
		test.log(Status.INFO, "Read and Retieve the details from Source and Target File systems");
		Map<String, Object> srcJSONMap = resuableComponents.readJSONFile(srcJsonElement);
		Map<String, Object> tgtJSONMap = resuableComponents.readJSONFile(tgtJsonElement);
		test.log(Status.INFO, "Comparison of Source and Target File system starts-------------------------");
		test.log(Status.INFO, "*******************************************");
		resuableComponents.compareJSONKeys(srcJSONMap, tgtJSONMap, test);
		test.log(Status.INFO, "*******************************************");
		resuableComponents.compareJSONKeysValues(srcJSONMap, tgtJSONMap, test);
		test.log(Status.INFO, "*******************************************");
		resuableComponents.compareJSONSequence(srcJSONMap, tgtJSONMap, test);
		test.log(Status.INFO, "*******************************************");
		test.log(Status.INFO, "Comparison of Source and Target File system ends-------------------------");
	}
}
