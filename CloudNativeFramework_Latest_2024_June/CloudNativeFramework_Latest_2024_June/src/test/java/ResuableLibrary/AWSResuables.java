package ResuableLibrary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.internal.SkipMd5CheckStrategy;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;


public class AWSResuables {
	
	 static {
	        System.setProperty(SkipMd5CheckStrategy.DISABLE_PUT_OBJECT_MD5_VALIDATION_PROPERTY, "true");
	    }
	public  BasicAWSCredentials loadAWSKeys(Properties globalProp,ExtentTest test) {
		byte[] Responsedecoded = Base64.getMimeDecoder().decode(globalProp.getProperty("secretKey"));
		test.log(Status.INFO, "Decoding the secret key and access key");
		String SECRET_KEY_ENCODED = new String(Responsedecoded);
		byte[] Responsedecoded_1 = Base64.getMimeDecoder().decode(globalProp.getProperty("accessKey"));
		String ACCESS_KEY_ENCODED = new String(Responsedecoded_1);
		BasicAWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY_ENCODED, SECRET_KEY_ENCODED);
		return credentials;
	}
	public AmazonS3 coonectAWSS3Client(BasicAWSCredentials credentials) {
		AmazonS3 s3client = AmazonS3ClientBuilder
				  .standard()
				  .withCredentials(new AWSStaticCredentialsProvider(credentials))
				  .withRegion(Regions.US_EAST_1)
				  .build();
		return s3client;
	}
	public void uploadFiletoS3(String bucketName, AmazonS3 s3client,Properties globalProp) {
		s3client.putObject(bucketName,globalProp.getProperty("fileName"),new File(globalProp.getProperty("XMLFilePath") + "\\"+globalProp.getProperty("fileName")));
	}
	
	
	public void uploadFiletoS3(String bucketName, AmazonS3 s3client,String filePath,String property) {
		System.out.println("path"+filePath + "\\"+property);
		s3client.putObject(bucketName,property,new File(filePath + "\\"+property));
		
	}
	
	public void moveFilebetweenS3(String sourceBucketName, String destinationBucketName,AmazonS3 s3client,Properties globalProp) {
	
		s3client.copyObject(sourceBucketName, globalProp.getProperty("sourcefilename"), destinationBucketName, globalProp.getProperty("destinationfilename"));
		s3client.deleteObject(sourceBucketName, globalProp.getProperty("sourcefilename"));
	}
	
	public void moveFilebetweenS3(String sourceBucketName, String destinationBucketName,AmazonS3 s3client,Properties globalProp,String source,String destinationFile) {
		
		s3client.copyObject(sourceBucketName, source, destinationBucketName,destinationFile);
		s3client.deleteObject(sourceBucketName, source);
	}
	public void deleteFileinS3(String bucketName, AmazonS3 s3client,Properties globalProp) {
		s3client.deleteObject(bucketName,globalProp.getProperty("fileName"));
	}
	public void ValidateUploadedFileinS3(String bucketName, AmazonS3 s3client,Properties globalProp,ExtentTest test) {
		ObjectListing objectListing = s3client.listObjects(bucketName);
		boolean flag=false;
		for(S3ObjectSummary os : objectListing.getObjectSummaries()) {
		    System.out.println(os.getKey());
		    if(os.getKey().equalsIgnoreCase(globalProp.getProperty("fileName"))){
		    	flag=true;
		    	break;
		    }
		}
		if(flag){
			test.log(Status.PASS, "Uploading of file ***"+globalProp.getProperty("fileName")+"*** to AWS S3 bucket ***"+bucketName+"*** is successful");
		}else{
			test.log(Status.FAIL, "Uploading of file ***"+globalProp.getProperty("fileName")+"*** to AWS S3 bucket ***"+bucketName+"*** is not successful");
		}
	}
	
	
	public void ValidateUploadedFileinS3(String bucketName, AmazonS3 s3client,String property,ExtentTest test) {
		ObjectListing objectListing = s3client.listObjects(bucketName);
		boolean flag=false;
		for(S3ObjectSummary os : objectListing.getObjectSummaries()) {
		    System.out.println(os.getKey());
		    if(os.getKey().equalsIgnoreCase(property)){
		    	flag=true;
		    	break;
		    }
		}
		if(flag){
			test.log(Status.PASS, "Uploading of file ***"+property+"*** to AWS S3 bucket ***"+bucketName+"*** is successful");
		}else{
			test.log(Status.FAIL, "Uploading of file ***"+property+"*** to AWS S3 bucket ***"+bucketName+"*** is not successful");
		}
	}
	
	public void ValidateFileDeleteinS3(String bucketName, AmazonS3 s3client,Properties globalProp,ExtentTest test) {
		ObjectListing objectListing = s3client.listObjects(bucketName);
		boolean flag=false;
		for(S3ObjectSummary os : objectListing.getObjectSummaries()) {
		    System.out.println(os.getKey());
		    if(os.getKey().equalsIgnoreCase(globalProp.getProperty("fileName"))){
		    	flag=true;
		    	break;
		    }
		}
		if(flag){
			test.log(Status.FAIL, "Deleting of file ***"+globalProp.getProperty("fileName")+"*** in AWS S3 bucket ***"+bucketName+"*** is not successful");
		}else{
			test.log(Status.PASS, "Deleting of file ***"+globalProp.getProperty("fileName")+"*** in AWS S3 bucket ***"+bucketName+"*** is successful");
		}
	}
	public void ValidateDownloadedFilefromS3(String bucketName, Properties globalProp,ExtentTest test) {
		boolean flag=false;
		File folder = new File(globalProp.getProperty("XMLFilePath"));
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
		    if (file.isFile()) {
		        System.out.println(file.getName());
		        if(file.getName().contains(globalProp.getProperty("fileName"))){
		        	flag=true;
			    	break;
		        }
		    }
		}
		if(flag){
			test.log(Status.PASS, "Downloading of file ***"+globalProp.getProperty("fileName")+"*** from AWS S3 bucket ***"+bucketName+"*** is successful");
		}else{
			test.log(Status.FAIL, "Downloading of file ***"+globalProp.getProperty("fileName")+"*** from AWS S3 bucket ***"+bucketName+"*** is not successful");
		}
	}
	public void downloadFileFromS3(String bucketName, AmazonS3 s3client,Properties globalProp) throws IOException {
		S3Object s3object = s3client.getObject(bucketName, globalProp.getProperty("fileName"));
		S3ObjectInputStream inputStream = s3object.getObjectContent();
		FileUtils.copyInputStreamToFile(inputStream, new File(globalProp.getProperty("XMLFilePath") + "\\"+globalProp.getProperty("fileName")));
	}
	public void downloadFileFromS3(String bucketName, AmazonS3 s3client,String fileName,String property) throws IOException {
		S3Object s3object = s3client.getObject(bucketName, fileName);
		S3ObjectInputStream inputStream = s3object.getObjectContent();
		FileUtils.copyInputStreamToFile(inputStream, new File(property+"\\"+fileName));
	}
	public void writeFileContents(String srcXMLString,Properties globalProp) throws IOException {
		srcXMLString=srcXMLString+"<end>";
		File newTextFile = new File(globalProp.getProperty("XMLFilePath") + "\\AWSS3source.xml");
        FileWriter fw = new FileWriter(newTextFile);
        fw.write(srcXMLString);
        fw.close();
	}
	
	public void prepareForJson(File txtFile, FileWriter fileToWriteTo) {
	    // parser for reading strings
	    //JSONParser parser = new JSONParser();
	    BufferedReader reader;
	    try {
	        reader = new BufferedReader((new FileReader(txtFile)));
	        String line = reader.readLine();

	        // convert that line to an json object
	        while (line != null) {
	            fileToWriteTo.write(line);
	            line = reader.readLine();
	          
	        }
	      
	        fileToWriteTo.close();
	        reader.close();
	    } catch (Exception e){
	        e.getStackTrace();
	    }
	}
	
	

}