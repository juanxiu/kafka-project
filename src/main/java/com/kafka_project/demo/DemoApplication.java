package com.kafka_project.demo;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {

		// Properties 객체를 생성한다.
		Properties kafkaProps = new Properties();

		kafkaProps.put("bootstrap.servers", "broker1:9092, broker2:9092");


		// 메시지의 키값과 밸류값으로 문자열 타입을 사용하므로, StringSerializer 사용했다.
		kafkaProps.put("key.serializer",
				StringSerializer.class.getName());
		kafkaProps.put("value.serializer",
				StringSerializer.class.getName());


	    // Properties 객체를 넘겨줘서 새로운 프로듀서를 생성한다.
		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(kafkaProps);
		SpringApplication.run(DemoApplication.class, args);
	}

}
