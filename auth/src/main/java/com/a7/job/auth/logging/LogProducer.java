package com.a7.job.auth.logging;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class LogProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "app-logs";

    public LogProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendLog(String logMessage) {
        kafkaTemplate.send(TOPIC, logMessage);
    }
}
