package com.UITests.tests;

import org.testng.annotations.Test;

import com.UITests.pageObjects.HomePage;

public class ToolsQaSampleTest extends com.UITests.basePackage.BaseTest {
	
	@Test(groups = {"Sanity", "Regression"})
	public static void OrderegPizzaTest() throws InterruptedException {		
		HomePage homePage = new HomePage(driver);
		waitForElementDisplayed(homePage.getFirstName());
		type(homePage.getFirstName(), "Gothandapani", "First Name");
		type(homePage.getLastName(), "Muthuvel", "Last Name");
		
	}
}
