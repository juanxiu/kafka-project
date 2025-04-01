package com.kafka_project.demo;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import java.util.Properties;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {

		// Properties 객체를 생성한다.
		Properties kafkaProps = new Properties();

//		kafkaProps.put("bootstrap.servers", "broker1:9092, broker2:9092");

		kafkaProps.put("bootstrap.servers", "localhost:9092");

		// 메시지의 키값과 밸류값으로 문자열 타입을 사용하므로, StringSerializer 사용했다.
		kafkaProps.put("key.serializer",
				StringSerializer.class.getName());
		kafkaProps.put("value.serializer",
				StringSerializer.class.getName());


	    // Properties 객체를 넘겨줘서 새로운 프로듀서를 생성한다.
		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(kafkaProps);


		// 프로듀서는 ProducerRecord 객체를 받으므로 이 객체를 생성해준다.
		ProducerRecord<String, String> record =
				new ProducerRecord<>("CustomerCountry", "Precision Products", "France");

		ProducerRecord<String, String > record2 =
				new ProducerRecord<>("CustomerCountry", "Biomedical Materials", "USA");

		// 레코드를 전송할 때, Callback 객체를 함께 매개변수로 전달한다.
		producer.send(record2, new DemoProducerCallback());

		// ProducerRecord 를 전송하기 위해 프로듀서 객체의 send 메서드를 사용한다.
		try{
			producer.send(record);
		} catch (Exception e){
			// 프로듀서가 카프카로 메시지를 보내기 전 에러가 발생할 경우 여전히 예외가 발생할 수 있다.
			e.printStackTrace();
		}

		SpringApplication.run(DemoApplication.class, args);
	}

	/*
	메시지는 비동기적으로 전송되지만, 메시지 전송에 완전히 실패했을 때를 위해 에러 처리가 필요하다.
	에러 처리를 위해 프로듀서는 레코드를 전송할 때 콜백을 지정할 수 있도록 한다.
	 */
	private static class DemoProducerCallback implements Callback {
		@Override
		public void onCompletion(RecordMetadata recordMetadata, Exception e) {
			if (e != null) {
				e.printStackTrace(); // 만약 카프카가 에러를 리턴하면 onCompletion() 메서드가 null 아닌 Exception 객체를 받는다.
			}
		}
	}


}
