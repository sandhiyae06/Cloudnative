package ResuableLibrary;

import static io.restassured.RestAssured.given;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;

public class MicroServiceReusables {
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
		public void validateResponse(String actualResponse, String expectedResponse, ExtentTest test)
				throws UnsupportedEncodingException {
			test.log(Status.INFO, "Expected Response is ------->   " + expectedResponse);
			test.log(Status.INFO, "Actual Response is ------->   " + actualResponse);
			if (actualResponse.replaceAll(" ", "").equalsIgnoreCase(expectedResponse.replaceAll(" ", "")))
				test.log(Status.PASS, "Test Passed");
			else
				test.log(Status.FAIL, "Test Failed");
		}

		// *MS
		public void validateStatusCode(String actualStatusCode, String expectedStatusCode, ExtentTest test)
				throws UnsupportedEncodingException {
			test.log(Status.INFO, "Expected Response is ------->   " + expectedStatusCode);
			test.log(Status.INFO, "Actual Response is ------->   " + actualStatusCode);
			if (actualStatusCode.replaceAll(" ", "").equalsIgnoreCase(expectedStatusCode.replaceAll(" ", "")))
				test.log(Status.PASS, "Test Passed");
			else
				test.log(Status.FAIL, "Test Failed");
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
					.body(Addbook(globalProp.getProperty("BookName"), globalProp.getProperty("BookCode"))).when()
					.post(globalProp.getProperty("EndPoint")).then().extract().response();
		}

		// *MS
		public void validateIndividualFields(Response resp, ExtentTest test, String strFieldName, String strExpectedValue)
				throws Exception {
			JsonPath js = new JsonPath(resp.asString());
			String strActualValue = js.get(strFieldName);
			validateResponse(strActualValue, strExpectedValue, test);
		}
		
}
