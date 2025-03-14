package com.KAFKATESTING.BrokerTest;

import java.io.File;
import java.util.HashMap;
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

public class ProducerBrokerMultiplePartitionTest extends BaseTest_TestNG {
	HashMap<String, Integer> producerpartitionDataMap = new HashMap<String, Integer>();
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
	 * Test method to send and validate multiple messages to Kakfa Broker
	 * @throws Exception
	 */
	@Test
	public void ProducerWithStringMessages() throws Exception {
		RecordMetadata recordMetadata;
		test = report.createTest("Producer Sending JSON Messages");
		test.log(Status.INFO, "Validation for Kafka Mutiple JSON Messages Starts");
		// Mocking Producer
		loadJSONMessages();
		CLUSTER.createTopic(topic, 3, (short) 1);
		//Send first message to Kafka Broker
		recordMetadata = resuableComponents.sendMessgaeGetMetadata(topic,firstTopicKey, firstTopicValue,test,producer);
		// Validating the metadata info
		resuableComponents.MetadataInfo(recordMetadata, test);
		producerpartitionDataMap.put(firstTopicKey, recordMetadata.partition());
		//Send second message to Kafka Broker
		recordMetadata = resuableComponents.sendMessgaeGetMetadata(topic,secondTopicKey, secondTopicValue,test,producer);
		// Validating the metadata info
		resuableComponents.MetadataInfo(recordMetadata, test);
		producerpartitionDataMap.put(secondTopicKey, recordMetadata.partition());
		//Send third message to Kafka Broker
		recordMetadata = resuableComponents.sendMessgaeGetMetadata(topic,thirdTopicKey, thirdTopicValue,test,producer);
		// Validating the metadata info
		resuableComponents.MetadataInfo(recordMetadata, test);
		producerpartitionDataMap.put(thirdTopicKey, recordMetadata.partition());

		producer.close();

	}

	

	public void loadJSONMessages() throws Exception {
		File f = new File(".");
		test.log(Status.INFO, " Getting values from external soruce");
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
