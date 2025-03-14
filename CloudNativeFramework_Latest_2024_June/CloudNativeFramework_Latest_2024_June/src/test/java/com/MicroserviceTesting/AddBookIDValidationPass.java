package com.MicroserviceTesting;

import org.junit.Test;

import com.aventstack.extentreports.Status;

import ResuableLibrary.ResuableComponents;
import baseTestPackage.BaseTest;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class AddBookIDValidationPass extends BaseTest {
	ResuableComponents resuableComponents = new ResuableComponents();

	@Test()
	public void addBook() throws Exception {
		String strAuthor = "Sample123555";
		String strCode = "8843401";
		test = report.createTest("Add Book Details to Library");
		test.log(Status.INFO, "Initiate POST call with Unique ISBN or ISLE number to the payload-------");
		test.log(Status.INFO, "Loading the base URI" + globalProp.getProperty("BaseURIServer"));
		test.log(Status.INFO, "Hitting the post request with AutorName as " + strAuthor + " and Code as " + strCode);
		RestAssured.baseURI = globalProp.getProperty("BaseURIServer");
		Response resp = resuableComponents.retrievePostResponseforDynamicData(strAuthor, strCode, globalProp);
		test.log(Status.INFO, "Retrieved Response " + resp.asString());
		test.log(Status.INFO, "****************Validation of Response starts***************");
		int statusCode = resp.getStatusCode();
		resuableComponents.validateResponse(Integer.toString(statusCode), Integer.toString(200), test);
		JsonPath js = new JsonPath(resp.asString());
		String id = js.get("ID");
		test.log(Status.INFO, "The ID of added book is " + id);
		resuableComponents.validateResponse(id, strAuthor + strCode, test);
		test.log(Status.INFO, "****************Validation of Response ends***************");
	}
}
