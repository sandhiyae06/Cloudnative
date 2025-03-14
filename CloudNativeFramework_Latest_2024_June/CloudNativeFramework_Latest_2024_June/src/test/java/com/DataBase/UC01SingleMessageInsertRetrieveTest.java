package com.DataBase;
import java.io.File;
import java.io.IOException;
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

public class UC01SingleMessageInsertRetrieveTest extends BaseTest_TestNG {
	KafkaResuables resuableComponents = new KafkaResuables();
	TestDataManagement testdataManage = new TestDataManagement();
	EmbeddedSingleNodeKafkaCluster CLUSTER;
	KafkaProducer<String, String> producer;
	String topic = null;
	String key, firstTopicKey, firstTopicValue, secondTopicKey, secondTopicValue, thirdTopicKey, thirdTopicValue;
	Properties Readprop = new Properties();
	Map<String, Object[]> retrievedData = new TreeMap<String, Object[]>();
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
	 * Test method to receive and validate message from Consumer end
	 * 
	 * @throws Exception
	 */
	@Test
	public void ConsumerWithSingleJsonMessages() throws Exception {
		test = report.createTest("Consumer Validating JSON Messages");
		test.log(Status.INFO, "Validation for Kafka Mutiple JSON Messages Starts");
		// Mocking Producer
		LoadJSONMessages();
		CLUSTER.createTopic(topic);
		RecordMetadata recordMetadata;
		recordMetadata = resuableComponents.sendMessgaeGetMetadata(topic, firstTopicKey, firstTopicValue, test,
				producer);
		resuableComponents.MetadataInfo(recordMetadata, test);
		// Loading Consumer
		final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(
				resuableComponents.getConsumerStringProperties(CLUSTER));
		// Subscribing topic to Consumer
		consumer.subscribe(Collections.singletonList(topic));
		final ArrayList<String> values = new ArrayList<>();
		// Receiving values from broker and validation
		consumerRecordValidation(consumer);
		consumer.close();
		producer.close();

	}

	public void consumerRecordValidation(final KafkaConsumer<String, String> consumer)
			throws IOException {
		 resuableComponents.validateConsumedRecords("consumer1","SingleConsumerSingleMessage",consumer, test,mongoClient);
		 resuableComponents.validateConsumedRecordsAgainstProducerRecords("consumer1","SingleConsumerSingleMessage",topic, firstTopicKey, firstTopicValue, test,mongoClient);
		
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
