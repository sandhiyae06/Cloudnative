package com.ContractTesting.ConversionService;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import ResuableLibrary.ResuableComponents;
import baseTestPackage.BaseTest_TestNG;
import io.restassured.response.Response;

public class CurrencyConversionTest2 extends BaseTest_TestNG {

	static String contractResponse;

	ResuableComponents resuableComponents = new ResuableComponents();

	/**
	 * Test Method to validate Currency Conversion service
	 * 
	 * @throws Exception
	 */

	@Test
	@DisplayName("AUD to INR Convertor Sequence validation")
	public void validateResponseDetailsOrderTest() throws Exception {

		test = report.createTest("AUD_INR_Convertor_Contract Test to check response tag Sequence");
		List<String> contractResponse1 = resuableComponents.loadContractResponseDetails(globalProp, "contractfilePath");
		test.log(Status.INFO, "Loading the Contract Response " + contractResponse);
		// Hit the API to get the actual response
		Response actualResponse = resuableComponents.executeGetAPI(
				globalProp.getProperty("CurrencyConversionEndPointURL1"), globalProp.getProperty("contentType"), test);
		// Validate the actual response with contract response
		resuableComponents.validateStatusCode(Integer.toString(actualResponse.getStatusCode()),
				contractResponse1.get(0), test);
		test.log(Status.INFO, "**Validation of Actual Response order Vs expected order defined in Contract**");
		resuableComponents.validateContractResponseOrderSequence(actualResponse.asString().trim(),
				contractResponse1.get(1), test);
		test.log(Status.INFO, "**Validation of Actual Response order Vs expected order defined in Contract ends**");
		test.log(Status.INFO, "**Validation of Actual Response Field Type Vs Contract Response Field Type starts **");
		// resuableComponents.validateContractResponseFieldTypes(actualResponse.asString().trim(),
		// contractResponse1.get(1), test);
		test.log(Status.INFO, "**Validation of Actual Response Field Type Vs Contract Response Field Type ends **");
	}

	@Test
	@DisplayName("AUD to INR Convertor Sequence validation Negative TC")
	public void validateResponseDetailsOrder_Negative() throws Exception {

		test = report.createTest("AUD_INR_Convertor_Contract Test to check response tag Sequence");
		List<String> contractResponse1 = resuableComponents.loadContractResponseDetails(globalProp,
				"contractfilePathNegativeTC");
		test.log(Status.INFO, "Loading the Contract Response " + contractResponse);
		// Hit the API to get the actual response
		Response actualResponse = resuableComponents.executeGetAPI(
				globalProp.getProperty("CurrencyConversionEndPointURL1"), globalProp.getProperty("contentType"), test);
		// Validate the actual response with contract response
		resuableComponents.validateStatusCode(Integer.toString(actualResponse.getStatusCode()),
				contractResponse1.get(0), test);
		// test.log(Status.INFO, "**Validation of Actual Response order Vs expected
		// order defined in Contract**");
		resuableComponents.validateContractResponseOrderSequence(actualResponse.asString().trim(),
				contractResponse1.get(1), test);
		// test.log(Status.INFO, "**Validation of Actual Response order Vs expected
		// order defined in Contract ends**");
		test.log(Status.INFO, "**Validation of Actual Response Field Type Vs Contract Response Field Type starts **");
		resuableComponents.validateContractResponseFieldTypes(actualResponse.asString().trim(),
				contractResponse1.get(1), test);
		test.log(Status.INFO, "**Validation of Actual Response Field Type Vs Contract Response Field Type ends **");
	}
}
