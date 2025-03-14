package com.KAFKATESTING.ProducerTest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import ResuableLibrary.KafkaResuables;
import TestDataExternalization.TestDataManagement;
import baseTestPackage.BaseTest_TestNG;
import no.sysco.testing.kafka.embedded.EmbeddedSingleNodeKafkaCluster;

public class SingleProducerMultipleMessageTest extends BaseTest_TestNG {
	KafkaResuables resuableComponents = new KafkaResuables();
	TestDataManagement testdataManage = new TestDataManagement();
	EmbeddedSingleNodeKafkaCluster CLUSTER;
	KafkaProducer<String, String> producer;
	String topic = null;
	String key, firstTopicKey, firstTopicValue, secondTopicKey, secondTopicValue, thirdTopicKey, thirdTopicValue;
	Properties Readprop = new Properties();


	@BeforeTest
	public void Init() throws Exception {
		CLUSTER = resuableComponents.startCluster(CLUSTER);
		producer = new KafkaProducer<>(resuableComponents.getProducerStringProperties(CLUSTER));
		Readprop = resuableComponents.getPropValues();
		topic = Readprop.getProperty("ProducerTopic1");
	}

	@AfterTest
	public void Destroy() throws InterruptedException {

		CLUSTER.stop();
		test.log(Status.INFO, "The cluster has been stopped");
	}

	/**
	 * Test method to send and validate  message to Kafka Broker
	 * @throws Exception
	 */
	@Test
	public void ProducerWithMultipleMessages()
			throws Exception {
			
		test = report.createTest("Producer Sending  Multiple Messages");
		test.log(Status.INFO, "Validation for Kafka Mutiple  Messages Starts");
		StringMessages();
		//Creating topic
		CLUSTER.createTopic(topic);
		// Mocking Producer
		mockProducer();
		producer.close();

	}

	
	public void mockProducer() throws Exception, UnsupportedEncodingException {
		RecordMetadata recordMetadata;
		recordMetadata = resuableComponents.sendMessgaeGetMetadata(topic, firstTopicKey, firstTopicValue, test,
				producer);
		resuableComponents.MetadataInfo(recordMetadata, test);
		recordMetadata = resuableComponents.sendMessgaeGetMetadata(topic, secondTopicKey, secondTopicValue, test,
				producer);
		resuableComponents.MetadataInfo(recordMetadata, test);
		recordMetadata = resuableComponents.sendMessgaeGetMetadata(topic, thirdTopicKey, thirdTopicValue, test,
				producer);
		resuableComponents.MetadataInfo(recordMetadata, test);
	}
	public void StringMessages() throws Exception, Exception {
		File f=new File(".");
		test.log(Status.INFO, " Getting values from external soruce");
		firstTopicKey = Readprop.getProperty("Topic1Key");
		firstTopicValue = KafkaResuables.readFileAsString(
				f.getCanonicalPath()+"\\src\\main\\resources\\JSON\\Book1.Json");
		secondTopicKey = Readprop.getProperty("Topic2Key");
		secondTopicValue = Readprop.getProperty("Topic2Value");
		thirdTopicKey = Readprop.getProperty("Topic3Key");
		thirdTopicValue = KafkaResuables.readFileAsString(
				f.getCanonicalPath()+"\\src\\main\\resources\\JSON\\Book3.Json");


	}

	

	
}
