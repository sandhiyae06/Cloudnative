package com.KAFKATESTING.ConsumerTest;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import com.KAFKATESTING.Message;
import javax.xml.bind.DatatypeConverter;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AvroDeserializer<User extends SpecificRecordBase> extends io.confluent.kafka.serializers.AbstractKafkaAvroDeserializer implements Deserializer<Object>  {

  private static final Logger LOGGER = LoggerFactory.getLogger(AvroDeserializer.class);

  protected  Class<Message> targetType;
//@SuppressWarnings("unused")
public AvroDeserializer(){
	
}
  public AvroDeserializer(Class<Message> targetType) {
    this.targetType = targetType;
  }

  @Override
  public void close() {
    // No-op
  }

  @Override
  public void configure(Map<String, ?> arg0, boolean arg1) {
    // No-op
  }

  @SuppressWarnings("unchecked")
  @Override
  public Object deserialize(String topic, byte[] data) {
    try {
    	Object result = null;
    	org.apache.avro.Schema SCHEMA = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Message\",\"namespace\":\"com.KAFKATESTING\",\"fields\":[{\"name\":\"id\",\"type\":\"string\"},{\"name\":\"from\",\"type\":\"string\"},{\"name\":\"to\",\"type\":\"string\"},{\"name\":\"text\",\"type\":\"string\"}]}");
    	org.apache.avro.Schema SCHEMA2 = new com.KAFKATESTING.Message().getClassSchema();
    	if (data != null) {
        LOGGER.debug("data='{}'", DatatypeConverter.printHexBinary(data));

        /*DatumReader<GenericRecord> datumReader =
            new SpecificDatumReader<>(targetType.newInstance().getSchema());*/
        DatumReader<GenericRecord> datumReader =
                new SpecificDatumReader<>(SCHEMA);
        /*DatumReader<GenericRecord> datumReader =
                new GenericDatumReader<>(User.SCHEMA$);*/
        Decoder decoder = DecoderFactory.get().binaryDecoder(data, null);

        result = (Object) datumReader.read(null, decoder);
        LOGGER.debug("deserialized data='{}'", result);
      }
      return result;
    } catch (Exception ex) {
    	return null;
      /*throw new SerializationException(
          "Can't deserialize data '" + Arrays.toString(data) + "' from topic '" + topic + "'", ex);*/
    }
  }
  
  /*@Override
  public Object deserialize(String s, byte[] bytes) {
      try {
          return (Object) deserialize(bytes);
      } catch (SerializationException e)
      {
          return null;
      }
  }

  public Object deserialize(String s, byte[] bytes, Schema readerSchema) {
      try {
          return (Object) deserialize(bytes, readerSchema);
      } catch (SerializationException e)
      {
          return null;
      }
}*/
}
