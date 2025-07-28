package com.smartpower;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {

    private final JavaMailSender mailSender;

    public NotificationListener(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @KafkaListener(topics = "outage-topic", groupId = "notification-group", containerFactory = "kafkaListenerContainerFactory")
    public void listenOutage(OutageNotificationEvent event) {
        System.out.println("Recived due reminder: " + event);
        sendEmailForOutage(event);
    }

    @KafkaListener(topics = "due-date-reminder-topic", groupId = "notification-group", containerFactory = "kafkaListenerContainerFactory")
    public void listenDueReminder(DueDateReminderEvent event) {
        System.out.println("Recived due reminder: " + event);
        sendEmailForDueReminder(event);
    }

    private void sendEmailForOutage(OutageNotificationEvent event) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.getEmail());
        message.setSubject("⚠ Power Outage Alert");
        message.setText("Hello " + event.getName() + ",\n\n" + event.getMessage() + "\n\nStay safe,\nSmartPower Team");
        message.setFrom("manunaik599@gmail.com");

        mailSender.send(message);
    }

    private void sendEmailForDueReminder(DueDateReminderEvent event) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("manunaik599@gmail.com");
        message.setTo(event.getEmail());
        message.setSubject("Due date Reminder");
        message.setText(event.getMessage());
        mailSender.send(message);
    }
}
