package com.smartpower.outage;

import com.smartpower.OutageNotificationEvent;
import com.smartpower.Role;
import com.smartpower.UserException.UserNotFoundException;
import com.smartpower.user.User;
import com.smartpower.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OutageService {

    private static final Logger log = LoggerFactory.getLogger(OutageService.class);
    private final OutageRepository outageRepository;
    private final UserRepository userRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OutageService(OutageRepository outageRepository, UserRepository userRepository,
                         KafkaTemplate<String, Object> kafkaTemplate) {
        this.outageRepository = outageRepository;
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOutageMessage(String pincode, String title, String content) {
        List<User> users = userRepository.findByPincodeAndRole(pincode, Role.USER);

        if (users.isEmpty()) {
            throw new UserNotFoundException("No user found for pincode:" + pincode);
        }

        for (User user : users) {
            OutageNotificationEvent event = new OutageNotificationEvent(user.getEmail(), user.getName(),
                    content, user.getPhoneNumber(), user.getPincode());
            kafkaTemplate.send("outage-topic", event);
        }

        OutageMessage message = new OutageMessage();
        message.setPincode(pincode);
        message.setContent(content);
        message.setTitle(title);
        message.setTimestamp(LocalDateTime.now());
        outageRepository.save(message);
    }

    public List<OutageMessage> getMessages(String pincode) {
        return outageRepository.findByPincode(pincode);
    }
}
