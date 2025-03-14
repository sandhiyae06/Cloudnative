package com.APISchemaValidation;

import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import ResuableLibrary.ResuableComponents;
import baseTestPackage.BaseTest_TestNG;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;

public class SchemaValidation_Pass extends BaseTest_TestNG {

	ResuableComponents resuableComponents = new ResuableComponents();

	@Test(priority = 0)
	public void GetPost() throws Exception {
		test = report.createTest(" API 1 :Json Place holder ");
		test.log(Status.INFO, "Initiate Get call with  number in Payload-------");
		RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

		ValidatableResponse resp = resuableComponents.executeGetAPI("https://jsonplaceholder.typicode.com", "/posts/2",
				"application/json", test);
		int statusCode = resp.extract().statusCode();
		System.out.println("Response" + resp.extract().asString());
		resuableComponents.validateStatusCode(Integer.toString(statusCode), Integer.toString(200), test);
		resuableComponents.validateSchema(resp, "schema_pass.json", test);

	}

	@Test(priority = 1)
	public void GetBookerID() throws Exception {
		test = report.createTest("API 2 :API restful-booker ");
		test.log(Status.INFO, "Initiate Get call with Booking ID in Payload-------");
		RestAssured.baseURI = "https://restful-booker.herokuapp.com";

		ValidatableResponse resp = resuableComponents.executeGetAPI("https://restful-booker.herokuapp.com",
				"/booking/12", "application/json", test);

		int statusCode = resp.extract().statusCode();
		System.out.println("Response" + resp.extract().asString());
		resuableComponents.validateStatusCode(Integer.toString(statusCode), Integer.toString(200), test);
		resuableComponents.validateSchema(resp, "schema_restful-booker_pass.json", test);

	}
}

/*
 * ValidatableResponse resp= given(). when(). get("/booking/220"). then();
 */
