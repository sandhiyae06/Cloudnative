package com.XMLComparator;

import java.io.BufferedReader;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;
import org.w3c.dom.Document;

import com.aventstack.extentreports.Status;

import ResuableLibrary.ResponseComparatorResuables;
import baseTestPackage.BaseTest_TestNG;

public class UC10XMLFileFieldValueComparisonFail extends BaseTest_TestNG {
	ResponseComparatorResuables resuableComponents = new ResponseComparatorResuables();

	/**
	 * Test method to Compare two XML files
	 * 
	 * @throws Exception
	 */
	@Test
	public void compareXMLFiles() throws Exception {
		test = report.createTest("XML FIles Comparison");
		test.log(Status.INFO, "*********************XML Files Comparison - Starts***********************");
		test.log(Status.INFO, "Load source and target XML files");
		BufferedReader srcBuffReader = resuableComponents
				.loadXMLFile(globalProp.getProperty("XMLFilePath") + "\\source.xml");
		BufferedReader tgtBuffReader = resuableComponents
				.loadXMLFile(globalProp.getProperty("XMLFilePath") + "\\target.xml");
		test.log(Status.INFO, "Read the XML Document from Source and Target File systems");
		String srcXMLString = resuableComponents.readXMLFile(srcBuffReader);
		String tgtXMLString = resuableComponents.readXMLFile(tgtBuffReader);
		Document srcDoc = resuableComponents.getXMLDocument(srcXMLString);
		Document tgtDoc = resuableComponents.getXMLDocument(tgtXMLString);
		test.log(Status.INFO, "Retieve the Node details from Source and Target File systems");
		List<Map<String, String>> srcNodeMapList = resuableComponents.getNodeMapList(srcDoc,true);
		List<Map<String, String>> tgtNodeMapList = resuableComponents.getNodeMapList(tgtDoc,true);
		test.log(Status.INFO, "Comparison of Source and Target File system starts-------------------------");
		test.log(Status.INFO, "*******************************************");
		String srcXMLKeys=globalProp.getProperty("SrcXMLKeys1");
		String tgtXMLKeys=globalProp.getProperty("TgtXMLKeys1");
		String[] srcKeyArray=srcXMLKeys.split(";");
		String[] tgtKeyArray=tgtXMLKeys.split(";");
		int index=0;
		for(String srcKey:srcKeyArray){
			resuableComponents.compareXMLFieldValue(srcNodeMapList.get(index), tgtNodeMapList.get(index), test, srcKey, tgtKeyArray[index]);
			index++;
		}
		test.log(Status.INFO, "*******************************************");
		test.log(Status.INFO, "Comparison of Source and Target File system ends-------------------------");
	}
}
