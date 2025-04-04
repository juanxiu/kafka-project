package com.kafka_project.demo.producer;

import com.kafka_project.demo.DemoApplication;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import lombok.RequiredArgsConstructor;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
@RequiredArgsConstructor
public class MessageProducer {

    private final KafkaProducer<String, GenericRecord> producer;

    public void sendMessage(){

        String topic = "customerContacts";

        // 에이브로 스키마 직접 지정
        String schemaString =
                "{\"name\":\"OrderCancelKafkaEvent\",\"namespace\":\"customer.avsc\",\"type\":\"record\",\"fields\":[{\"name\":\"orderId\",\"type\":\"long\"},{\"name\":\"userName\",\"type\":\"string\"}]}";


        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse(schemaString);


        // avro 메시지
        GenericRecord customer = new GenericData.Record(schema);
        customer.put("orderId", 1L);
        customer.put("userName", "yeonsoo");

        ProducerRecord<String, GenericRecord> data =
                new ProducerRecord<>(topic,customer);


        // 메시지 전송
        try {
            producer.send(data, new DemoProducerCallback());
        } catch (Exception e) {
            // 프로듀서가 카프카로 메시지를 보내기 전 에러가 발생할 경우 여전히 예외가 발생할 수 있다.
            e.printStackTrace();
        }

    }
    private static class DemoProducerCallback implements Callback {
        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            if (e != null) {
                e.printStackTrace(); // 만약 카프카가 에러를 리턴하면 onCompletion() 메서드가 null 아닌 Exception 객체를 받는다.
            }
        }
    }



}
