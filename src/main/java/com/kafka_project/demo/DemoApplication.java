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

		/*
		카프카에 쓸 레코드의 키와 밸류값을 직렬화하기 위해 시리얼라이저 클래스를 사용한다.
		카프카 브로커는 메시지의 키값, 밸류값으로 바이트 배열을 받지만, 프로듀서 인터페이스는 임의의 자바 객체를 키 혹은 밸류로 전송할 수 있도록 매개변수화된 타입을 사용할 수 있게 한다.
		프로듀서 입장에서는 이 객체를 어떻게 바이트로 바꿔야 하는지 알아야 한다.
		메시지의 키값과 밸류값으로 문자열 타입을 사용하므로, StringSerializer 사용했다.
		 */
		kafkaProps.put("key.serializer",
				StringSerializer.class.getName());
		kafkaProps.put("value.serializer",
				StringSerializer.class.getName());


	    // Properties 객체를 넘겨줘서 새로운 프로듀서를 생성한다.
		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(kafkaProps);
		SpringApplication.run(DemoApplication.class, args);
	}

}
