package com.JMSService;

import java.util.Properties;

import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import ResuableLibrary.JMSReusables;
import baseTestPackage.BaseTest_TestNG;

public class JMSReceiver extends BaseTest_TestNG {
	JMSReusables resuableComponents = new JMSReusables();
	static InitialContext ctx;
	static Queue queue;
	static QueueConnectionFactory connFactory;
	static QueueConnection queueConn;
	static QueueSession queueSession;

	@Test
	public void receiveMessageFromJMS() throws Exception {
		test = report.createTest("JMS Message Receiver");
		test.log(Status.INFO, "*********************JMS Message Receiver - Starts***********************");
		test.log(Status.INFO, "Load Queue Properties");
		Properties env = resuableComponents.loadQueueProperties("tcp://localhost:61616", "MyNewQueue");
		test.log(Status.INFO, "Create Queue Session");
		InitialContext ctx = new InitialContext(env);
		Queue queue = (Queue) ctx.lookup("QueueName");
		QueueConnectionFactory connFactory = (QueueConnectionFactory) ctx.lookup("QueueConnectionFactory");
		QueueConnection queueConn = connFactory.createQueueConnection();
		QueueSession queueSession = queueConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		test.log(Status.INFO, "Create Queue Receiver Object");
		QueueReceiver queueReceiver = queueSession.createReceiver(queue);
		queueConn.start();
		test.log(Status.INFO, "Start Receiver Connection");
		TextMessage message = (TextMessage) queueReceiver.receive();
		test.log(Status.INFO, "JMS Message Receiver validation starts-------------------------");
		test.log(Status.INFO, "JMS Message Receiver Delivery Mode, Expected value is '1' and Actual value is '"
				+ message.getJMSDeliveryMode()+ "'");
		test.log(Status.INFO,
				"JMS Message Receiver Destination, Expected value is 'queue://MyNewQueue' and Actual value is "
						+ message.getJMSDestination());
		System.out.println("received: " + message.getText());
		test.log(Status.INFO,
				"JMS Message Receiver Actual Messgae received is "+ message.getText());
		if (resuableComponents.validateReceiverMessage(message.getText(),"Java")){
			test.log(Status.PASS, "Received Message Validated successfully");
		} else {
			test.log(Status.FAIL, "Received Message Validated successfully");
		}
		test.log(Status.INFO, "JMS Message Receiver validation ends-------------------------");
		queueConn.close();
	
	}
	
}