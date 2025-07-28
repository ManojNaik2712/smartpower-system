package com.smartpower;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class DueDateReminderScheduler {

    private final UserRepository userRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;


    public DueDateReminderScheduler(UserRepository userRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
    }


    @Scheduled(cron = "0 45 11 * * ?") //Every day at 11 45Am due date notification will be sent
    public void sendDueDateReminders() {
        List<User> users = userRepository.findAll();
        LocalDateTime today = LocalDateTime.now();

        for (User user : users) {
            LocalDate dueDate = user.getDuedate();
            if (dueDate != null && ChronoUnit.DAYS.between(today, dueDate) <= 3) {
                DueDateReminderEvent event = new DueDateReminderEvent();
                event.setEmail(user.getEmail());
                event.setMessage("Your electricity bill is due on " + dueDate + ". Please pay soon to avoid disconnection.");

                kafkaTemplate.send("due-date-reminder-topic", event);
            }
        }
    }

}
