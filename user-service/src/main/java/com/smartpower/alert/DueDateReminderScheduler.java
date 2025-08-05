package com.smartpower.alert;


import com.smartpower.DueDateReminderEvent;
import com.smartpower.user.User;
import com.smartpower.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
public class DueDateReminderScheduler {

    private final UserRepository userRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;


    public DueDateReminderScheduler(UserRepository userRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * This method runs every day at 11:45 AM and checks for users whose due date
     * is within the next 3 days. It sends a reminder notification for such users
     * via Kafka.
     */
    @Scheduled(cron = "0 45 11 * * ?") //Every day at 11:45 AM
    public void sendDueDateReminders() {
        log.info("Starting due date reminder scheduler...");

        List<User> users = userRepository.findAll();
        LocalDateTime today = LocalDateTime.now();

        for (User user : users) {
            LocalDate dueDate = user.getDuedate();

            if (dueDate != null && ChronoUnit.DAYS.between(today, dueDate) <= 3) {
                DueDateReminderEvent event = new DueDateReminderEvent();
                event.setEmail(user.getEmail());
                event.setPhoneNumber(user.getPhoneNumber());
                event.setMessage("Your electricity bill is due on " + dueDate + ". Please pay soon to avoid disconnection.");

                kafkaTemplate.send("due-date-reminder-topic", event);
                log.info("Sent due date reminder to user: {}", user.getEmail());
            } else {
                log.warn("User {} has no due date set. Skipping.", user.getEmail());
            }
        }
        log.info("Due date reminder scheduler completed.");
    }

}
