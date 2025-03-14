package com.UITests.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

	@FindBy(id = "firstName")
	WebElement firstName;
	
	@FindBy(id = "lastName")
	WebElement lastName;

	
	public HomePage(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}
		
	public WebElement getFirstName() {
		return firstName;
	}
	
	public WebElement getLastName() {
		return lastName;
	}
	
}
