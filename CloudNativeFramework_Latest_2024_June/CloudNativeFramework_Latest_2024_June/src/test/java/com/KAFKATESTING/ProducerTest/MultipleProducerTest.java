package com.KAFKATESTING.ProducerTest;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import ResuableLibrary.KafkaResuables;
import TestDataExternalization.TestDataManagement;
import baseTestPackage.BaseTest_TestNG;
import no.sysco.testing.kafka.embedded.EmbeddedSingleNodeKafkaCluster;

public class MultipleProducerTest extends BaseTest_TestNG {
	KafkaResuables resuableComponents = new KafkaResuables();
	TestDataManagement testdataManage = new TestDataManagement();
	EmbeddedSingleNodeKafkaCluster CLUSTER;
	KafkaProducer<String, String> producer;
	KafkaProducer<String, String> producer1;
	String topic = null;
	String firstTopicKey, firstTopicValue, secondTopicKey, secondTopicValue, thirdTopicKey, thirdTopicValue;
	Properties Readprop = new Properties();

	@BeforeClass
	public void Init() throws Exception {
		CLUSTER = resuableComponents.startCluster(CLUSTER);
		producer = new KafkaProducer<>(resuableComponents.getProducerStringProperties(CLUSTER));
		producer1 = new KafkaProducer<>(resuableComponents.getProducerStringProperties(CLUSTER));
		Readprop = resuableComponents.getPropValues();
		topic = Readprop.getProperty("ProducerTopic1");
	}

	@AfterClass
	public void Destroy() throws InterruptedException {
		CLUSTER.stop();
		test.log(Status.INFO, "The cluster has been stopped");
	}

	/**
	 * Test method to send and validate message to Kafka Broker with multiple
	 * producer
	 * 
	 * @throws Exception
	 */
	@Test
	public void MultipleProducerWithJSONMessages() throws Exception {
		test = report.createTest("Producer Sending JSON Messages");
		test.log(Status.INFO, "Validation for Kafka Mutiple JSON Messages Starts");
		// Mocking Producer
		JSONMessages();
		// Creating topic
		CLUSTER.createTopic(topic);
		// Mocking producer
		mockProducer();
		producer.close();
		producer1.close();

	}

	public void mockProducer() throws Exception, UnsupportedEncodingException {
		RecordMetadata recordMetadata;
		recordMetadata = resuableComponents.sendMessgaeGetMetadata(topic, firstTopicKey, firstTopicValue, test,
				producer);
		resuableComponents.MetadataInfo(recordMetadata, test);
		recordMetadata = resuableComponents.sendMessgaeGetMetadata(topic, secondTopicKey, secondTopicValue, test,
				producer1);
		resuableComponents.MetadataInfo(recordMetadata, test);
		recordMetadata = resuableComponents.sendMessgaeGetMetadata(topic, thirdTopicKey, thirdTopicValue, test,
				producer);
		resuableComponents.MetadataInfo(recordMetadata, test);
	}

	public void JSONMessages() throws Exception {
		test.log(Status.INFO, " Getting values from external soruce");
		File f = new File(".");
		firstTopicKey = Readprop.getProperty("Topic1Key");
		firstTopicValue = KafkaResuables
				.readFileAsString(f.getCanonicalPath() + "\\src\\main\\resources\\JSON\\Book1.Json");
		secondTopicKey = Readprop.getProperty("Topic2Key");
		secondTopicValue = KafkaResuables
				.readFileAsString(f.getCanonicalPath() + "\\src\\main\\resources\\JSON\\Book2.Json");
		thirdTopicKey = Readprop.getProperty("Topic3Key");
		thirdTopicValue = KafkaResuables
				.readFileAsString(f.getCanonicalPath() + "\\src\\main\\resources\\JSON\\Book3.Json");

	}

}
