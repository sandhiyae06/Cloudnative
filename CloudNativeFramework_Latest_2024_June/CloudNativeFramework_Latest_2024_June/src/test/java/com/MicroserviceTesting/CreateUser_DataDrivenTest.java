package com.MicroserviceTesting;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ResuableLibrary.ResuableComponents;
import baseTestPackage.BaseTest_TestNG;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class CreateUser_DataDrivenTest extends BaseTest_TestNG {
	ResuableComponents resuableComponents = new ResuableComponents();

	@Test(dataProvider = "UserData", groups = {"Regression" })
	public void addBook(String reqBody) throws Exception {
		test = report.createTest("Create Multiple Users using Data Provider");
		RestAssured.baseURI = globalProp.getProperty("baseURI");
		Response resp = resuableComponents.executePostAPI(EndPoints.endPointList.get("CREATEUSER"), reqBody, globalProp, test);
		int statusCode = resp.getStatusCode();
		resuableComponents.validateStatusCode(Integer.toString(statusCode), Integer.toString(201), test);
		resuableComponents.validatePartialResponseFieldValues(resp.asString(), reqBody, test);
	}

	@DataProvider(name = "UserData")
	public Object[][] getData() {
		return new Object[][] { 
			{ "{ \"name\": \"Tamil\", \"job\": \"manager\"}"},
			{ "{ \"name\": \"Abi\", \"job\": \"senior associate\"}"},
			};
	}
}
