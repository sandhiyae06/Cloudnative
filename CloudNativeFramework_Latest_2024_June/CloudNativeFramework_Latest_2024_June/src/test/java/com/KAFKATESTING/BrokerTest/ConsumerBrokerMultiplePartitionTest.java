package com.KAFKATESTING.BrokerTest;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import ResuableLibrary.KafkaResuables;
import TestDataExternalization.TestDataManagement;
import baseTestPackage.BaseTest_TestNG;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import no.sysco.testing.kafka.embedded.EmbeddedSingleNodeKafkaCluster;

public class ConsumerBrokerMultiplePartitionTest extends BaseTest_TestNG {
	HashMap<String, Integer> producerpartitionDataMap = new HashMap<String, Integer>();
	HashMap<String, Integer> consumerpartitionDataMap = new HashMap<String, Integer>();
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
		producer.close();
		CLUSTER.stop();
		test.log(Status.INFO, "The cluster has been stopped");
	}

	/**
	 * Test method to receive and validate multiple messages from Kakfa Broker
	 * @throws Exception
	 */
	@Test
	public void ConsumerWithStringMessages() throws Exception {
		test = report.createTest("Producer Sending JSON Messages");
		test.log(Status.INFO, "Validation for Kafka Mutiple JSON Messages Starts");
		// Mocking Producer
		producerMock();
		// Loading Consumer
		final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(
				getConsumerProperties(CLUSTER));
		// Subscribing topic to Consumer
		consumer.subscribe(Collections.singletonList(topic));
		// Receiving values from broker and validating
		HashMap<String, Integer> consumerpartitionDataMap = resuableComponents.retrievePartitionData(consumer, test);
		resuableComponents.comparePartition(consumerpartitionDataMap, producerpartitionDataMap, test);
		consumer.close();
	}

	public void producerMock() throws Exception, UnsupportedEncodingException {
		RecordMetadata recordMetadata;
		loadJSONMessages();
		CLUSTER.createTopic(topic, 3, (short) 1);
		recordMetadata = resuableComponents.sendMessgaeGetMetadata(topic, firstTopicKey, firstTopicValue, test,
				producer);
		resuableComponents.MetadataInfo(recordMetadata, test);
		producerpartitionDataMap.put(firstTopicKey, recordMetadata.partition());
		recordMetadata = resuableComponents.sendMessgaeGetMetadata(topic, secondTopicKey, secondTopicValue, test,
				producer);
		resuableComponents.MetadataInfo(recordMetadata, test);
		producerpartitionDataMap.put(secondTopicKey, recordMetadata.partition());
		recordMetadata = resuableComponents.sendMessgaeGetMetadata(topic, thirdTopicKey, thirdTopicValue, test,
				producer);
		resuableComponents.MetadataInfo(recordMetadata, test);
		producerpartitionDataMap.put(thirdTopicKey, recordMetadata.partition());
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
	public static Properties getConsumerProperties(EmbeddedSingleNodeKafkaCluster clusterObj) {

		final Properties props1 = new Properties();

		props1.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, clusterObj.bootstrapServers());
		props1.put(ConsumerConfig.GROUP_ID_CONFIG, "group0");
		props1.put(ConsumerConfig.CLIENT_ID_CONFIG, "client0");
		props1.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props1.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				com.KAFKATESTING.ConsumerTest.AvroDeserializer.class.getName());
		props1.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
		props1.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		props1.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);

		return props1;
	}
}
