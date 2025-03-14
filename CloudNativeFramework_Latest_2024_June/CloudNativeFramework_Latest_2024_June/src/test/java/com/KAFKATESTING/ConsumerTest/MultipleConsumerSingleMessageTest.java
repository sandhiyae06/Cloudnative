package com.KAFKATESTING.ConsumerTest;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import org.apache.kafka.clients.consumer.KafkaConsumer;
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

public class MultipleConsumerSingleMessageTest extends BaseTest_TestNG {
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
	 * Test method to receive and validate message from Consumer end by multiple
	 * consumers
	 * 
	 * @throws Exception
	 */
	@Test
	public void MultipleConsumerMessages() throws Exception {
		test = report.createTest("Producer Sending JSON Messages");
		test.log(Status.INFO, "Validation for Kafka Mutiple JSON Messages Starts");
		LoadJSONMessages();
		// Create topic
		CLUSTER.createTopic(topic);
		// Mocking Producer
		mockProducer();
		// Loading Consumer1
		final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(
				resuableComponents.getConsumerStringProperties(CLUSTER));
		consumer.subscribe(Collections.singletonList(topic));
		
		// Loading Consumer2
		final KafkaConsumer<String, String> consumer1 = new KafkaConsumer<>(
				resuableComponents.getConsumerStringProperties1(CLUSTER));
		consumer1.subscribe(Collections.singletonList(topic));
		// Validating the consumed Records of consumer1
		final ArrayList<String> values = new ArrayList<>();
		resuableComponents.validateConsumedRecords("consumer1","MultipleConsumerSingleMessage",consumer,  test,mongoClient);
		resuableComponents.validateConsumedRecordsAgainstProducerRecords("consumer1","MultipleConsumerSingleMessage",topic, firstTopicKey, firstTopicValue, test,mongoClient);
		values.clear();
		// Validating the consumed Records of consumer2
		resuableComponents.validateConsumedRecords("consumer2","MultipleConsumerSingleMessage",consumer1,  test,mongoClient);
		consumer.close();
		consumer1.close();
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

	public void LoadJSONMessages() throws Exception {
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
