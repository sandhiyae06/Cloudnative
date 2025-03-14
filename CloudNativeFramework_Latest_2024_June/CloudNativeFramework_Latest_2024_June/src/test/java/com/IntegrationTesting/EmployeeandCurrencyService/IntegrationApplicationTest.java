package com.IntegrationTesting.EmployeeandCurrencyService;

import static io.restassured.RestAssured.given;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import ResuableLibrary.ContractResuables;
import baseTestPackage.BaseTest;

public class IntegrationApplicationTest extends BaseTest {
	ContractResuables resuableComponents = new ContractResuables();
	/**
	 * Test Method to validate GetPerson service Integration
	 * 
	 * @throws Exception
	 */
	@Test
	@DisplayName("Get Person Details")
	public void get_person_from_service_contract() throws Exception {
		test = report.createTest("Get Person Details");
		//Freeze test data row and get values from Test data sheet
		freezeTestDataRow(this.getClass().getSimpleName());
		String strCustomerID = getValue("CustomerID");
		String strPassword = getValue("Password");
		String strAccountNo = getValue("AccountNumber");
		String strEndPointURL = getValue("EndPointURL");
		//Get the response as String
		String res = given().queryParam("CUSTOMER_ID", strCustomerID).queryParam("PASSWORD", strPassword)
				.queryParam("Account_No", strAccountNo).when().get(strEndPointURL).then().extract().asString();
		//Validate the response
		resuableComponents.validateResponse(res, res, test);
		//Pass specific value from response to next service for testing the integration
		if (res.contains("1")) {
			res = given().queryParam("CUSTOMER_ID", strCustomerID).queryParam("PASSWORD", strPassword)
					.queryParam("Account_No", strAccountNo).when().get(strEndPointURL).then().extract().asString();
			resuableComponents.validateResponse(res, res, test);
		}
	}


}