package com.kafka.exchange.streams.configuration;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {


    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> config =  new HashMap<>();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092,127.0.0.1:9093,127.0.0.1:9094");
        return new KafkaAdmin(config);
    }

    @Bean
    public NewTopic transactionTopic() {
        return new NewTopic("transaction_topic", 2, (short)3);
    }

    @Bean
    public NewTopic processingTopic() {
        return new NewTopic("processing_topic", 2, (short) 3);
    }

    @Bean
    public NewTopic validPaymentTopic() {
        return new NewTopic("payment_topic", 2, (short) 3);
    }

    @Bean
    public NewTopic validAddressTopic() {
        return new NewTopic("address_topic", 2, (short) 3);
    }
}
