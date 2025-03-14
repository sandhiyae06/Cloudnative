package ResuableLibrary;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Base64;
import java.util.List;
import java.util.Properties;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import java.nio.file.*;
public class AWSMsgQueue  {
	
	public static void Publish(ExtentTest test,ExtentReports report,Properties globalProp) throws Exception {
		byte[] Responsedecoded = Base64.getMimeDecoder().decode(globalProp.getProperty("secretKey"));
		test.log(Status.INFO, "Decoding the secret key and access key");
		String SECRET_KEY_ENCODED = new String(Responsedecoded);

		byte[] Responsedecoded_1 = Base64.getMimeDecoder().decode(globalProp.getProperty("accessKey"));
		String ACCESS_KEY_ENCODED = new String(Responsedecoded_1);
		String ENDPOINT_URL=globalProp.getProperty("connectionStringAWS");
		BasicAWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY_ENCODED, SECRET_KEY_ENCODED);
		test.log(Status.INFO, "Invoking AWS SQS Client");
		AmazonSQSClient sqsClient = (AmazonSQSClient) AmazonSQSClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_2).build();
		test.log(Status.INFO, "Loading the file from which message has to be sent");
		String fileName=globalProp.getProperty("textFile")+"src\\test\\resources\\Files\\Text_File.txt";
		File file = new File(fileName);

		DataInputStream dis = new DataInputStream(new FileInputStream(file.getAbsolutePath()));
		byte[] datainBytes = new byte[dis.available()];
		dis.readFully(datainBytes);
		dis.close();
		String content = new String(datainBytes, 0, datainBytes.length, "UTF-8");
		test.log(Status.PASS, "Messgae content to be sent to AWS SQS is ************"+content+"**************");
		SendMessageResult publishResult = sqsClient.sendMessage(ENDPOINT_URL, content);
		test.log(Status.PASS, "Messgae content sent successfully to AWS Client");
		//test = report.createTest("********************PUBLISH DONE********************");
		
	}
	private static String sanitizePathTraversal(String filename) {
		 Path p = Paths.get(filename);
		 return p.getFileName().toString();
		}
	
	public static void Subscribe(ExtentTest test,ExtentReports report,Properties globalProp) throws Exception {
		//test = report.createTest("Subscribe Messgae from AWS Portal");
		String output = null;
		test.log(Status.INFO, "Decoding the secret key and access key");
		byte[] Responsedecoded = Base64.getMimeDecoder().decode(globalProp.getProperty("secretKey"));
		final String SECRET_KEY_ENCODED = new String(Responsedecoded);

		 byte[] Responsedecoded_1 = Base64.getMimeDecoder().decode(globalProp.getProperty("accessKey"));
		final  String ACCESS_KEY_ENCODED = new String(Responsedecoded_1);
		String ENDPOINT_URL=globalProp.getProperty("connectionStringAWS");
		test.log(Status.INFO, "Invoking AWS SQS Client");
		BasicAWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY_ENCODED, SECRET_KEY_ENCODED);
		AmazonSQSClient sqsClient = (AmazonSQSClient) AmazonSQSClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_2).build();

		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(
				ENDPOINT_URL).withAttributeNames("All")
						.withMessageAttributeNames("All");
		List<com.amazonaws.services.sqs.model.Message> messages = sqsClient.receiveMessage(receiveMessageRequest)
				.getMessages();
		test.log(Status.INFO, "MESSAGE SUBSCRIBTION STARTS ");
		
		for (com.amazonaws.services.sqs.model.Message message : messages) {
			test.log(Status.INFO,"    MessageId:     " + message.getMessageId());
			test.log(Status.INFO, "    ReceiptHandle: " + message.getReceiptHandle());
			test.log(Status.INFO,"    MD5OfBody:     " + message.getMD5OfBody());
			test.log(Status.INFO, " Message Body Content :          " + message.getBody());
			
			output = message.getBody();
			test.log(Status.INFO, "MessageGroupId is " + message.getAttributes().get("MessageGroupId"));
			test.log(Status.INFO, "MessageDeduplicationId is " + message.getAttributes().get("MessageDeduplicationId"));
			
			test.log(Status.INFO, "Invoking Delete Message Request");
			DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest(ENDPOINT_URL, message.getReceiptHandle());

			sqsClient.deleteMessage(deleteMessageRequest);
			test.log(Status.INFO, "Message Deleted from SQS Queue");
		}
		test.log(Status.PASS, "Validation for subscribed messgae starts");
		if (output.contains("Test")) {
			test.log(Status.PASS, "Validation for subscribed messgae is successfull");
		} else {
			test.log(Status.FAIL, "Validation for subscribed messgae is not successfull");
			}
		test.log(Status.PASS, "Validation for subscribed messgae ends");
	}
	
	/////////////////////
	public static void Publish(ExtentTest test,ExtentReports report,Properties globalProp,String strFilePath) throws Exception {
		byte[] Responsedecoded = Base64.getMimeDecoder().decode(globalProp.getProperty("secretKey"));
		test.log(Status.INFO, "Decoding the secret key and access key");
		String SECRET_KEY_ENCODED = new String(Responsedecoded);

		byte[] Responsedecoded_1 = Base64.getMimeDecoder().decode(globalProp.getProperty("accessKey"));
		String ACCESS_KEY_ENCODED = new String(Responsedecoded_1);
		String ENDPOINT_URL=globalProp.getProperty("connectionStringAWS");
		BasicAWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY_ENCODED, SECRET_KEY_ENCODED);
		test.log(Status.INFO, "Invoking AWS SQS Client");
		AmazonSQSClient sqsClient = (AmazonSQSClient) AmazonSQSClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_2).build();
		test.log(Status.INFO, "Loading the file from which message has to be sent");
		File file = new File(strFilePath);

		DataInputStream dis = new DataInputStream(new FileInputStream(file.getAbsolutePath()));
		byte[] datainBytes = new byte[dis.available()];
		dis.readFully(datainBytes);
		dis.close();
		String content = new String(datainBytes, 0, datainBytes.length, "UTF-8");
		test.log(Status.INFO, "Messgae content to be sent to AWS SQS is ************"+content+"**************");
		SendMessageResult publishResult = sqsClient.sendMessage(ENDPOINT_URL, content);
		test.log(Status.INFO, "Messgae content sent successfully to AWS Client");
		//test = report.createTest("********************PUBLISH DONE********************");
		System.out.println("********************PUBLISHING THE CONTENT TO AWS SQS IS SUCCESSFUL********************");

	}

	
	public static void Subscribe(ExtentTest test,ExtentReports report,Properties globalProp,String strValidationTxt) throws Exception {
		//test = report.createTest("Subscribe Messgae from AWS Portal");
		String output = null;
		test.log(Status.INFO, "Decoding the secret key and access key");
		byte[] Responsedecoded = Base64.getMimeDecoder().decode(globalProp.getProperty("secretKey"));
		final String SECRET_KEY_ENCODED = new String(Responsedecoded);

		 byte[] Responsedecoded_1 = Base64.getMimeDecoder().decode(globalProp.getProperty("accessKey"));
		final  String ACCESS_KEY_ENCODED = new String(Responsedecoded_1);
		String ENDPOINT_URL=globalProp.getProperty("connectionStringAWS");
		test.log(Status.INFO, "Invoking AWS SQS Client");
		BasicAWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY_ENCODED, SECRET_KEY_ENCODED);
		AmazonSQSClient sqsClient = (AmazonSQSClient) AmazonSQSClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_2).build();

		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(
				ENDPOINT_URL).withAttributeNames("All")
						.withMessageAttributeNames("All");
		List<com.amazonaws.services.sqs.model.Message> messages = sqsClient.receiveMessage(receiveMessageRequest)
				.getMessages();
		test.log(Status.INFO, "MESSAGE SUBSCRIBTION STARTS ");
		for (com.amazonaws.services.sqs.model.Message message : messages) {
			test.log(Status.INFO, "    MessageId:     " + message.getMessageId());
			test.log(Status.INFO, "    ReceiptHandle: " + message.getReceiptHandle());
			test.log(Status.INFO, "    MD5OfBody:     " + message.getMD5OfBody());
			test.log(Status.INFO, " Message Body Content :          " + message.getBody());
			output = message.getBody();
			test.log(Status.INFO, "MessageGroupId is " + message.getAttributes().get("MessageGroupId"));
			test.log(Status.INFO, "MessageDeduplicationId is " + message.getAttributes().get("MessageDeduplicationId"));
			test.log(Status.INFO, "Invoking Delete Message Request");
			DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest(ENDPOINT_URL, message.getReceiptHandle());

			sqsClient.deleteMessage(deleteMessageRequest);
			test.log(Status.INFO, "Message Deleted from SQS Queue");
		}
		test.log(Status.PASS, "Validation for subscribed messgae starts");
		if (output.contains(strValidationTxt)) {
			test.log(Status.PASS, "Validation for subscribed messgae is successfull");
		} else {
			test.log(Status.FAIL, "Validation for subscribed messgae is not successfull");
		}
		test.log(Status.PASS, "Validation for subscribed messgae ends");
	}

}
