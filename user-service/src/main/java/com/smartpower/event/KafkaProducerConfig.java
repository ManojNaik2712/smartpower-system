package com.smartpower.event;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * KafkaProducerConfig sets up the Kafka producer configuration for the application.
 * It defines the necessary producer factory and KafkaTemplate beans used to send messages to Kafka topics.
 */
@Configuration
@Slf4j
public class KafkaProducerConfig {

    /**
     * Creates and configures the ProducerFactory bean.
     *
     * @return a ProducerFactory with custom Kafka producer settings
     */
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        log.info("Initializing Kafka Producer Factory...");

        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    /**
     * Creates and configures the KafkaTemplate bean for publishing messages.
     *
     * @return a KafkaTemplate that can send messages to Kafka topics
     */
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        log.info("Creating Kafka Template...");
        return new KafkaTemplate<>(producerFactory());
    }
}

