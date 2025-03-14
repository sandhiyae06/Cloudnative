package baseTestPackage;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.*;
//import com.relevantcodes.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.mongodb.MongoClient;

import ResuableLibrary.JsonUtils;
import TestDataExternalization.TestDataManagement;


//@RunWith(SpringRunner.class)
public class BaseTest_TestNG extends TestDataManagement {
	public static ExtentReports report;
	public static ExtentTest test;
	public static Properties globalProp;
	public static MongoClient mongoClient;
	 		
	@BeforeSuite
	public void startTest() throws Exception {
		File f = new File(".");
		//loadTestDataFile();
		globalProp = loadPropertyFile(f.getCanonicalPath() + "//GlobalProperties.properties");
		mongoClient = new MongoClient("localhost", 27017);
		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_hh_mm_ss");
		String strDate = dateFormat.format(date);
		File reportFile = new File(f.getCanonicalPath() + "\\ExtentReport\\ExtentReportResults1_" + strDate + ".html");
		if (!reportFile.exists()) {
			reportFile.createNewFile();
		}
		//report = new ExtentReports(f.getCanonicalPath() + "\\ExtentReport\\ExtentReportResults1_" + strDate + ".html",
			//	true);
		
		//report = new ExtentReports(f.getCanonicalPath()+"\\ExtentReport\\ExtentReportResults1_"+strDate+".html",true);
	    report = new ExtentReports();
	    ExtentSparkReporter spark=new ExtentSparkReporter(f.getCanonicalPath()+"\\ExtentReport\\ExtentReportResults1_"+strDate+".html");
	    report.attachReporter(spark);
		
		
		
		
	}

	@AfterSuite
	public static void endTest() throws Exception {
		
		mongoClient.close();
		report.flush();
	}	
}