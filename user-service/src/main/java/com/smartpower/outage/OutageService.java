package com.smartpower.outage;

import com.smartpower.OutageNotificationEvent;
import com.smartpower.Role;
import com.smartpower.UserException.UserNotFoundException;
import com.smartpower.user.User;
import com.smartpower.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for handling outage notifications.
 * Sends outage messages to users based on their pincode
 * and saves those messages to the database.
 */
@Service
@Slf4j
public class OutageService {

    private final OutageRepository outageRepository;
    private final UserRepository userRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OutageService(OutageRepository outageRepository, UserRepository userRepository,
                         KafkaTemplate<String, Object> kafkaTemplate) {
        this.outageRepository = outageRepository;
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Sends an outage message to all users in the specified pincode
     * and persists the message in the database.
     *
     * @param pincode The pincode to target users.
     * @param title   The title of the outage message.
     * @param content The content/body of the outage message.
     * @throws UserNotFoundException if no users are found for the given pincode.
     */
    public void sendOutageMessage(String pincode, String title, String content) {
        log.info("Sending outage message to users in pincode: {}", pincode);

        List<User> users = userRepository.findByPincodeAndRole(pincode, Role.USER);

        if (users.isEmpty()) {
            throw new UserNotFoundException("No user found for pincode:" + pincode);
        }

        for (User user : users) {
            OutageNotificationEvent event = new OutageNotificationEvent(user.getEmail(), user.getName(),
                    content, user.getPhoneNumber(), user.getPincode());
            kafkaTemplate.send("outage-topic", event);
            log.debug("Sent outage event to user: {}", user.getEmail());
        }

        OutageMessage message = new OutageMessage();
        message.setPincode(pincode);
        message.setContent(content);
        message.setTitle(title);
        message.setTimestamp(LocalDateTime.now());

        outageRepository.save(message);
        log.info("Outage message saved to database for pincode: {}", pincode);
    }

    /**
     * Retrieves all outage messages associated with the given pincode.
     *
     * @param pincode The pincode to filter messages.
     * @return A list of outage messages for the pincode.
     */
    public List<OutageMessage> getMessages(String pincode) {
        log.info("Fetching outage messages for pincode: {}", pincode);
        return outageRepository.findByPincode(pincode);
    }
}
