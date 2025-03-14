package com.JSONComparator;

import java.io.File;
import java.io.FileReader;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.testng.annotations.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.aventstack.extentreports.Status;

import ResuableLibrary.ResponseComparatorResuables;
import baseTestPackage.BaseTest_TestNG;

public class UC13JSONFileComparisonManyScrToManyTarget extends BaseTest_TestNG {
	ResponseComparatorResuables resuableComponents = new ResponseComparatorResuables();

	/**
	 * Test method to Compare two JSON files
	 * 
	 * @throws Exception
	 */
	@Test
	public void compareJSONFiles() throws Exception {
		File srcfolder = new File(globalProp.getProperty("1SrctoManySourceFolderPath"));
		File[] listOfSrcFiles = srcfolder.listFiles();
		for (int srci = 0; srci < listOfSrcFiles.length; srci++) {
			if (listOfSrcFiles[srci].isFile()) {
				String srcfilename=listOfSrcFiles[srci].getName();
				String tragetfoldername = FilenameUtils.removeExtension(srcfilename);
				File Targetfolder = new File(globalProp.getProperty("1SrctoManyTargetFolderPath")+"//"+ tragetfoldername );
				File[] listOfTragetFiles = Targetfolder.listFiles();
				test = report.createTest("JSON FIles Comparison");
				test.log(Status.INFO, "*********************JSON Files Comparison - Starts***********************");
				test.log(Status.INFO, "Load source and target JSON files");
				JsonElement srcJsonElement = resuableComponents
						.loadJSONFile(globalProp.getProperty("1SrctoManySourceFolderPath") +"//"+ srcfilename);
				// Target file loop
				for (int i = 0; i < listOfTragetFiles.length; i++) {
					if (listOfTragetFiles[i].isFile()) {				
					  System.out.println("File " + listOfTragetFiles[i].getName()); 
					  test.log(Status.INFO, "Src file path  :" +listOfSrcFiles[srci].getAbsolutePath());
					  test.log(Status.INFO, "Target file path  :" +listOfTragetFiles[i].getAbsolutePath());
					  test.log(Status.INFO, "Src file name  :" +listOfSrcFiles[srci].getName());
					  test.log(Status.INFO, "Target file name  :" +listOfTragetFiles[i].getName() );
					  
						JsonElement tgtJsonElement = resuableComponents
								.loadJSONFile(globalProp.getProperty("1SrctoManyTargetFolderPath") + listOfTragetFiles[i].getName());
						test.log(Status.INFO, "Read and Retieve the details from Source and Target File systems");
						Map<String, Object> srcJSONMap = resuableComponents.readJSONFile(srcJsonElement);
						Map<String, Object> tgtJSONMap = resuableComponents.readJSONFile(tgtJsonElement);
						test.log(Status.INFO, "Comparison of Source file name  "+srcfilename+"  and Target File name "+listOfTragetFiles[i].getName()+" system starts-------------------------");
						test.log(Status.INFO, "*******************************************");
						resuableComponents.compareJSONKeys(srcJSONMap, tgtJSONMap, test);
						test.log(Status.INFO, "*******************************************");
						resuableComponents.compareJSONKeysValues(srcJSONMap, tgtJSONMap, test);
						test.log(Status.INFO, "*******************************************");
						resuableComponents.compareJSONSequence(srcJSONMap, tgtJSONMap, test);
						test.log(Status.INFO, "*******************************************");
						
						test.log(Status.INFO, "Comparison of Source file name  "+srcfilename+" and Target File name "+listOfTragetFiles[i].getName()+" system ends-------------------------");
					} 
					else {
						  System.out.println("No target files avaialble to compare"); 
						  } 
				}
				
			}
		}
	
	}
	
}
