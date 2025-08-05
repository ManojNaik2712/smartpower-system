package com.smartpower;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for Kafka consumer.
 * This sets up the consumer factory and listener container factory
 * for consuming messages from Kafka topics.
 */
@Configuration
@Slf4j
public class KafkaConsumerConfig {

    /**
     * Creates a Kafka ConsumerFactory with required configuration.
     *
     * @return ConsumerFactory for consuming Kafka messages
     */
    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        log.info("Initializing Kafka ConsumerFactory...");

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "notification-group");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        log.debug("Kafka Consumer Properties: {}", props);

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(Object.class)
        );
    }

    /**
     * Creates a Kafka Listener Container Factory.
     * This is used to register Kafka listeners for consuming messages.
     *
     * @return ConcurrentKafkaListenerContainerFactory for handling Kafka messages
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        log.info("Creating KafkaListenerContainerFactory...");

        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        log.info("KafkaListenerContainerFactory initialized successfully.");

        return factory;
    }
}
