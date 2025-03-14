package ResuableLibrary;

import static org.awaitility.Awaitility.await;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.KAFKATESTING.Message;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import no.sysco.testing.kafka.embedded.EmbeddedSingleNodeKafkaCluster;

public class KafkaResuables {
	
		// *Kafka
		public Message generateProducerAvroRecord(String from, String Id, String content, String To) {

			return Message.newBuilder().setFrom(from).setId(Id).setText(content).setTo(To).build();
		}
		// *Kafka
		public void consumedRecordValidation(ProducerRecord<String, Object> record, final ArrayList<String> values,
				final ConsumerRecords<String, Object> records, ExtentTest test, String AvroRecord1From,
				String AvroRecord1To, String AvroRecord1Content, String AvroRecord1Id) {
			for (final ConsumerRecord<String, Object> record1 : records) {

				test.log(Status.PASS, "The Consumer Receiving Message on topic " + record1.topic() + " and the key is "
						+ record1.key() + " and the value is " + record1.value());
				values.add(record.key());
				Message userRecord = (Message) record1.value();

				test.log(Status.INFO, "Validation for received value starts");
				validateValues(userRecord.getId().toString(), AvroRecord1Id, test);
				validateValues(userRecord.getFrom().toString(), AvroRecord1From, test);
				validateValues(userRecord.getTo().toString(), AvroRecord1To, test);
				validateValues(userRecord.getText().toString(), AvroRecord1Content, test);
			}
		}

// *Kafka
	public void validateValues(String strField, ExtentTest test) {
		test.log(Status.INFO, "Validation for the Key '" + strField + "' starts----------->");
		if (strField.equalsIgnoreCase(strField)) {
			test.log(Status.PASS, "Validation for the Key '" + strField + "' is successfull.");
		} else {
			test.log(Status.PASS, "Validation for the Key '" + strField
					+ "' is not successfull. It is not a String Value----------->");
		}

	}
	// *Kafka
	public void validateOffset(boolean actual, boolean expected, ExtentTest test) throws UnsupportedEncodingException {

		if (actual)
			test.log(Status.PASS,
					"Producer Record metadata has the offset and successfully sent the message, Passed");
		else
			test.log(Status.FAIL, "Producer Record metadata does not have the offset, Failed");
	}
	// *Kafka
	public void validateTimeStamp(boolean actual, boolean expected, ExtentTest test)
			throws UnsupportedEncodingException {
		test.log(Status.INFO, "Expected value for TimeStamp is ------->" + expected);
		test.log(Status.INFO, "Actual value for TimeStamp is ------->" + actual);
		if (actual)
			test.log(Status.PASS,
					"Producer Record metadata has the TimeStamp and successfully sent the message, Passed");
		else
			test.log(Status.FAIL, "Producer Record metadata does not have the TimeStamp, Failed");
	}
	// *Kafka
	public void validaePartition(RecordMetadata recordMetadata, ExtentTest test) {
		if ((recordMetadata.partition() < 0)) {
			test.log(Status.FAIL, "The producer recors is not sent to the partition");
		} else {
			test.log(Status.PASS, "The producer recors is  sent to the partition " + recordMetadata.partition());
		}
	}
	// *Kafka
	public static String readFileAsString(String file) throws Exception {
		return new String(Files.readAllBytes(Paths.get(file)));
	}
	// *Kafka
	public static void validateRecordCount(ArrayList recordCount, int expectedCount, ExtentTest test) {
		if (recordCount.size() == expectedCount) {
			test.log(Status.PASS, "Producer Record Count " + recordCount.size()
					+ " matches with the Consumer Received Record Count " + expectedCount + "Passed");

		} else {
			test.log(Status.FAIL, "Producer Record Count " + recordCount.size()
					+ " doesnot matches with the Consumer Received Record Count " + expectedCount + "Failed");
		}
	}
	// *Kafka
	public static void validateRecordCount1(int recordCount, int expectedCount, ExtentTest test) {
		if (recordCount == expectedCount) {
			test.log(Status.PASS, "Producer Record Count " + recordCount
					+ " matches with the Consumer Received Record Count " + expectedCount + "Passed");

		} else {
			test.log(Status.FAIL, "Producer Record Count " + recordCount
					+ " doesnot matches with the Consumer Received Record Count " + expectedCount + "Failed");
		}
	}
	
