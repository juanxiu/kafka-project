package com.kafka_project.demo.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageProducer {

    // .yml에 정의한 kafka 설정이 주입된 kafkaTemplete
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static String TOPIC_NAME = "test-topic";

    @GetMapping("producer")
    public String sendMessage(){
        String messageData = "kafka message";
        kafkaTemplate.send(TOPIC_NAME, messageData);
        return "success. ";
    }

}
