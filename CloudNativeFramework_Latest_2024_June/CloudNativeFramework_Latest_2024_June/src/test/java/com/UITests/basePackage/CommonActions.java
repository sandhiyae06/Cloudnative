package com.UITests.basePackage;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.Status;


public class CommonActions {

	public static void click(WebElement element, String stepDesc) {
		try {
			if(waitForElementDisplayed(element)) {
				element.click();
				BaseTest.stepPass(Status.PASS, "Click on "+stepDesc);
			} else {
				BaseTest.stepFail(Status.FAIL, "Click on "+stepDesc);						
			}			
		} catch(Exception ex)
		{			
			BaseTest.stepFail(Status.FAIL, "Click on "+stepDesc + ex.toString());
		}		
	}
	
	public static void type(WebElement element, String value, String stepDesc) {
		try {
			if(waitForElementDisplayed(element)) {
				element.clear();
				element.sendKeys(value);
				BaseTest.stepPass(Status.PASS, "Value '"+value+"' is entered in "+ stepDesc);
			} else {
				BaseTest.stepFail(Status.FAIL, "Value '"+value+"' is entered in "+ stepDesc);						
			}			
		} catch(Exception ex)
		{			
			BaseTest.stepFail(Status.FAIL, "Value '"+value+"' is entered in "+ stepDesc);
		}		
	}
	
	public static boolean waitForElementDisplayed(WebElement element) throws InterruptedException {
		BaseTest.getWebDriver().manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		int timeout = Integer.parseInt(BaseTest.configObject.get("maxElementWaitTime").toString()); 
		int pollingEvery = 5;
		int i=0;
		boolean isElementFound = false;
		do {
			try {
				element.isDisplayed();
				isElementFound = true;
				break;
			} catch (NoSuchElementException ex) {
			} catch (WebDriverException ex) {
				
			}
			Thread.sleep(5000);
			i++;
		} while(i<=(timeout/pollingEvery) && !isElementFound);	
		BaseTest.getWebDriver().manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
		return isElementFound;
	}
	
	public static void waitForElementNotDisplayed(WebElement element) throws InterruptedException {
		BaseTest.getWebDriver().manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		int timeout = Integer.parseInt(BaseTest.configObject.get("maxElementWaitTime").toString()); 
		int pollingEvery = 5;
		int i=0;
		boolean isElementFound = true;
		do {
			try {
				element.isDisplayed();
			} catch (NoSuchElementException ex) {
				isElementFound = false;
				break;
			} catch (WebDriverException ex) {
				
			}
			Thread.sleep(5000);
			i++;
		} while(i<=(timeout/pollingEvery) && isElementFound);	
		BaseTest.getWebDriver().manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
	}
	
	public static void waitForElementEnabled(WebElement element) throws InterruptedException {
		BaseTest.getWebDriver().manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		int timeout = Integer.parseInt(BaseTest.configObject.get("maxElementWaitTime").toString()); 
		int pollingEvery = 5;
		int i=0;
		boolean isElementFound = false;
		do {
			try {
				element.isEnabled();
				isElementFound = true;
				break;
			} catch (NoSuchElementException ex) {
				
			} catch (WebDriverException ex) {
				
			}
			Thread.sleep(5000);
			i++;
		} while(i<=(timeout/pollingEvery) && !isElementFound);	
		BaseTest.getWebDriver().manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
	}
	
	public static void verifyElementPresent(WebElement element) throws InterruptedException {		
		try {
			if(waitForElementDisplayed(element)) {
				element.isDisplayed();
				BaseTest.stepPass(Status.PASS, "Element is displayed");
			} else {
				BaseTest.stepFail(Status.FAIL, "Element is displayed");					
			}			
		} catch(Exception ex)
		{			
			BaseTest.stepFail(Status.FAIL, "Element is displayed");
		}				
	}
	
	public static void verifyElementNotPresent(WebElement element) throws InterruptedException {		
		try {
			if(waitForElementDisplayed(element)) {
				element.isDisplayed();
				BaseTest.stepPass(Status.FAIL, "Element is displayed");
			} else {
				BaseTest.stepFail(Status.PASS, "Element is not displayed");					
			}			
		} catch(Exception ex)
		{			
			BaseTest.stepFail(Status.FAIL, "Element is displayed");
		}			
	}
	
	public static String getFutureDate(int day) {
		String pattern = "E, dd MMM, yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date()); 
		c.add(Calendar.DATE, day);		
		return simpleDateFormat.format(c.getTime());
	}
	
	public static void switchToFrameByName(String frameName, String name) {
		try {
			BaseTest.getWebDriver().switchTo().frame(frameName);
			BaseTest.stepPass(Status.PASS, "Switch to '"+name+"'frame");	
		} catch(Exception ex)
		{			
			BaseTest.stepFail(Status.FAIL, "Switch to '"+name+"'frame");	
		}			
	}
	
	public static void switchToFrameByElement(WebElement element, String name) {
		try {
			BaseTest.getWebDriver().switchTo().frame(element);
			BaseTest.stepPass(Status.PASS, "Switch to '"+name+"'frame");	
		} catch(Exception ex)
		{			
			BaseTest.stepFail(Status.FAIL, "Switch to '"+name+"'frame");	
		}		
	}
}
