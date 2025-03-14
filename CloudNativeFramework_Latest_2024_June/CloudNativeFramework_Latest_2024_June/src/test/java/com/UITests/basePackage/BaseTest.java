package com.UITests.basePackage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.mongodb.MongoClient;



public class BaseTest extends CommonActions {
	public static ExtentTest test;
	//static ResourceBundle configObject = null;;
	public static Properties configObject;
	protected static WebDriver driver = null;
	static ExtentReports extent = null;
	static String userDir = null;
	static String testDataDir = null;
	public static Map<String, String> testNgParameters = null;
	public static ExtentReports report;
	public static Properties globalProp;
	public static MongoClient mongoClient;

	@BeforeSuite(alwaysRun = true)
	public static void getBrowser(ITestContext context) throws Exception {
		testNgParameters = context.getCurrentXmlTest().getAllParameters();
		userDir = System.getProperty("user.dir").replaceAll("\\\\", "/");
		System.out.println(userDir);
		System.out.println("Executing Before Suite");
		File f = new File(".");
		configObject = loadPropertyFile(f.getCanonicalPath() + "//uiconfig.properties");
		//configObject = ResourceBundle.getBundle("uiconfig");
		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_hh_mm_ss");
		String strDate = dateFormat.format(date);
		
		File reportFile = new File(f.getCanonicalPath() + "\\ExtentReport\\UI_Automation_Results_" + strDate + ".html");
		if (!reportFile.exists()) {
			reportFile.createNewFile();
		}
		
		extent = new ExtentReports();
		    ExtentSparkReporter spark=new ExtentSparkReporter(f.getCanonicalPath()+"\\ExtentReport\\ExtentReportResults1_"+strDate+".html");
		    extent.attachReporter(spark);
	}

	@BeforeMethod(alwaysRun = true)
	public static WebDriver launchBrowser(Method  method) {
		test = extent.createTest(method.getName());
		test.log(Status.INFO, method.getName() + " script execution is started");
		System.out.println("Executing Before Method");
		File f1 = new File(".");
		try {
			if (configObject.get("browser").equals("firefox")) {
			//	WebDriverManager.firefoxdriver().setup();
				driver = new FirefoxDriver();
				test.log(Status.PASS, "Firefox browser launched");
			} else if (configObject.get("browser").equals("chrome")) {
				//WebDriverManager.chromedriver().setup();				
				 driver =new ChromeDriver();
				//System.setProperty("webdriver.chrome.driver",  f1.getCanonicalPath() + "\\lib\\chromedriver.exe");
				//driver = new ChromeDriver();
				test.log(Status.PASS, "Chrome browser launched");
			}
		} catch(Exception e) {
			System.out.println("Failed to lauch browser  "+ e.getMessage());
			test.log(Status.FAIL, "Failed to lauch browser");
		}
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		ClearBrowserCache();
		launchApplication();
		return driver;
	}

	@AfterMethod(alwaysRun = true)
	public static void closeBrowser() {		
		System.out.println("Executing After Method");
		driver.quit();
		//test.log(Status.PASS, "Test case execution completed");
		//extent.endTest(test);
		//extent.flush();
	}
	
	public static void launchApplication() {
		try {
			driver.get(configObject.get("url").toString());
			Thread.sleep(3000);
			test.log(Status.PASS, "Application is opened in browser");			
		} catch (Exception e)
		{
			test.log(Status.FAIL, "Failed to open Application in browser");
			e.printStackTrace();
		}
	}
	
	@AfterSuite(alwaysRun = true)
	public static void generateReport() {
		extent.flush();
	}
	
	public static ExtentTest getTest() {
		return test;
	}
	
	public static void stepPass(Status pass, String description) {
		test.log(pass, description);
	}
	
	public static void stepFail(Status fail, String description) {
		test.log(fail, description +test.addScreenCaptureFromBase64String(getScreenshot()));
		driver.quit();
	}
	
	public static WebDriver getWebDriver() {
		return driver;
	}
	
	public static String getScreenshot() {
		return "data:image/png;base64,"
				+ ((TakesScreenshot) BaseTest.getWebDriver()).getScreenshotAs(OutputType.BASE64);
	}

	public static void ClearBrowserCache() {
		driver.manage().deleteAllCookies(); 
	}
	public static Properties loadPropertyFile(String strFilePath) throws Exception {
		FileReader reader = new FileReader(strFilePath);
		Properties prop = new Properties();
		prop.load(reader);
		return prop;
	}
}
