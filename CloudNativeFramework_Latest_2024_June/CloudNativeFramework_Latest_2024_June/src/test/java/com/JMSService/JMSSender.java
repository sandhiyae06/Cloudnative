package com.JMSService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.jms.DeliveryMode;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import ResuableLibrary.JMSReusables;
import baseTestPackage.BaseTest_TestNG;

public class JMSSender extends BaseTest_TestNG {
	JMSReusables resuableComponents = new JMSReusables();
	static InitialContext ctx;
	static Queue queue;
	static QueueConnectionFactory connFactory;
	static QueueConnection queueConn;
	static QueueSession queueSession;

	@Test
	public void sendMessageToJMS() throws Exception {
		test = report.createTest("JMS Message Sender");
		test.log(Status.INFO, "*********************JMS Message Sender - Starts***********************");
		test.log(Status.INFO, "Load Queue Properties");
		Properties env = resuableComponents.loadQueueProperties("tcp://localhost:61616", "MyNewQueue");
		test.log(Status.INFO, "Create Queue Session");
		InitialContext ctx = new InitialContext(env);
		Queue queue = (Queue) ctx.lookup("QueueName");
		QueueConnectionFactory connFactory = (QueueConnectionFactory) ctx.lookup("QueueConnectionFactory");
		QueueConnection queueConn = connFactory.createQueueConnection();
		QueueSession queueSession = queueConn.createQueueSession(false, Session.DUPS_OK_ACKNOWLEDGE);
		test.log(Status.INFO, "Create Queue Sender Object");
		QueueSender queueSender = queueSession.createSender(queue);
		queueSender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		test.log(Status.INFO, "Load Messages to be sent");
		List<String> senderMsgList = loadJSONMessages();
		for (String senderMsg : senderMsgList) {
			TextMessage message = queueSession.createTextMessage(senderMsg);
			queueSender.send(message);
			test.log(Status.INFO, "JMS Message sender validation starts-------------------------");
			test.log(Status.INFO, "JMS Message sender Delivery Mode, Expected value is '1' and Actual value is '"
					+ queueSender.getDeliveryMode() + "'");
			test.log(Status.INFO,
					"JMS Message sender Destination, Expected value is 'queue://MyNewQueue' and Actual value is "
							+ queueSender.getDestination());
			if (resuableComponents.validateSenderMessage(queueSender, "MyNewQueue")) {
				test.log(Status.PASS, "Message sent successfully by JMS Sender");
			} else {
				test.log(Status.FAIL, "Message not sent successfully by JMS Sender");
			}
			test.log(Status.INFO, "JMS Message sender validation ends-------------------------");
		}
		queueConn.close();

	}

	public  List<String> loadJSONMessages() throws Exception {
		List<String> senderMsgList = new ArrayList<String>();
		File f = new File(".");
		String firstTopicValue = resuableComponents
				.readFileAsString(f.getCanonicalPath() + "\\src\\main\\resources\\JSON\\Book1.Json");
		senderMsgList.add(0, firstTopicValue);
		String secondTopicValue = resuableComponents
				.readFileAsString(f.getCanonicalPath() + "\\src\\main\\resources\\JSON\\Book2.Json");
		// senderMsgList.add(1, secondTopicValue);
		String thirdTopicValue = resuableComponents
				.readFileAsString(f.getCanonicalPath() + "\\src\\main\\resources\\JSON\\Book3.Json");
		// senderMsgList.add(2, thirdTopicValue);
		return senderMsgList;
	}

}
