package ResuableLibrary;

import static io.restassured.RestAssured.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.MatcherAssert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.skyscreamer.jsonassert.FieldComparisonFailure;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


import files.payload;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import static org.hamcrest.MatcherAssert.assertThat;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class ResuableComponents {

	// Pact Contract File Testing
	public Response executePactGetAPI(Map<String, Object> pactTC, ExtentTest test) {
		test.log(Status.INFO, "<b>Request Type: </b> GET");
		test.log(Status.INFO, "<b>URL: </b>" + pactTC.get("path"));
		Response res = given().header("Content-Type", pactTC.get("contentType")).when()
				.get(pactTC.get("path").toString()).then().extract().response();
		test.log(Status.INFO, "<b>Response: </b>" + res.asString());
		return res;
	}

	// Pact Contract File Testing
	public Response executePactPostAPI(Map<String, Object> pactTC, ExtentTest test) {
		test.log(Status.INFO, "<b>Request Type: </b> POST");
		test.log(Status.INFO, "<b>URL: </b>" + pactTC.get("path"));
		Response res = given().header("Content-Type", pactTC.get("contentType")).body(pactTC.get("reqbody")).when()
				.post(pactTC.get("path").toString()).then().extract().response();
		test.log(Status.INFO, "<b>Response: </b>" + res.asString());
		return res;
	}

	

	// *MS
	public Response executePostAPI(String endPointUrl, String body, Properties globalProp, ExtentTest test) {
		test.log(Status.INFO, "<b>Request Type: </b> POST");
		test.log(Status.INFO, "<b>URL: </b>" + globalProp.getProperty("baseURI") + endPointUrl);
		test.log(Status.INFO, "<b>Request Body: </b>" + body);
		Response res = given().header("Content-Type", globalProp.getProperty("contentType")).body(body).when()
				.post(endPointUrl).then().extract().response();
		test.log(Status.INFO, "<b>Response: </b>" + res.asString());
		return res;
	}

	// *MS
	public Response executeGetAPI(String endPointUrl, Properties globalProp, ExtentTest test) {
		test.log(Status.INFO, "<b>Request Type: </b> GET");
		test.log(Status.INFO, "<b>URL: </b>" + globalProp.getProperty("baseURI") + endPointUrl);
		Response res = given().header("Content-Type", globalProp.getProperty("contentType")).when().get(endPointUrl)
				.then().extract().response();
		test.log(Status.INFO, "<b>Response: </b>" + res.asString());
		return res;
	}

	// *Contract Testing
	public Response executeGetAPI(String endPointUrl, String contentType, ExtentTest test) {
		test.log(Status.INFO, "<b>Request Type: </b> GET");
		test.log(Status.INFO, "<b>URL: </b>" + endPointUrl);
		Response res = given().header("Content-Type", contentType).when().get(endPointUrl).then().extract().response();
		test.log(Status.INFO, "<b>Response: </b>" + res.asString());
		return res;
	}

	// *MS
		public void validatePartialResponseFieldValues(String actualResponse, String expectedResponse, ExtentTest test)
				throws Exception {
			JSONObject actualFields = new JSONObject(actualResponse);
			JSONObject expectedFields = new JSONObject(expectedResponse);
			for (Object key : expectedFields.keySet()) {
				String expectedValue = (String) expectedFields.get((String) key);
				String actualValue = (String) actualFields.get((String) key);
				if (expectedValue.trim().equalsIgnoreCase(actualValue)) {
					test.log(Status.PASS, "Valaidate data for <b>'" + key + "'<b><br> Actual: " + actualValue
							+ "<br> Expected: " + expectedValue);
				} else {
					test.log(Status.FAIL, "Valaidate data for <b>'" + key + "'<b><br> Actual: " + actualValue
							+ "<br> Expected: " + expectedValue);
				}
			}
		}
		
	// *MS
	public Response retrievePostResponseforDynamicData(String contentType, String body, String endPointUrl) {
		return given().header("Content-Type", contentType).body(body).when().post(endPointUrl).then().extract()
				.response();
	}

	// *MS
	public Response retrieveGetResponse(String contentType, String endPointUrl) {
		return given().header("Content-Type", contentType).when().get(endPointUrl).then().extract().response();
	}

	// *MS & Contract Testing
	public JSONCompareResult validateResponse(String actualResponse, String expectedResponse, ExtentTest test)
			throws UnsupportedEncodingException {
		JSONCompareResult result = null;
		try {
			JSONObject actualObject = new JSONObject(actualResponse);
			JSONObject expectedObject = new JSONObject(expectedResponse);
			result = JSONCompare.compareJSON(actualObject, expectedObject, JSONCompareMode.LENIENT);
			if (result.passed()) {
				test.log(Status.PASS, "<b>Expected Response:</b> " + expectedResponse
						+ "<br><b>Actual Response:</b> " + actualResponse);
			} else {
				test.log(Status.FAIL, "<b>Expected Response:</b> " + expectedResponse
						+ "<br><b>Actual Response:</b> " + actualResponse);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

	// *MS & Contract Testing
	public void validateResponseLogJSONDifference(String actualResponse, String expectedResponse, ExtentTest test)
			throws UnsupportedEncodingException {
		JSONCompareResult res = null;
		try {
			JSONObject actualObject = new JSONObject(actualResponse);
			JSONObject expectedObject = new JSONObject(expectedResponse);
			res = JSONCompare.compareJSON(actualObject, expectedObject, JSONCompareMode.LENIENT);
			if (res.passed()) {
				test.log(Status.PASS, "<b>Expected Response:</b> " + expectedResponse
						+ "<br><b>Actual Response:</b> " + actualResponse);
			} else {
				test.log(Status.FAIL, "<b>Expected Response:</b> " + expectedResponse
						+ "<br><b>Actual Response:</b> " + actualResponse);
			}
			if (res.isFailureOnField()) {
				List<FieldComparisonFailure> fFailures = res.getFieldFailures();
				for (FieldComparisonFailure fail : fFailures) {
					String fieldName = fail.getField().toString();
					if (fieldName.contains("[") && fieldName.contains("]")) {
						int sIndex = fieldName.indexOf('[');
						int lIndex = fieldName.lastIndexOf(']') + 1;
						fieldName = fieldName.replace(fieldName.substring(sIndex, lIndex), "");
					}
					// String fieldName = fail.getField().toString().split("\\].")[1];
					String expectedValue = fail.getActual().toString();
					String actualValue = fail.getExpected().toString();
					String expectedFieldType = fail.getActual().getClass().getSimpleName();
					String actualFieldType = fail.getExpected().getClass().getSimpleName();
					test.log(Status.FAIL,
							"Field validation failed for <b>'" + fieldName + "'</b><br> Expected Value is <b>'"
									+ expectedValue + "'</b>, Actual Value is <b>'" + actualValue
									+ "'</b>.<br> Expected Field Data Type is <b>'" + expectedFieldType
									+ "'</b>, Actual Field Data Type is <b>'" + actualFieldType + "'</b>.");
				}
			}
			if (res.isMissingOnField()) {
				List<FieldComparisonFailure> fFailures = res.getFieldMissing();
				for (FieldComparisonFailure fail : fFailures) {
					String fieldName = fail.getField().toString();
					String expectedValue = fail.getExpected().toString();
					test.log(Status.FAIL,
							"<b>'" + fieldName + "' </b>field is missing in the actual response. Expected Value is <b>'"
									+ expectedValue + "'</b>.");
				}
			}
			if (res.isUnexpectedOnField()) {
				List<FieldComparisonFailure> fFailures = res.getFieldUnexpected();
				for (FieldComparisonFailure fail : fFailures) {
					String fieldName = fail.getField().toString();
					test.log(Status.FAIL,
							"<b>'" + fieldName + "' </b>field is added additionally in the actual response.");
				}
			}
		} catch (JSONException e) {
			test.log(Status.FAIL, "JSON Comparison failed<br>" + e.getMessage());
			e.printStackTrace();
		}
	}

	// *MS & Contract Testing
	public void validateSchemaResponseLogJSONDifference(String actualResponse, String expectedResponse, ExtentTest test)
			throws UnsupportedEncodingException {
		JSONCompareResult res = null;
		try {
			JSONObject actualObject = new JSONObject(actualResponse);
			JSONObject expectedObject = new JSONObject(expectedResponse);
			res = JSONCompare.compareJSON(actualObject, expectedObject, JSONCompareMode.LENIENT);
			/*
			 * if (res.passed()) { test.log(Status.PASS, "<b>Expected Response:</b> " +
			 * expectedResponse + "<br><b>Actual Response:</b> " + actualResponse); } else {
			 * test.log(Status.FAIL, "<b>Expected Response:</b> " + expectedResponse +
			 * "<br><b>Actual Response:</b> " + actualResponse); }
			 */
			if (res.isFailureOnField()) {
				List<FieldComparisonFailure> fFailures = res.getFieldFailures();
				for (FieldComparisonFailure fail : fFailures) {
					String fieldName = fail.getField().toString();
					if (fieldName.contains("[") && fieldName.contains("]")) {
						int sIndex = fieldName.indexOf('[');
						int lIndex = fieldName.lastIndexOf(']') + 1;
						fieldName = fieldName.replace(fieldName.substring(sIndex, lIndex), "");
					}
					// String fieldName = fail.getField().toString().split("\\].")[1];
					String expectedValue = fail.getActual().toString();
					String actualValue = fail.getExpected().toString();
					String expectedFieldType = fail.getActual().getClass().getSimpleName();
					String actualFieldType = fail.getExpected().getClass().getSimpleName();
					if (!actualFieldType.equalsIgnoreCase(expectedFieldType)) {
						test.log(Status.FAIL,
								"Field validation failed for <b>'" + fieldName + "'</b><br> Expected Value is <b>'"
										+ expectedValue + "'</b>, Actual Value is <b>'" + actualValue
										+ "'</b>.<br> Expected Field Data Type is <b>'" + expectedFieldType
										+ "'</b>, Actual Field Data Type is <b>'" + actualFieldType + "'</b>.");
					}
				}
			}
			if (res.isMissingOnField()) {
				List<FieldComparisonFailure> fFailures = res.getFieldMissing();
				for (FieldComparisonFailure fail : fFailures) {
					String fieldName = fail.getField().toString();
					String expectedValue = fail.getExpected().toString();
					test.log(Status.FAIL,
							"<b>'" + fieldName + "' </b>field is missing in the actual response. Expected Value is <b>'"
									+ expectedValue + "'</b>.");
				}
			}
			if (res.isUnexpectedOnField()) {
				List<FieldComparisonFailure> fFailures = res.getFieldUnexpected();
				for (FieldComparisonFailure fail : fFailures) {
					String fieldName = fail.getField().toString();
					test.log(Status.FAIL,
							"<b>'" + fieldName + "' </b>field is added additionally in the actual response.");
				}
			}
		} catch (JSONException e) {
			test.log(Status.FAIL, "JSON Comparison failed<br>" + e.getMessage());
			e.printStackTrace();
		}
	}

	// *MS
	public void validateStatusCode(String actualStatusCode, String expectedStatusCode, ExtentTest test)
			throws UnsupportedEncodingException {
		if (actualStatusCode.replaceAll(" ", "").equalsIgnoreCase(expectedStatusCode.replaceAll(" ", "")))
			test.log(Status.PASS, "<b>Expected Status Code: </b> " + expectedStatusCode
					+ "    <br><b>Actual Status Code: </b> " + actualStatusCode);
		else
			test.log(Status.FAIL, "<b>Expected Status Code: </b> " + expectedStatusCode
					+ "    <br><b>Actual Status Code: </b> " + actualStatusCode);
	}

	// *MS
	public void validateJSONResponse(String actualResponse, String expectedValue, String strJsonPath, ExtentTest test) {
		JsonPath jsonPath = new JsonPath(actualResponse);
		String strActualValue = jsonPath.getString(strJsonPath);
		if (expectedValue.equalsIgnoreCase(strActualValue)) {
			test.log(Status.PASS, "Test Passed");
		} else {
			test.log(Status.FAIL, "Test Failed");
		}
	}

	// *MS
	public void validateXMLResponse(String actualResponse, String expectedValue, String strJsonPath, ExtentTest test) {
		XmlPath xmlPath = new XmlPath(actualResponse);
		String strActualValue = xmlPath.getString(strJsonPath);
		if (expectedValue.equalsIgnoreCase(strActualValue)) {
			test.log(Status.PASS, "Test Passed");
		} else {
			test.log(Status.FAIL, "Test Failed");
		}
	}

	// *MS
	public Response retrievePostResponse(String strPayload, Properties globalProp) {
		return given().header("Content-Type", "application/json").body(strPayload).when()
				.post(globalProp.getProperty("endpoint")).then().extract().response();
	}

	// *MS
	public Response retrievePostResponseforDynamicData(String strAuthor, String strCode, Properties globalProp) {
		return given().header("Content-Type", "application/json").body(Addbook(strAuthor, strCode)).when()
				.post(globalProp.getProperty("endpoint")).then().extract().response();
	}

	// *MS
	public Response retrieveGetResponse(Properties globalProp) {
		return given().header("Content-Type", "application/json").when().get(globalProp.getProperty("getendpoint"))
				.then().extract().response();
	}

	// *MS
	public static String Addbook(String isbn, String aisle) {
		String payload = "{\r\n" + " \"name\" : \"Learn Appium Automation with Java\",\r\n" + " \"isbn\":  \"" + isbn
				+ "\",\r\n" + " \"aisle\":\"" + aisle + "\",\r\n" + " \"author\":\"kirthika\"\r\n" + "}\r\n" + "";
		return payload;
	}
	// *MS

	public static String Deletebook(String id) {
		String payload = "{\r\n" + " \"ID\" : \"" + id + "\",\r\n" + "}\r\n" + "";
		return payload;
	}

	// *MS
	public Response hitEndpointandGetResponse(Properties globalProp) {
		return given().header("Content-Type", "application/json")
				.body(payload.Addbook(globalProp.getProperty("BookName"), globalProp.getProperty("BookCode"))).when()
				.post(globalProp.getProperty("EndPoint")).then().extract().response();
	}

	// *MS
	public void validateIndividualFields(Response resp, ExtentTest test, String strFieldName, String strExpectedValue)
			throws Exception {
		JsonPath js = new JsonPath(resp.asString());
		String strActualValue = js.get(strFieldName);
		validateResponse(strActualValue, strExpectedValue, test);
	}

	// *Contract
	public String hitContractRequest(Properties globalProp) {
		return given().when().get(globalProp.getProperty("CurrencyConversionEndPointURL")).then().extract().asString();
	}

	// *Contract
	public String hitActualRequest(Properties globalProp) {
		return given().when().get(globalProp.getProperty("CurrencyConversionEndPointURL")).then().extract().asString();
	}

	// *Contract
	public String loadContractResponse(Properties globalProp) throws Exception {
		String strContractResponse = globalProp.getProperty("contractResponse");
		return strContractResponse;
	}

	// *Contract
	public String loadContractResponse(Properties globalProp, String contract) throws Exception {
		String strContractResponse = globalProp.getProperty(contract);
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
		JSONObject actualFields = new JSONObject(actualResponse);
		JSONObject expectedFields = new JSONObject(expectedResponse);
		for (Object key : expectedFields.keySet()) {
			String expectedKeyType = (String) expectedFields.get((String) key).getClass().getSimpleName();
			String actualKeyType = actualFields.get((String) key).getClass().getSimpleName();
			if (expectedKeyType.trim().equalsIgnoreCase(actualKeyType)) {
				test.log(Status.PASS, "Valaidate data type for <b>'" + key + "'<b><br> Actual: " + actualKeyType
						+ "<br> Expected: " + expectedKeyType);
			} else {
				test.log(Status.FAIL, "Valaidate data type for <b>'" + key + "'<b><br> Actual: " + actualKeyType
						+ "<br> Expected: " + expectedKeyType);
			}
		}
	}

	// *Contract
	public void validateResponseFields(String response, String actualResponse, ExtentTest test)
			throws UnsupportedEncodingException, JSONException {
		validateResponse(actualResponse, response, test);
		JSONArray array = new JSONArray("[" + actualResponse + "]");
		for (int i = 0; i < array.length(); i++) {
			JSONObject object = array.getJSONObject(i);
			validateContract("id", object.getString("id"), test);
			validateContract("name", object.getString("name"), test);
		}
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

	

	@SuppressWarnings("unchecked")
	public void validateContractResponseOrderSequence(String actualResponse, String expectedResponse, ExtentTest test) throws ParseException, JsonMappingException, JsonProcessingException {
		// TODO Auto-generated method stub
		//JSONObject actualFields = new JSONObject(actualResponse);
		//JSONObject expectedFields = new JSONObject(expectedResponse);
		
		 ObjectMapper objectMapper = new ObjectMapper();
	       objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
	       Map<String, Object> actualFields = (Map<String, Object>) objectMapper.readValue(actualResponse, LinkedHashMap.class);
	       Map<String, Object> expectedFields = (Map<String, Object>) objectMapper.readValue(expectedResponse, LinkedHashMap.class);

	       ObjectMapper objectMapper1 = new ObjectMapper();
	       JsonNode actualNode = objectMapper1.valueToTree(actualFields);
           JsonNode expectedNode = objectMapper1.valueToTree(expectedFields);
           // Compare JSON nodes
           boolean result= actualNode.equals(expectedNode);
	       System.out.println(result);
	     //  JSONAssert.assertEquals(actualResponse, expectedResponse, JSONCompareMode.STRICT);
	       if (result== false) {   
	    	   List<String> differences = new ArrayList<>();
	    	   printFieldDifferences(actualNode, expectedNode,test);
	    	   
	       }
	       else {
	    	   test.log(Status.PASS, "<b>Response fields order validation matched<br>Expected Response:</b> " + expectedResponse
						+ "<br><b>Actual Response:</b> " + actualResponse ); 
	       }
	      
				
			
	       
	//	JSONAssert.assertEquals(actualFields, expectedFields, JSONCompareMode.STRICT); 
		
		/*
		 try {
	               // Compare JSON strings
	               JSONAssert.assertEquals(actualResponse, expectedResponse, JSONCompareMode.STRICT);
	               System.out.println("JSON strings are equal.");
	           } catch (AssertionError e) {
	               System.out.println("JSON strings are not equal.");
	               System.out.println("Differences: " + e.getMessage());
	           } 
		 
		  for (Object key : expectedFields.keySet()) {
			String expectedKeyType = (String) expectedFields.get((String) key).getClass().getSimpleName();
			String actualKeyType = actualFields.get((String) key).getClass().getSimpleName();
			if (expectedKeyType.trim().equalsIgnoreCase(actualKeyType)) {
				test.log(Status.PASS, "Valaidate data type for <b>'" + key + "'<b><br> Actual: " + actualKeyType
						+ "<br> Expected: " + expectedKeyType);
			} else {
				test.log(Status.FAIL, "Valaidate data type for <b>'" + key + "'<b><br> Actual: " + actualKeyType
						+ "<br> Expected: " + expectedKeyType);
			}
		}*/
	}

	@SuppressWarnings("unchecked")
	private void findDifferences(JsonNode actualNode, JsonNode expectedNode, String path, List<String> differences) {
		// TODO Auto-generated method stub
		 if (!actualNode.equals(expectedNode)) {
	           differences.add("Mismatch at path: " + path +
	                   "\nActual: " + actualNode.toString() +
	                   "\nExpected: " + expectedNode.toString());
	       }
	       Set<String> allFieldNames = new HashSet<>();
	       allFieldNames.addAll((Collection<? extends String>) actualNode.fieldNames());
	       allFieldNames.addAll((Collection<? extends String>) expectedNode.fieldNames());
	       for (String fieldName : allFieldNames) {
	           String newPath = path.isEmpty() ? fieldName : path + "." + fieldName;
	           if (actualNode.has(fieldName) && expectedNode.has(fieldName)) {
	               findDifferences(actualNode.get(fieldName), expectedNode.get(fieldName), newPath, differences);
	           } else if (actualNode.has(fieldName)) {
	               differences.add("Missing in expected at path: " + newPath + "\nActual: " + actualNode.get(fieldName));
	           } else if (expectedNode.has(fieldName)) {
	               differences.add("Missing in actual at path: " + newPath + "\nExpected: " + expectedNode.get(fieldName));
	           }
	       }
	   
	}
	
	 private static void printFieldDifferences(JsonNode actualNode, JsonNode expectedNode,ExtentTest test) {
		 boolean ordercompare=true ;
	       System.out.println("Differences in field names and order:");
	       Iterator<String> actualFields = actualNode.fieldNames();
	       Iterator<String> expectedFields = expectedNode.fieldNames();
	       test.log(Status.INFO, "<b>Response fields order validation - starts</b>");
	       while (actualFields.hasNext() || expectedFields.hasNext()) {
	           String actualField = actualFields.hasNext() ? actualFields.next() : null;
	           String expectedField = expectedFields.hasNext() ? expectedFields.next() : null;
	           if (!actualField.equals(expectedField)) {
	        	   test.log(Status.FAIL, "<b>Expected field :</b> " + expectedField
							+ "<br><b>Actual field:</b> " + actualField);
	               System.out.println("Expected field: " + expectedField);
	               System.out.println("Actual field: " + actualField);
	               System.out.println();
	               ordercompare=false ;
	           }
	       }
	       if (ordercompare==true ) {
	    	   test.log(Status.PASS, "<b>Response fields order validation matched</b> "); 
	       }
	       test.log(Status.INFO, "<b>Response fields order validation - Ends</b>");
	   }
	 
	 public  JsonElement loadJSONFile(String strFilePath) throws Exception {
			FileReader fileReader = new FileReader(strFilePath);
			JsonElement	jsonElement = JsonParser.parseReader(fileReader);
		
		return jsonElement;
	}
	 
	 public  String loadJSONFile1(String strFilePath) throws Exception {
			FileReader fileReader = new FileReader(strFilePath);
			JsonElement	jsonElement = JsonParser.parseReader(fileReader);	
			return jsonElement.toString();
		}
	 
	 public List<String> loadContractResponseDetails(Properties globalProp,String strFilePath) throws Exception {
		 
		 List<String> apiResponseList=new ArrayList<>();
		 //String json =loadJSONFile1("C:\\Project\\Code-Base\\Contract_Workspace\\CloudNativeFramework\\src\\main\\resources\\contract-testing-files\\contractForConsumerService-USDtoINR.json");
		 String json=loadJSONFile1(globalProp.getProperty(strFilePath));
		 try {  
	            ObjectMapper mapper = new ObjectMapper();  
	            JsonNode jsonNode = mapper.readTree(json);  
	            
	            JsonNode responseStatusNode = jsonNode.get("response").get("status");  
	            String responseStatus = responseStatusNode.asText();  
	            apiResponseList.add(responseStatus);
	            JsonNode responseBodyNode = jsonNode.get("response").get("body");  
	            String responseBody = responseBodyNode.asText();  
	            apiResponseList.add(responseBody);
	            
	            System.out.println("Response Body: " + responseBody);  
	            System.out.println("Response Status: " + responseStatus);  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }
		return apiResponseList; 
		 
		 
	 }
	 
	//Ms schema validation 	
		public ValidatableResponse executeGetAPI(String baseURI,String endPointUrl ,String contentType , ExtentTest test ) {
			test.log(Status.INFO, "<b>Request Type: </b> GET");
			test.log(Status.INFO, "<b>URL: </b>" + baseURI + endPointUrl);
			ValidatableResponse res = given().header("Content-Type", contentType).when().get(endPointUrl)
					.then();
			test.log(Status.INFO, "<b>Response: </b>" + res.extract().asString());
			return res;
		}
	//Ms schema validation 
		public void validateSchema(ValidatableResponse response, String JsonSchemafilename, ExtentTest test) {
			try {
				assertThat(response.extract().body().asString(), matchesJsonSchemaInClasspath(JsonSchemafilename));
				// test.log(Status.INFO, "Expected Response is -------> " + );
				test.log(Status.PASS, "Schema Validation Test Passed");
			} catch (AssertionError e) {
				// TODO Auto-generated catch block
				String error = e.toString();
				System.out.println("print   " + error + " end of printing ");
				test.log(Status.FAIL, "Schema validation failed for below field : ");
				if (error.contains("error:") && error.contains("object has missing required properties")) {
					String[] e1 = error.split("error:");			
					int temp ;
					for ( temp = 1; temp < e1.length; temp++) {
						
						String[] FieldNametemp1 =e1[temp].split("object has missing required properties ");
						String[] FieldNametemp2  = FieldNametemp1[1].split("]");
						String FieldName= FieldNametemp2[0].replace("\"","");
						String[] Actualvaluetemp = FieldNametemp2[2].split(" missing:");
						String[] Expectedvaluetemp = FieldNametemp2[1].split(" required:");
						String Actualvalue = Actualvaluetemp[0];
						String Expectedvaluet = Expectedvaluetemp[1];
						String Expectedvalue;
						String actualresponse = null;
						if (Expectedvaluet.contains("but: was")) {
							String[] Expectedvaluetemp1=	Expectedvaluet.split("but: was");
							 Expectedvalue=Expectedvaluetemp1[0];
							 actualresponse = Expectedvaluetemp1[1];
						}
						else
						{
							Expectedvalue=Expectedvaluetemp[1];
						}
						 FieldName= FieldName.replace("([", "");
						 FieldName=FieldName.replace("/", "");
						 Expectedvalue=Expectedvalue.replace("[\"", "").replace("\"]", "");
						 Actualvalue =Actualvalue.replace("\"", "");
						System.out.println("Error " + temp + " : " +"fielddName : "+ FieldName +"Actualvalue :" +Actualvalue +"Expectedvalue :" + Expectedvalue );
						if (Actualvalue.trim().equals("")) {
							test.log(Status.FAIL,
									"Schema validation- field missing : <b>'" + FieldName );
						}else {
						test.log(Status.FAIL,
								"Schema Field validation failed for field# <b>'" + FieldName + "'</b><br> Expected field Data Type is <b>'"
										+ Expectedvalue + "'</b>, Actual field Data Type is  <b>'" + Actualvalue
										+ "'</b>.");
						}
						/*
						 * test.log(Status.INFO, "Actual response <b>'" + actualresponse + "'</b>.");
						 */
					}
					
					
				}
				
				else  {
					
					String[] e1 = error.split("error:");			
					int temp ;
					for ( temp = 1; temp < e1.length; temp++) {
						
						String[] FieldNametemp1 =e1[temp].split("instance: ");
						String[] FieldNametemp2  = FieldNametemp1[1].split("}");
						String FieldName= FieldNametemp2[0].replace("{\"pointer\":","");
						String[] Actualvaluetemp = FieldNametemp2[1].split("found:");
						String[] Expectedvaluetemp = null;
						if (Actualvaluetemp[1].contains("maxLength"))
						{
							 Expectedvaluetemp = Actualvaluetemp[1].split("maxLength:");
						}
						else if (Actualvaluetemp[1].contains("expected")) {
							 Expectedvaluetemp = Actualvaluetemp[1].split("expected:");
						}
						else if (Actualvaluetemp[1].contains("minLength")) {
							 Expectedvaluetemp = Actualvaluetemp[1].split("minLength:");
						}
						String Actualvalue = Expectedvaluetemp[0];
						String Expectedvaluet = Expectedvaluetemp[1];
						String Expectedvalue;
						String actualresponse = null;
						if (Expectedvaluet.contains("but: was")) {
							String[] Expectedvaluetemp1=	Expectedvaluet.split("but: was");
							 Expectedvalue=Expectedvaluetemp1[0];
							 actualresponse = Expectedvaluetemp1[1];
						}
						else
						{
							Expectedvalue=Expectedvaluetemp[1];
						}
						 FieldName= FieldName.replace("\"/", "").replace("\"", "");
						 FieldName=FieldName.replace("/", ".");
						 Expectedvalue=Expectedvalue.replace("[\"", "").replace("\"]", "");
						 Actualvalue =Actualvalue.replace("\"", "");
						System.out.println("Error " + temp + " : " +"fielddName : "+ FieldName +"Actualvalue :" +Actualvalue +"Expectedvalue :" + Expectedvalue );
						test.log(Status.FAIL,
								"Schema Field validation failed for field# <b>'" + FieldName + "'</b><br> Expected field Data Type is <b>'"
										+ Expectedvalue + "'</b>, Actual field Data Type is  <b>'" + Actualvalue
										+ "'</b>.");
						/*
						 * test.log(Status.INFO, "Actual response <b>'" + actualresponse + "'</b>.");
						 */
					}
				}
			
			}
		}
		
		//Ms schema validation 
			public void validateSchemaArray(ValidatableResponse response, String JsonSchemafilename, ExtentTest test) {
				try {
					//assertThat(response.extract().body().asString(), matchesJsonSchemaInClasspath(JsonSchemafilename));
					MatcherAssert.assertThat(payload.PayloadforNullSchemavalidation(), JsonSchemaValidator.matchesJsonSchema(new File("C:\\Project\\Code-Base\\qea-tech-coe_automation-frameworks_cloud-native-AWSLambdaImplementation\\src\\main\\resources\\Schema_Pet_Swagger_fail.json")));
					// test.log(Status.INFO, "Expected Response is -------> " + );
					test.log(Status.PASS, "Schema Validation Test Passed");
				} catch (AssertionError e) {
					// TODO Auto-generated catch block
					String error = e.toString();
					System.out.println("print   " + error + " end of printing ");
					test.log(Status.FAIL, "Schema validation failed for below field : ");
					if (error.contains("error:") && error.contains("object has missing required properties")) {
						String[] e1 = error.split("error:");			
						int temp ;
						for ( temp = 1; temp < e1.length; temp++) {
							
							String[] FieldNametemp1 =e1[temp].split("object has missing required properties ");
							String[] FieldNametemp2  = FieldNametemp1[1].split("]");
							String FieldName= FieldNametemp2[0].replace("\"","");
							String[] Actualvaluetemp = FieldNametemp2[2].split(" missing:");
							String[] Expectedvaluetemp = FieldNametemp2[1].split(" required:");
							String Actualvalue = Actualvaluetemp[0];
							String Expectedvaluet = Expectedvaluetemp[1];
							String Expectedvalue;
							String actualresponse = null;
							if (Expectedvaluet.contains("but: was")) {
								String[] Expectedvaluetemp1=	Expectedvaluet.split("but: was");
								 Expectedvalue=Expectedvaluetemp1[0];
								 actualresponse = Expectedvaluetemp1[1];
							}
							else
							{
								Expectedvalue=Expectedvaluetemp[1];
							}
							 FieldName= FieldName.replace("([", "");
							 FieldName=FieldName.replace("/", "");
							 Expectedvalue=Expectedvalue.replace("[\"", "").replace("\"]", "");
							 Actualvalue =Actualvalue.replace("\"", "");
							System.out.println("Error " + temp + " : " +"fielddName : "+ FieldName +"Actualvalue :" +Actualvalue +"Expectedvalue :" + Expectedvalue );
							if (Actualvalue.trim().equals("")) {
								test.log(Status.FAIL,
										"Schema validation- field missing : <b>'" + FieldName );
							}else {
							test.log(Status.FAIL,
									"Schema Field validation failed for field# <b>'" + FieldName + "'</b><br> Expected field Data Type is <b>'"
											+ Expectedvalue + "'</b>, Actual field Data Type is  <b>'" + Actualvalue
											+ "'</b>.");
							}
							/*
							 * test.log(Status.INFO, "Actual response <b>'" + actualresponse + "'</b>.");
							 */
						}
						
						
					}
					
					else  {
						
						String[] e1 = error.split("error:");			
						int temp ;
						for ( temp = 1; temp < e1.length; temp++) {
							
							String[] FieldNametemp1 =e1[temp].split("instance: ");
							String[] FieldNametemp2  = FieldNametemp1[1].split("}");
							String FieldName= FieldNametemp2[0].replace("{\"pointer\":","");
							String[] Actualvaluetemp = FieldNametemp2[1].split("found:");
							String[] Expectedvaluetemp = null;
							if (Actualvaluetemp[1].contains("maxLength"))
							{
								 Expectedvaluetemp = Actualvaluetemp[1].split("maxLength:");
							}
							else if (Actualvaluetemp[1].contains("expected")) {
								 Expectedvaluetemp = Actualvaluetemp[1].split("expected:");
							}
							else if (Actualvaluetemp[1].contains("minLength")) {
								 Expectedvaluetemp = Actualvaluetemp[1].split("minLength:");
							}
							String Actualvalue = Expectedvaluetemp[0];
							String Expectedvaluet = Expectedvaluetemp[1];
							String Expectedvalue;
							String actualresponse = null;
							if (Expectedvaluet.contains("but: was")) {
								String[] Expectedvaluetemp1=	Expectedvaluet.split("but: was");
								 Expectedvalue=Expectedvaluetemp1[0];
								 actualresponse = Expectedvaluetemp1[1];
							}
							else
							{
								Expectedvalue=Expectedvaluetemp[1];
							}
							 FieldName= FieldName.replace("\"/", "").replace("\"", "");
							 FieldName=FieldName.replace("/", ".");
							 Expectedvalue=Expectedvalue.replace("[\"", "").replace("\"]", "");
							 Actualvalue =Actualvalue.replace("\"", "");
							System.out.println("Error " + temp + " : " +"fielddName : "+ FieldName +"Actualvalue :" +Actualvalue +"Expectedvalue :" + Expectedvalue );
							test.log(Status.FAIL,
									"Schema Field validation failed for field# <b>'" + FieldName + "'</b><br> Expected field Data Type is <b>'"
											+ Expectedvalue + "'</b>, Actual field Data Type is  <b>'" + Actualvalue
											+ "'</b>.");
							/*
							 * test.log(Status.INFO, "Actual response <b>'" + actualresponse + "'</b>.");
							 */
						}
					}
				
				}
			}
}