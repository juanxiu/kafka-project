package com.kafka_project.demo.producer;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class ProducerConfig {

    @Value("${spring.kafka.properties.schema.registry.url}")
    private String schemaRegistryUrl;


    @Bean
    public Properties producerProperties(){

        // Properties 객체를 생성한다.
        Properties kafkaProps = new Properties();

        kafkaProps.put("bootstrap.servers", "localhost:9092");

        // 메시지의 키값과 밸류값으로 문자열 타입을 사용하므로, StringSerializer 사용했다.
        kafkaProps.put("key.serializer",
                StringSerializer.class.getName());
        kafkaProps.put("value.serializer",
                KafkaAvroSerializer.class.getName());

        // 에이브로 시리얼라이저의 설정 매개변수로, 스키마를 저장해놓는 위치를 가리키는 값이다.
        kafkaProps.put("schema.registry.url", schemaRegistryUrl);

        return kafkaProps;

    }

    @Bean
    public KafkaProducer<String, String> kafkaProducer(Properties producerProperties){
        return new KafkaProducer<>(producerProperties);
    }
}
