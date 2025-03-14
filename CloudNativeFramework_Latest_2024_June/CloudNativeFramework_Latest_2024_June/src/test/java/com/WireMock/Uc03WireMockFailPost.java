package com.WireMock;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.aventstack.extentreports.Status;
import com.github.tomakehurst.wiremock.WireMockServer;


import ResuableLibrary.MicroServiceReusables;
import ResuableLibrary.WireMockReusables;
import baseTestPackage.BaseTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class Uc03WireMockFailPost extends BaseTest {

	private static WireMockServer mockedServer;
	MicroServiceReusables resuableComponents = new MicroServiceReusables();
	WireMockReusables wireMockReusables = new WireMockReusables();
	@BeforeClass
	public static void setUpClass() {
		mockedServer = new WireMockServer();
		mockedServer.start();
	}
	@AfterClass
	public static void tearDown() {
		mockedServer.stop();
	}
	@Test
	public void test_post_all_mock() throws Exception {
		test = report.createTest("Mocking the server starts");
		test.log(Status.INFO, "Initiate POST call -------");
		wireMockReusables.createStubforPostCall("/api/CreateUser", 404, "User not Created Successfully");
		test.log(Status.INFO, "server mocked in local host-------");
		RestAssured.baseURI = "http://localhost:8080";
		Response resp = wireMockReusables.getMockedPostResponse("/api/CreateUser");
		test.log(Status.INFO, "Received Response -------- " + resp.asString());
		int statusCode = resp.getStatusCode();
		resuableComponents.validateStatusCode(Integer.toString(statusCode), Integer.toString(201), test);
		resuableComponents.validateResponse(resp.asString(), resp.asString(), test);
	}

}