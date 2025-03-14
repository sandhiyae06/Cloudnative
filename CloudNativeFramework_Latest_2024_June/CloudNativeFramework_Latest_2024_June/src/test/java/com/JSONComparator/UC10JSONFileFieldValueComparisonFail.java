package com.JSONComparator;

import java.util.Map;

import org.testng.annotations.Test;

import com.google.gson.JsonElement;
import com.aventstack.extentreports.Status;

import ResuableLibrary.ResponseComparatorResuables;
import baseTestPackage.BaseTest_TestNG;

public class UC10JSONFileFieldValueComparisonFail extends BaseTest_TestNG {
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
				.loadJSONFile(globalProp.getProperty("JSONFilePath") + "\\source.json");
		JsonElement tgtJsonElement = resuableComponents
				.loadJSONFile(globalProp.getProperty("JSONFilePath") + "\\target.json");
		test.log(Status.INFO, "Read and Retieve the details from Source and Target File systems");
		Map<String, Object> srcJSONMap = resuableComponents.readJSONFile(srcJsonElement);
		Map<String, Object> tgtJSONMap = resuableComponents.readJSONFile(tgtJsonElement);
		test.log(Status.INFO, "Comparison of Source and Target File system starts-------------------------");
		test.log(Status.INFO, "*******************************************");
		String srcJsonKeys=globalProp.getProperty("SrcJSONKeys1");
		String tgtJsonKeys=globalProp.getProperty("TgtJSONKeys1");
		String[] srcKeyArray=srcJsonKeys.split(";");
		String[] tgtKeyArray=tgtJsonKeys.split(";");
		int index=0;
		for(String srcJsonKey:srcKeyArray){
			if(srcJsonKey.contains(".")){
				resuableComponents.compareJSONFieldValueMap(srcJSONMap, tgtJSONMap, test, srcJsonKey,tgtKeyArray[index]);
				}else{
				resuableComponents.compareJSONFieldValue(srcJSONMap, tgtJSONMap, test, srcJsonKey,tgtKeyArray[index]);
			}index++;
		}
		test.log(Status.INFO, "*******************************************");
		test.log(Status.INFO, "Comparison of Source and Target File system ends-------------------------");
	}
}
