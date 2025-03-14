package com.KAFKATESTING.ConsumerTest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.RecordMetadata;
//import org.apache.metamodel.data.Row;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import ResuableLibrary.KafkaResuables;
import TestDataExternalization.TestDataManagement;
import baseTestPackage.BaseTest_TestNG;
import no.sysco.testing.kafka.embedded.EmbeddedSingleNodeKafkaCluster;

public class SingleCosumerMutipleMessageTest extends BaseTest_TestNG {
	KafkaResuables resuableComponents = new KafkaResuables();
	TestDataManagement testdataManage = new TestDataManagement();
	EmbeddedSingleNodeKafkaCluster CLUSTER;
	KafkaProducer<String, String> producer;
	String topic = null;
	String key, firstTopicKey, firstTopicValue, secondTopicKey, secondTopicValue, thirdTopicKey, thirdTopicValue;
	Properties Readprop = new Properties();
	Map<String, Object[]> retrievedData = new TreeMap<String, Object[]>();
//	static List<Row> recordSet;

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
	 * Test method to receive and validate multiple messages from Consumer end 
	 * @throws Exception
	 */
	@Test
	public void ConsumerWithMultipleMessages() throws Exception {
		test = report.createTest("Producer Sending  Multiple Messages");
		test.log(Status.INFO, "Validation for Kafka Mutiple  Messages Starts");
		LoadStringMessages();
		// Creating topic
		CLUSTER.createTopic(topic);
		// Producer Sending data to kafka broker
		mockProducer();
		// Loading Consumer
		final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(
				resuableComponents.getConsumerStringProperties(CLUSTER));
		// Subscribing topic to Consumer
		consumer.subscribe(Collections.singletonList(topic));
		final ArrayList<String> values = new ArrayList<>();
		// Receiving values from broker and validation
		consumerRecordValidation(consumer);
		producer.close();
		consumer.close();
	}

	public void consumerRecordValidation(final KafkaConsumer<String, String> consumer)
			throws IOException {
		 resuableComponents.validateConsumedRecords("consumer1","SingleConsumerMultipleMessage",consumer, test,mongoClient);
		 resuableComponents.validateConsumedRecordsAgainstProducerRecords("consumer1","SingleConsumerMultipleMessage",topic, firstTopicKey, firstTopicValue, test,mongoClient);
		
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

	public void LoadStringMessages() throws Exception, Exception {
		File f = new File(".");
		test.log(Status.INFO, " Getting values from external soruce");
		firstTopicKey = Readprop.getProperty("Topic1Key");
		firstTopicValue = KafkaResuables
				.readFileAsString(f.getCanonicalPath() + "\\src\\main\\resources\\JSON\\Book1.Json");
		secondTopicKey = Readprop.getProperty("Topic2Key");
		secondTopicValue = Readprop.getProperty("Topic2Value");
		thirdTopicKey = Readprop.getProperty("Topic3Key");
		thirdTopicValue = KafkaResuables
				.readFileAsString(f.getCanonicalPath() + "\\src\\main\\resources\\JSON\\Book3.Json");

	}

}
