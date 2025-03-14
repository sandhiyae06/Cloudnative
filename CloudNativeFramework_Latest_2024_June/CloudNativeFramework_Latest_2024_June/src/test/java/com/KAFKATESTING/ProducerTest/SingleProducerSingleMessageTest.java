package com.KAFKATESTING.ProducerTest;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import ResuableLibrary.KafkaResuables;
import TestDataExternalization.TestDataManagement;
import baseTestPackage.BaseTest_TestNG;
import no.sysco.testing.kafka.embedded.EmbeddedSingleNodeKafkaCluster;

public class SingleProducerSingleMessageTest extends BaseTest_TestNG {
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
	 * Test method to send and validate JSON message to Kafka Broker
	 * @throws Exception
	 */
	@Test
	public void ProducerWithSingleJsonMessages() throws Exception {
		RecordMetadata recordMetadata;
		test = report.createTest("Producer Sending JSON Messages");
		test.log(Status.INFO, "Validation for Kafka Mutiple JSON Messages Starts");
		//Load JSON Message
		loadJSONMessages();
		//Create Topic for Producer
		CLUSTER.createTopic(topic);
		//Send messgae to Kafka Broker and get the Record metadata
		recordMetadata = resuableComponents.sendMessgaeGetMetadata(topic, firstTopicKey, firstTopicValue, test,
				producer);
		//Validate Record Meta Data
		resuableComponents.MetadataInfo(recordMetadata, test);
		producer.close();

	}

	public void loadJSONMessages() throws Exception {
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
