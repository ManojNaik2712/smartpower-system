package com.smartpower;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class DueDateReminderScheduler {

    private final UserRepository userRepository;
    private final KafkaTemplate<String, DueDateReminderEvent> kafkaTemplate;


    public DueDateReminderScheduler(UserRepository userRepository, KafkaTemplate<String, DueDateReminderEvent> kafkaTemplate) {
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
    }


    @Scheduled(cron = "0 0 9 * * ?") // Every day at 9 AM
    public void sendDueDateReminders() {
        List<User> users = userRepository.findAll();
        LocalDate today = LocalDate.now();

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