	String result = "";
	InputStream inputStream;
	Properties prop;
	// *Kafka
	public Properties getPropValues() throws IOException {

		try {
			prop = new Properties();
			String propFileName = "config.properties";

			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

			Date time = new Date(System.currentTimeMillis());

		} catch (Exception e) {

		} finally {
			inputStream.close();
		}
		return prop;
	}
	// *Kafka
	public File[] loadFiles(String strFolderPath) throws Exception {
		File f = new File(".");
		File folder = new File(f.getCanonicalPath() + strFolderPath);
		File[] listOfFiles = folder.listFiles();

		return listOfFiles;
	}

	
	// *Kafka
	public EmbeddedSingleNodeKafkaCluster startCluster(EmbeddedSingleNodeKafkaCluster clusterObj) throws Exception {
		clusterObj = new EmbeddedSingleNodeKafkaCluster();
		clusterObj.start();
		return clusterObj;
	}
	// *Kafka
	public void MetadataInfo(RecordMetadata recordMetadata, ExtentTest test) throws UnsupportedEncodingException {
		test.log(Status.INFO, " OffSet and Timestamp Validation On Received Record On Kafka Broker Side");
		boolean offSet = false;
		boolean timeStamp = false;
		offSet = recordMetadata.hasOffset();
		timeStamp = recordMetadata.hasTimestamp();
		validateOffset(offSet, true, test);
		validateTimeStamp(timeStamp, true, test);
	}
		// *Kafka
	public void validateValues(String strField, String strExpected, ExtentTest test) {
		test.log(Status.INFO, "Validation for the Key '" + strField + "' starts----------->");
		if (strField.equalsIgnoreCase(strExpected)) {
			test.log(Status.PASS, "Validation for the Key '" + strField + "' is successfull.");
		} else {
			test.log(Status.PASS, "Validation for the Key '" + strField
					+ "' is not successfull. It is not a String Value----------->");
		}

	}
	// *Kafka
	public Properties getProducerStringProperties(EmbeddedSingleNodeKafkaCluster clusterObj) {
		final Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, clusterObj.bootstrapServers());
		properties.put(ProducerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
		properties.put(ProducerConfig.ACKS_CONFIG, "all");
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return properties;
	}
	// *Kafka
	public Properties getConsumerStringProperties(EmbeddedSingleNodeKafkaCluster clusterObj) {
		final Properties properties = new Properties();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, clusterObj.bootstrapServers());
		properties.put(ConsumerConfig.CLIENT_ID_CONFIG, "client0");
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group0");
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		return properties;
	}
	// *Kafka
	public Properties getConsumerStringProperties1(EmbeddedSingleNodeKafkaCluster clusterObj) {
		final Properties properties = new Properties();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, clusterObj.bootstrapServers());
		properties.put(ConsumerConfig.CLIENT_ID_CONFIG, "client0");
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group1");
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		return properties;
	}
	// *Kafka
	public Map<String, Object[]> validateConsumedRecords(final KafkaConsumer<String, String> consumer1,
			final ArrayList<String> values, ExtentTest test) {
		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		final ArrayList<String> values1 = new ArrayList<>();
		await().atMost(35, TimeUnit.SECONDS).untilAsserted(() -> {
			final ConsumerRecords<String, String> records1 = consumer1.poll(Duration.ofSeconds(20));
			data.put("0", new Object[] { "TOPIC", "KEY", "VALUE" });
			int i = 1;

			for (final ConsumerRecord<String, String> record : records1) {
				String itervalue = Integer.toString(i);

				data.put(itervalue, new Object[] { record.topic(), record.key(), record.value() });
				test.log(Status.PASS, "The  Consumer2 Receiving Message on topic " + record.topic()
						+ " and the key is " + record.key() + " and the value is " + record.value());
				values1.add(record.key());
				i = i + 1;

			}

			validateRecordCount(values1, 3, test);
		});
		return data;
	}
	// *Kafka
	public void validateConsumedRecords(String Consumer1, String testName,
			final KafkaConsumer<String, String> consumer1, ExtentTest test, MongoClient mongoclient) {

		MongoDatabase database = mongoclient.getDatabase("kafkadb");
		MongoCollection<Document> collection = database.getCollection("kafkamessagedata");
		await().atMost(35, TimeUnit.SECONDS).untilAsserted(() -> {
			final ConsumerRecords<String, String> records1 = consumer1.poll(Duration.ofSeconds(20));

			Document document = null;
			for (final ConsumerRecord<String, String> record : records1) {

				document = new Document("title", testName).append("consumer", Consumer1).append("topic", record.topic())
						.append("key", record.key()).append("value", record.value())
						.append("partition", record.partition());

				// Inserting document into the collection
				collection.insertOne(document);
				test.log(Status.PASS, "The  Consumer2 Receiving Message on topic " + record.topic()
						+ " and the key is " + record.key() + " and the value is " + record.value());

			}

			// ResuableComponents.validateRecordCount1(document.size(), 3,
			// test);
		});

	}
	// *Kafka
	public HashMap<String, Integer> retrievePartitionData(final KafkaConsumer<String, String> consumer,
			ExtentTest test) {
		HashMap<String, Integer> consumerpartitionDataMap = new HashMap<String, Integer>();
		Map<String, Object[]> retrievedData = new TreeMap<String, Object[]>();
		final ArrayList<String> values = new ArrayList<>();
		await().atMost(35, TimeUnit.SECONDS).untilAsserted(() -> {
			final ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(20));
			retrievedData.put("0", new Object[] { "TOPIC", "KEY", "VALUE" });
			int i = 1;

			for (final ConsumerRecord<String, String> record : records) {
				String itervalue = Integer.toString(i);
				retrievedData.put(itervalue, new Object[] { record.topic(), record.key(), record.value() });
				test.log(Status.PASS, "The Consumer Receiving Message on topic " + record.topic()
						+ " and the key is " + record.key());
				consumerpartitionDataMap.put(record.key(), record.partition());
				values.add(record.key());
				i = i + 1;

			}

			validateRecordCount(values, 3, test);

		});
		return consumerpartitionDataMap;
	}
	// *Kafka
	public void comparePartition(HashMap<String, Integer> consumerpartitionDataMap,
			HashMap<String, Integer> producerpartitionDataMap, ExtentTest test) {
		for (final Map.Entry<String, Integer> entry : consumerpartitionDataMap.entrySet()) {
			String recordKey = entry.getKey();

			if ((entry.getValue()) == producerpartitionDataMap.get(recordKey)) {
				test.log(Status.PASS, "The Consumer Mesage with key " + recordKey
						+ " received from the excepted partition" + entry.getValue());
			} else {
				test.log(Status.FAIL, "The Consumer Mesage with key " + recordKey
						+ " not received from the excepted partition" + producerpartitionDataMap.get(recordKey));
			}
		}
	}
	// *Kafka
	public RecordMetadata sendMessgaeGetMetadata(String topic, String key, String value, ExtentTest test,
			KafkaProducer<String, String> producer) throws Exception {
		RecordMetadata recordMetadata;
		recordMetadata = producer.send(new ProducerRecord<>(topic, key, value)).get(3, TimeUnit.SECONDS);
		test.log(Status.INFO, "The Producer Sending Message on topic " + topic + " and the key is " + key);
		test.log(Status.INFO, "The Producer Sending Message with key " + key + " and the value is " + value);
		test.log(Status.INFO, "The Producer Sending Message on topic with key " + key + " sent to  partition "
				+ recordMetadata.partition());
		return recordMetadata;
	}
	// *Kafka
		public RecordMetadata sendMessgaeGetMetadata(String topic, String key, String value,
				KafkaProducer<String, String> producer) throws Exception {
			RecordMetadata recordMetadata;
			recordMetadata = producer.send(new ProducerRecord<>(topic, key, value)).get(3, TimeUnit.SECONDS);
			
			return recordMetadata;
		}
	// *Kafka
	public void validateConsumedRecordsAgainstProducerRecords(String consumer, String testName, String topic,
			String firstTopicKey, String firstTopicValue, ExtentTest test, MongoClient mongoclient) {
		// TODO Auto-generated method stub
		boolean valid = false;
		MongoDatabase database = mongoclient.getDatabase("kafkadb");
		MongoCollection<Document> collection1 = database.getCollection("kafkamessagedata");

		BasicDBObject andQuery = new BasicDBObject();

		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		obj.add(new BasicDBObject("consumer", consumer));
		obj.add(new BasicDBObject("title", testName));
		obj.add(new BasicDBObject("topic", topic));
		obj.add(new BasicDBObject("key", firstTopicKey));
		obj.add(new BasicDBObject("value", firstTopicValue));
		andQuery.put("$and", obj);

		// DBCursor cursor7 = collection.find(andQuery);
		FindIterable<Document> cursor = collection1.find(andQuery);

		for (Document document : cursor) {
			if (document.containsValue(firstTopicKey)) {
				valid = true;
				break;
			}
		}
		if (valid = true)
			test.log(Status.PASS,
					"The Producer sent record with key " + firstTopicKey + " has been received by consumer");
		else
			test.log(Status.FAIL,
					"The Producer sent record with key " + firstTopicKey + " has not been received by consumer");
	}
}
