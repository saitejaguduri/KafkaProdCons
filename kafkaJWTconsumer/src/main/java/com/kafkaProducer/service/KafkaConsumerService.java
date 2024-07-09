package com.kafkaProducer.service;

import com.kafkaProducer.entity.LoginRequest;
import com.kafkaProducer.entity.RegisterRequest;
import com.kafkaProducer.entity.User;
import com.kafkaProducer.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @Autowired
    private UserRepository userRepository;

    @KafkaListener(topics = "topicdata", groupId = "group1", containerFactory = "registerKafkaListenerContainerFactory")
    public void consumeRegisterRequest(RegisterRequest registerRequest) {
        logger.info("Received RegisterRequest: {}", registerRequest);

        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        user.setRole(registerRequest.getRole());

        try {
            userRepository.save(user);
            logger.info("User registered and saved to database: {}", registerRequest.getEmail());
        } catch (Exception e) {
            logger.error("Error saving user to database: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "topicdata", groupId = "group1", containerFactory = "loginKafkaListenerContainerFactory")
    public void consumeLoginRequest(LoginRequest loginRequest) {
        logger.info("Received LoginRequest: {}", loginRequest);

        try {
            Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());
            if (userOpt.isPresent() && userOpt.get().getPassword().equals(loginRequest.getPassword())) {
                logger.info("User logged in: {}", loginRequest.getEmail());
            } else {
                logger.info("Login failed for: {}", loginRequest.getEmail());
            }
        } catch (Exception e) {
            logger.error("Error processing LoginRequest: {}", e.getMessage());
        }
    }
}
