package com.UITests.basePackage;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.aventstack.extentreports.Status;



public class SoftAssert {

	public static void assertTitle(String title) {
		if (BaseTest.getWebDriver().getTitle().contains(title)) {
			BaseTest.getTest().log(Status.PASS, "'"+title + "' page is displayed");
			System.out.println("'"+title + "' page is displayed");
		} else {
			String base64Screenshot = "data:image/png;base64,"
					+ ((TakesScreenshot) BaseTest.getWebDriver()).getScreenshotAs(OutputType.BASE64);
			BaseTest.getTest().log(Status.FAIL,
					"'"+title + " page is not displayed" + BaseTest.getTest().addScreenCaptureFromBase64String(base64Screenshot));
			System.out.println(title + " page is not displayed");
		}
	}

	public static void assertTrue(boolean status, String desc) {
		if(status) {
			BaseTest.getTest().log(Status.PASS, desc);
		} else {
			String base64Screenshot = "data:image/png;base64,"
					+ ((TakesScreenshot) BaseTest.getWebDriver()).getScreenshotAs(OutputType.BASE64);
			BaseTest.getTest().log(Status.FAIL,
					desc + BaseTest.getTest().addScreenCaptureFromBase64String(base64Screenshot));
		}
		
	}
	
	public static void assertFalse(boolean status, String desc) {
		if(!status) {
			BaseTest.getTest().log(Status.PASS, desc);
		} else {
			String base64Screenshot = "data:image/png;base64,"
					+ ((TakesScreenshot) BaseTest.getWebDriver()).getScreenshotAs(OutputType.BASE64);
			BaseTest.getTest().log(Status.FAIL,
					desc + BaseTest.getTest().addScreenCaptureFromBase64String(base64Screenshot));
		}
		
	}

	public static void assertEquals(String actualText, String expectedText, String desc) {
		if(actualText.equals(expectedText)) {
			BaseTest.getTest().log(Status.PASS, desc +"<br>Actual Text: "+actualText +"<br>Expected Text: "+expectedText);
		} else {
			String base64Screenshot = "data:image/png;base64,"
					+ ((TakesScreenshot) BaseTest.getWebDriver()).getScreenshotAs(OutputType.BASE64);
			BaseTest.getTest().log(Status.FAIL,
					desc +"<br>Actual Text: "+actualText +"<br>Expected Text: "+expectedText + BaseTest.getTest().addScreenCaptureFromBase64String(base64Screenshot));
		}
		
	}
	
	public static void assertContains(String actualText, String expectedText, String desc) {
		if(actualText.contains(expectedText)) {
			BaseTest.getTest().log(Status.PASS, desc +"<br>Actual Text: "+actualText +"<br>Expected Text: "+expectedText);
		} else {
			String base64Screenshot = "data:image/png;base64,"
					+ ((TakesScreenshot) BaseTest.getWebDriver()).getScreenshotAs(OutputType.BASE64);
			BaseTest.getTest().log(Status.FAIL,
					desc +"<br>Actual Text: "+actualText +"<br>Expected Text: "+expectedText + BaseTest.getTest().addScreenCaptureFromBase64String(base64Screenshot));
		}
		
	}


}
