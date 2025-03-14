package com.JSONComparator;

import java.io.File;
import java.util.Map;

import org.testng.annotations.Test;

import com.google.gson.JsonElement;
import com.aventstack.extentreports.Status;

import ResuableLibrary.ResponseComparatorResuables;
import baseTestPackage.BaseTest_TestNG;

public class UC12JSONFileComparison1ScrToManyTargetFail extends BaseTest_TestNG {
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
		JsonElement	srcJsonElement = resuableComponents
				.loadJSONFile(globalProp.getProperty("1SrctoManySourceFolderPath") + "\\source2.json");
		File folder = new File(globalProp.getProperty("1SrctoManyTargetFolderPath"));
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {				
			  System.out.println("File " + listOfFiles[i].getName()); 
			  test.log(Status.INFO, "Target file name  :" +listOfFiles[i].getName() );
				JsonElement	tgtJsonElement = resuableComponents
						.loadJSONFile(globalProp.getProperty("1SrctoManyTargetFolderPath") + listOfFiles[i].getName());
				test.log(Status.INFO, "Read and Retieve the details from Source and Target File systems");
				Map<String, Object> srcJSONMap = resuableComponents.readJSONFile(srcJsonElement);
				Map<String, Object> tgtJSONMap = resuableComponents.readJSONFile(tgtJsonElement);
				test.log(Status.INFO, "Comparison of Source and Target File name "+listOfFiles[i].getName()+" system starts-------------------------");
				test.log(Status.INFO, "*******************************************");
				resuableComponents.compareJSONKeys(srcJSONMap, tgtJSONMap, test);
				test.log(Status.INFO, "*******************************************");
				resuableComponents.compareJSONKeysValues(srcJSONMap, tgtJSONMap, test);
				test.log(Status.INFO, "*******************************************");
				resuableComponents.compareJSONSequence(srcJSONMap, tgtJSONMap, test);
				test.log(Status.INFO, "*******************************************");
				
				test.log(Status.INFO, "Comparison of Source  and Target File name "+listOfFiles[i].getName()+" system ends-------------------------");
			} 
			else {
				  System.out.println("No target files avaialble to compare"); 
				  } 
		}
	}
}
