package baseTestPackage;
import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import TestDataExternalization.TestDataManagement;

//@RunWith(SpringRunner.class)
public class BaseTest extends TestDataManagement{
	public static ExtentReports report;
	public static ExtentTest test;
	public static Properties globalProp;
	@BeforeClass
	public  static void startTest() throws Exception
	{
		File f=new File(".");
		//loadTestDataFile();
		globalProp = loadPropertyFile(f.getCanonicalPath() + "//GlobalProperties.properties");
		 Date date = Calendar.getInstance().getTime();  
	     DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_hh_mm_ss");  
	     String strDate = dateFormat.format(date); 
	    	File reportFile=new File(f.getCanonicalPath()+"\\ExtentReport\\ExtentReportResults1_"+strDate+".html");
	        if(!reportFile.exists()){
	       	 reportFile.createNewFile();
	        }
	   // report = new ExtentReports(f.getCanonicalPath()+"\\ExtentReport\\ExtentReportResults1_"+strDate+".html",true);
	        report = new ExtentReports();
			 ExtentSparkReporter spark=new ExtentSparkReporter(f.getCanonicalPath()+"\\ExtentReport\\ExtentReportResults1_"+strDate+".html");
			 report.attachReporter(spark);
	}
	
	@AfterClass
	public static void endTest() throws Exception
	{
	//report.endTest(test);
	report.flush();
	}
	
	public static Properties loadPropertyFile(String strFilePath) throws Exception {
		FileReader reader = new FileReader(strFilePath);

		Properties prop = new Properties();
		prop.load(reader);
		return prop;
	}
}