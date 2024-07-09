package com.kafkaProducer.config;

import com.kafkaProducer.entity.LoginRequest;
import com.kafkaProducer.entity.RegisterRequest;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Bean
    public ConsumerFactory<String, RegisterRequest> registerRequestConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group1");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, RegisterRequest.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.kafkaProducer.entity");

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RegisterRequest> registerKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, RegisterRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(registerRequestConsumerFactory());
        factory.setRecordFilterStrategy(record -> {
            String messageType =  new String(record.headers().lastHeader("messageType").value());
            return !messageType.equals("register");
        });
        return factory;
    }

    @Bean
    public ConsumerFactory<String, LoginRequest> loginRequestConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group1");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, LoginRequest.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.kafkaProducer.entity");

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LoginRequest> loginKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, LoginRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(loginRequestConsumerFactory());
        factory.setRecordFilterStrategy(record -> {
            String messageType = new String(record.headers().lastHeader("messageType").value());
            return !messageType.equals("login");
        });
        return factory;
    }
}
