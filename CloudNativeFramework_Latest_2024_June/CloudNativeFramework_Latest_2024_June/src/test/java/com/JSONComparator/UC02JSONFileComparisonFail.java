package com.JSONComparator;

import java.io.File;
import java.io.FileReader;
import java.util.Map;

import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


import ResuableLibrary.ResponseComparatorResuables;
import baseTestPackage.BaseTest_TestNG;

public class UC02JSONFileComparisonFail extends BaseTest_TestNG {
	ResponseComparatorResuables resuableComponents = new ResponseComparatorResuables();

	/**
	 * Test method to Compare two JSON files
	 * 
	 * @throws Exception
	 */
	@Test
	public void compareJSONFiles() throws Exception {
		
		
		test = report.createTest("JSON FIles Comparison");
		test.log(Status.INFO, "*********************JSON Files Comparison - Starts***********************");
		test.log(Status.INFO, "Load source and target JSON files");
		JsonElement srcJsonElement = resuableComponents
				.loadJSONFile(globalProp.getProperty("JSONFilePath") + "\\source2.json");
		
				JsonElement tgtJsonElement = resuableComponents
						.loadJSONFile(globalProp.getProperty("JSONFilePath") + "\\target.json");
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
				test.log(Status.INFO, "Comparison of Source  and Target File  system ends-------------------------");
			
	}
	
	
	
}
