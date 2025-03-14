package com.XMLComparator;

import java.io.BufferedReader;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;
import org.w3c.dom.Document;

import com.aventstack.extentreports.Status;

import ResuableLibrary.ResponseComparatorResuables;
import baseTestPackage.BaseTest_TestNG;

public class UC06XMLFileTagComparisonFail extends BaseTest_TestNG {
	ResponseComparatorResuables resuableComponents = new ResponseComparatorResuables();

	/**
	 * Test method to Compare two XML files Tags
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
		List<Map<String, String>> srcNodeMapList = resuableComponents.getNodeMapList(srcDoc);
		List<Map<String, String>> tgtNodeMapList = resuableComponents.getNodeMapList(tgtDoc);
		test.log(Status.INFO, "Comparison of Source and Target File system starts-------------------------");
		test.log(Status.INFO, "*******************************************");
		resuableComponents.compareXMLKeys(srcNodeMapList, tgtNodeMapList, test);
		test.log(Status.INFO, "*******************************************");
		test.log(Status.INFO, "Comparison of Source and Target File system ends-------------------------");
	}
}
