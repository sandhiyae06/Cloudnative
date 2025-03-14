package ResuableLibrary;

import static io.restassured.RestAssured.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;


public class ContractResuables {

	// *Contract
	public String hitContractRequest(Properties globalProp) {
		return given().when().get(globalProp.getProperty("CurrencyConversionEndPointURL")).then().extract().asString();
	}

	// *Contract
	public String hitActualRequest(Properties globalProp) {
		return given().when().get(globalProp.getProperty("CurrencyConversionEndPointURL")).then().extract().asString();
	}

	// *Contract
	public String hitActualRequest(Properties globalProp, String strEndPointURL) {
		return given().when().get(globalProp.getProperty(strEndPointURL)).then().extract().asString();
	}

	// *Contract
	public String loadContractResponse(Properties globalProp, String strContractKey) throws Exception {
		String strContractResponse = globalProp.getProperty(strContractKey);
		return strContractResponse;
	}

	// *Contract
	public void validateContract(String strField, String strFieldValue, String strDataType, ExtentTest test) {
		test.log(Status.INFO, "Validation for the field '" + strField + "' starts----------->");
		if (NumberUtils.isNumber(strFieldValue)) {
			test.log(Status.PASS, "Validation for the field '" + strField
					+ "' is successfull. It is an Integer Value as expected----------->");
		} else {
			test.log(Status.PASS, "Validation for the field '" + strField
					+ "' is not successfull. It is not an Integer Value----------->");
		}

	}

	// *Contract
	public void validateContract(String strField, String strFieldValue, ExtentTest test) {
		test.log(Status.INFO, "Validation for the field '" + strField + "' starts----------->");
		if (StringUtils.isAlpha(strFieldValue)) {
			test.log(Status.PASS, "Validation for the field '" + strField
					+ "' is successfull. It is a String Value as expected----------->");
		} else {
			test.log(Status.PASS, "Validation for the field '" + strField
					+ "' is not successfull. It is not a String Value----------->");
		}

	}

	// *Contract
	public void validateContractAlpha(String strField, String strFieldValue, ExtentTest test) {
		test.log(Status.INFO, "Validation for the Key '" + strField + "' starts----------->");
		if (StringUtils.isAlpha(strFieldValue)) {
			test.log(Status.PASS, "Validation for the Key '" + strField
					+ "' is successfull. It is a String Value as expected----------->");
		} else {
			test.log(Status.PASS, "Validation for the Key '" + strField
					+ "' is not successfull. It is not a String Value----------->");
		}

	}

	// *Contract
	public void validateContractResponseFieldTypes(String actualResponse, String expectedResponse, ExtentTest test)
			throws Exception {
		JSONArray array = new JSONArray("[" + actualResponse + "]");
		for (int i = 0; i < array.length(); i++) {
			JSONObject object = array.getJSONObject(i);
			validateContract("id", Integer.toString(object.getInt("id")), "int", test);
			validateContract("from", object.getString("from"), test);
			validateContract("to", object.getString("to"), test);

		}
	}

	// *Contract
	public void validateResponseFields(String response, String actualResponse, ExtentTest test)
			throws UnsupportedEncodingException, JSONException {
		validateResponse(actualResponse, response, test);
		JSONArray array = new JSONArray("[" + actualResponse + "]");
		for (int i = 0; i < array.length(); i++) {
			JSONObject object = array.getJSONObject(i);
			validateContract("name", object.getString("name"), test);
			validateContract("surname", object.getString("surname"), test);
		}
	}// *Contract

	public void validateResponse(String actualResponse, String expectedResponse, ExtentTest test)
			throws UnsupportedEncodingException {
		test.log(Status.INFO, "Expected Response is ------->   " + expectedResponse);
		test.log(Status.INFO, "Actual Response is ------->   " + actualResponse);
		if (actualResponse.replaceAll(" ", "").equalsIgnoreCase(expectedResponse.replaceAll(" ", "")))
			test.log(Status.PASS, "Test Passed");
		else
			test.log(Status.FAIL, "Test Failed");
	}

	// *Contract
	public Map<String, String> setURIValuesinMap() {
		Map<String, String> uriVaribales = new HashMap<String, String>();
		uriVaribales.put("from", "USD");
		uriVaribales.put("to", "INR");
		return uriVaribales;
	}

	// *Contract
	public void hitMockServiceandValidate(MockMvc mockMvc, ExtentTest test, Properties globalProp) throws Exception {
		ResultActions result = mockMvc.perform(
				MockMvcRequestBuilders.get(globalProp.getProperty("endpoint")).contentType(MediaType.APPLICATION_JSON));

		test.log(Status.INFO, "Validating SUccessful Response code");
		test.log(Status.PASS, "Test Passed--->  " + result.andExpect(status().isOk()).andReturn());
	}

	// *Contract
	public String getResponseAsString(MockMvc mockMvc, String strEndPoint)
			throws Exception, UnsupportedEncodingException {
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get(strEndPoint).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		String actualResponse = result.getResponse().getContentAsString();
		return actualResponse;
	}

	// *Contract
	public void validateKey(String strField, String strFieldValue, ExtentTest test) {
		test.log(Status.INFO, "Validation for the Key '" + strField + "' starts----------->");
		if (strField.equalsIgnoreCase(strFieldValue)) {
			test.log(Status.PASS, "Validation for the Key '" + strField + "' is successfull.");
		} else {
			test.log(Status.PASS, "Validation for the Key '" + strField
					+ "' is not successfull. It is not a String Value----------->");
		}

	}
}