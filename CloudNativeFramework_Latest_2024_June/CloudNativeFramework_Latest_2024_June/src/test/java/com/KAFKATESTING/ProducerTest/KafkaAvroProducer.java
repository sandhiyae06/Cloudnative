package com.KAFKATESTING.ProducerTest;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
//import org.apache.metamodel.data.Row;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.KAFKATESTING.Message;
import com.aventstack.extentreports.Status;

import ResuableLibrary.KafkaResuables;
import TestDataExternalization.TestDataManagement;
import baseTestPackage.BaseTest_TestNG;
import no.sysco.testing.kafka.embedded.EmbeddedSingleNodeKafkaCluster;

public class KafkaAvroProducer extends BaseTest_TestNG {
	KafkaResuables resuableComponents = new KafkaResuables();
	TestDataManagement testdataManage = new TestDataManagement();
	EmbeddedSingleNodeKafkaCluster CLUSTER;
	String topic = null;
	//static List<Row> resultSetRows1;
	String key, AvroRecord1From, AvroRecord1To, AvroRecord1Content, AvroRecord1Id;
	Properties Readprop = new Properties();
	
	@BeforeTest
	public void Init() throws Exception {
		CLUSTER = resuableComponents.startCluster(CLUSTER);
		// Read the properties
		Readprop = resuableComponents.getPropValues();
		topic = Readprop.getProperty("AvroTopic");
		key = Readprop.getProperty("AvroKey");
		AvroRecord1From = Readprop.getProperty("AvroRecord1From");
		AvroRecord1To = Readprop.getProperty("AvroRecord1To");
		AvroRecord1Content = Readprop.getProperty("AvroRecord1Content");
		AvroRecord1Id = Readprop.getProperty("AvroRecord1Id");
	}

	@AfterTest
	public void Destroy() throws InterruptedException {

		CLUSTER.stop();
		 test.log(Status.INFO, "The cluster has been stopped");
	}

	/**
	 * Test method to send the avro record
	 * @throws Exception
	 */
	@Test
	public void avroProducer() throws Exception {

		RecordMetadata recordMetadata;
		test = report.createTest("Producer Sending Avro Schema Messages");
		test.log(Status.INFO, "Validation for Avro Messages Starts");
		// producer sending avro record
		KafkaProducer producer = new KafkaProducer(getProducerProperties(CLUSTER));
		CLUSTER.createTopic(topic);
		ProducerRecord<String, Object> record = mockProducer();
		recordMetadata = (RecordMetadata) producer.send(record).get(3, TimeUnit.SECONDS);
		// Validating the metadata info
		resuableComponents.MetadataInfo(recordMetadata, test);
		producer.flush();
		producer.close();
	}

	public ProducerRecord<String, Object> mockProducer() {
		Message avroRecord1 = resuableComponents.generateProducerAvroRecord(AvroRecord1From, AvroRecord1Id,
				AvroRecord1Content, AvroRecord1To);
		ProducerRecord<String, Object> record = new ProducerRecord<String, Object>(topic, key, avroRecord1);
		test.log(Status.INFO, "The Producer Sending Message on topic " + topic + " and the key is " + key);
		test.log(Status.INFO, "The Producer Sending Message with key " + key + " and the value is " + avroRecord1);
		return record;
	}

	// *Kafka
	public static Properties getProducerProperties(EmbeddedSingleNodeKafkaCluster clusterObj) {

		final Properties props = new Properties();
		props.put("bootstrap.servers", clusterObj.bootstrapServers());
		props.put(ProducerConfig.CLIENT_ID_CONFIG, "AvroProducer");
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, com.KAFKATESTING.ConsumerTest.AvroSerializer.class);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put("schema.registry.url", "http://localhost:8082");
		return props;
	}


}