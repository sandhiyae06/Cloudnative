package com.KAFKATESTING.ConsumerTest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringDeserializer;
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
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import no.sysco.testing.kafka.embedded.EmbeddedSingleNodeKafkaCluster;

public class KafkaAvroConsumer extends BaseTest_TestNG {
	KafkaResuables resuableComponents = new KafkaResuables();
	TestDataManagement testdataManage = new TestDataManagement();
	EmbeddedSingleNodeKafkaCluster CLUSTER;
	String topic = null;
//	static List<Row> resultSetRows1;
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

	}

	/**
	 * Test method to validate the avro records received from consumer end
	 * @throws Exception
	 */
	@Test
	public void AvroConsumer() throws Exception {

		RecordMetadata recordMetadata;
		test = report.createTest("Kafka_Avro_Consumer_Test");
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
		// consumer subscribing the topic and validating the record received for the topic
		Consumer<String, Object> consumer = new KafkaConsumer<>(getConsumerProperties(CLUSTER));
		consumer.subscribe(Collections.singletonList(topic));
		validateConsumedRecords(record, consumer);
		consumer.close();
	}

	public void validateConsumedRecords(ProducerRecord<String, Object> record, Consumer<String, Object> consumer) {
		final ArrayList<String> values = new ArrayList<>();
		final ConsumerRecords<String, Object> records = consumer.poll(Duration.ofSeconds(100));

		resuableComponents.consumedRecordValidation(record, values, records, test, AvroRecord1From, AvroRecord1To,
				AvroRecord1Content, AvroRecord1Id);
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
				// *Kafka
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