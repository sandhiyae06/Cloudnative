package com.APISchemaValidation;

import static io.restassured.RestAssured.given;

import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import ResuableLibrary.ResuableComponents;
import baseTestPackage.BaseTest_TestNG;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;

public class SchemaValidation_Fail extends BaseTest_TestNG {

	ResuableComponents resuableComponents = new ResuableComponents();

	@Test(priority = 0)
	public void GetPost() throws Exception {
		test = report.createTest(" TC01 :To Validate Data Type ");
		test.log(Status.INFO, "Initiate Get call with  number in Payload-------");
		RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
		ValidatableResponse resp = resuableComponents.executeGetAPI("https://jsonplaceholder.typicode.com", "/posts/2",
				"application/json", test);

		int statusCode = resp.extract().statusCode();
		System.out.println("Response" + resp.extract().asString());
		resuableComponents.validateStatusCode(Integer.toString(statusCode), Integer.toString(200), test);
		resuableComponents.validateSchema(resp, "schema_Fail.json", test);

	}

	@Test(priority = 1)
	public void GetBookerID() throws Exception {
		test = report.createTest("TC02 :To Validate Missing Field  ");
		test.log(Status.INFO, "Initiate Get call with Booking ID in Payload-------");
		RestAssured.baseURI = "https://restful-booker.herokuapp.com";

		ValidatableResponse resp = resuableComponents.executeGetAPI("https://restful-booker.herokuapp.com","/booking/385", "application/json", test);

		int statusCode = resp.extract().statusCode();
		System.out.println("Response" + resp.extract().asString());
		resuableComponents.validateStatusCode(Integer.toString(statusCode), Integer.toString(200), test);
		resuableComponents.validateSchema(resp, "schema_restful-booker_Fail.json", test);

	}

	@Test(priority = 2)
	public void GetLengthValidation() throws Exception {
		test = report.createTest("TC03 :To Validate Length of String Field  ");
		test.log(Status.INFO, "Initiate Get call with Booking ID in Payload-------");
		RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

		ValidatableResponse resp = resuableComponents.executeGetAPI("https://jsonplaceholder.typicode.com", "/posts/2",
				"application/json", test);

		int statusCode = resp.extract().statusCode();
		System.out.println("Response" + resp.extract().asString());
		resuableComponents.validateStatusCode(Integer.toString(statusCode), Integer.toString(200), test);
		resuableComponents.validateSchema(resp, "schema_Length_Fail.json", test);

	}

	@Test(priority = 3)
	public void GetArrayValidation() throws Exception {
		test = report.createTest("TC04 :To Validate Array value in Schema//");
		test.log(Status.INFO, "Initiate Get call with Booking ID in Payload-------");
		RestAssured.baseURI = "https://petstore.swagger.io/v2";

		ValidatableResponse resp = resuableComponents.executeGetAPI("https://petstore.swagger.io/v2",
				"pet/findByStatus?status=available", "application/json", test);

		int statusCode = resp.extract().statusCode();
		System.out.println("Response" + resp.extract().asString());
		resuableComponents.validateStatusCode(Integer.toString(statusCode), Integer.toString(200), test);
		resuableComponents.validateSchemaArray(resp, "schema_Length_Fail.json", test);

	}

	@Test(priority = 4)
	public void GetNullCheckValidation() throws Exception {
		test = report.createTest("TC04 :To Validate null value in Schema//");
		test.log(Status.INFO, "Initiate Get call with Booking ID in Payload-------");
		RestAssured.baseURI = "https://petstore.swagger.io/v2";

		ValidatableResponse resp = resuableComponents.executeGetAPI("https://petstore.swagger.io/v2",
				"pet/findByStatus?status=available", "application/json", test);

		int statusCode = resp.extract().statusCode();
		System.out.println("Response" + resp.extract().asString());
		resuableComponents.validateStatusCode(Integer.toString(statusCode), Integer.toString(200), test);
		resuableComponents.validateSchemaArray(resp, "Schema_Pet_Swagger_null_fail.json", test);

	}
}
