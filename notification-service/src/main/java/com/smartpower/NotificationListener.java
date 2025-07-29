package com.smartpower;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {

    private final JavaMailSender mailSender;
    private final TwilioConfig twilioConfig;

    public NotificationListener(JavaMailSender mailSender, TwilioConfig twilioConfig) {
        this.mailSender = mailSender;
        this.twilioConfig = twilioConfig;
    }

    @Value("${twilio.fromNumber}")
    private String fromNumber;

    @KafkaListener(topics = "outage-topic", groupId = "notification-group", containerFactory = "kafkaListenerContainerFactory")
    public void listenOutage(OutageNotificationEvent event) {
        System.out.println("Recived due reminder: " + event);
        sendEmailForOutage(event);
        sendSMSAlert(event);
    }

    private void sendEmailForOutage(OutageNotificationEvent event) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.getEmail());
        message.setSubject("⚠ Power Outage Alert");
        message.setText("Hello " + event.getName() + ",\n\n" + event.getMessage() + "\n\nStay safe,\nSmartPower Team");
        message.setFrom("manunaik599@gmail.com");

        mailSender.send(message);
    }

    public void sendSMSAlert(OutageNotificationEvent event) {
        String messageBody = event.getMessage();
        Message message = Message.creator(
                new PhoneNumber(event.getPhoneNumber()),
                new PhoneNumber(fromNumber),
                messageBody
        ).create();

    }

    @KafkaListener(topics = "due-date-reminder-topic", groupId = "notification-group", containerFactory = "kafkaListenerContainerFactory")
    public void listenDueReminder(DueDateReminderEvent event) {
        sendEmailForDueReminder(event);
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
