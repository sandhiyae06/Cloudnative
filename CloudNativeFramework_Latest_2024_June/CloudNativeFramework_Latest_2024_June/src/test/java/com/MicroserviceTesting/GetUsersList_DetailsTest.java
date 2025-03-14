package com.MicroserviceTesting;

import org.testng.annotations.Test;

import ResuableLibrary.ResuableComponents;
import baseTestPackage.BaseTest_TestNG;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class GetUsersList_DetailsTest extends BaseTest_TestNG {
	ResuableComponents resuableComponents = new ResuableComponents();

	@Test(groups = { "Smoke", "Regression"})
	public void getAllUserTest() throws Exception {
		test = report.createTest("Get All the User Details");
		RestAssured.baseURI = globalProp.getProperty("baseURI");
		Response resp = resuableComponents.executeGetAPI(EndPoints.endPointList.get("ALLUSER"), globalProp, test);
		int statusCode = resp.getStatusCode();
		resuableComponents.validateStatusCode(Integer.toString(statusCode), Integer.toString(400), test);
	}	
}
