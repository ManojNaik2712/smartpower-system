package com.smartpower;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for listening to Kafka events
 * related to outages, due date reminders, and complaints,
 * and sending notifications via email and SMS.
 */
@Service
@Slf4j
public class NotificationListener {

    private final JavaMailSender mailSender;
    private final TwilioConfig twilioConfig;

    @Value("${twilio.fromNumber}")
    private String fromNumber;

    public NotificationListener(JavaMailSender mailSender, TwilioConfig twilioConfig) {
        this.mailSender = mailSender;
        this.twilioConfig = twilioConfig;
    }

    /**
     * Kafka listener for outage events.
     *
     * @param event The outage notification event.
     */
    @KafkaListener(topics = "outage-topic", groupId = "notification-group", containerFactory = "kafkaListenerContainerFactory")
    public void listenOutage(OutageNotificationEvent event) {
        log.info("Received outage event: {}", event);
        sendEmailForOutage(event);
        sendOutageSMS(event);
    }

    private void sendEmailForOutage(OutageNotificationEvent event) {
        log.debug("Sending outage email to: {}", event.getEmail());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.getEmail());
        message.setSubject("⚠ Power Outage Alert");
        message.setText("Hello " + event.getName() + ",\n\n" + event.getMessage() + "\n\nStay safe,\nSmartPower Team");
        message.setFrom("manunaik599@gmail.com");

        mailSender.send(message);
        log.info("Outage email sent to: {}", event.getEmail());
    }

    public void sendOutageSMS(OutageNotificationEvent event) {
        log.debug("Sending outage SMS to: {}", event.getPhoneNumber());

        String messageBody = event.getMessage();
        Message message = Message.creator(
                new PhoneNumber(event.getPhoneNumber()),
                new PhoneNumber(fromNumber),
                messageBody
        ).create();

        log.info("Outage SMS sent with SID: {}", message.getSid());
    }

    /**
     * Kafka listener for due date reminders.
     *
     * @param event The due date reminder event.
     */
    @KafkaListener(topics = "due-date-reminder-topic", groupId = "notification-group", containerFactory = "kafkaListenerContainerFactory")
    public void listenDueReminder(DueDateReminderEvent event) {
        log.info("Received due date reminder: {}", event);
        sendEmailForDueReminder(event);
        sendAlertSMS(event);
    }

    private void sendEmailForDueReminder(DueDateReminderEvent event) {
        log.debug("Sending due reminder email to: {}", event.getEmail());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("manunaik599@gmail.com");
        message.setTo(event.getEmail());
        message.setSubject("Due date Reminder");
        message.setText(event.getMessage());

        mailSender.send(message);
        log.info("Due reminder email sent to: {}", event.getEmail());
    }

    public void sendAlertSMS(DueDateReminderEvent event) {
        log.debug("Sending due reminder SMS to: {}", event.getPhoneNumber());

        String messageBody = event.getMessage();
        Message message = Message.creator(
                new PhoneNumber(event.getPhoneNumber()),
                new PhoneNumber(fromNumber),
                messageBody
        ).create();

        log.info("Due reminder SMS sent with SID: {}", message.getSid());
    }

    /**
     * Kafka listener for complaint events.
     *
     * @param complaintEvent The complaint event.
     */
    @KafkaListener(topics = "complaint-topic", groupId = "notification-group", containerFactory = "kafkaListenerContainerFactory")
    public void listenComplaint(ComplaintEvent complaintEvent) {
        log.info("Received complaint event from user: {}", complaintEvent.getUsername());
        sendComplaintEmail(complaintEvent);
    }

    private void sendComplaintEmail(ComplaintEvent event) {
        log.debug("Sending complaint email to admin: {}", event.getAdminEmail());

        String text = "Complaint is sent by: " + event.getUsername() + "\n\n" +
                "Complaint message: \n\n" +
                event.getMessage();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(event.getUserEmail());
        message.setTo(event.getAdminEmail());
        message.setSubject(event.getSubject());
        message.setText(text);

        mailSender.send(message);
        log.info("Complaint email sent to: {}", event.getAdminEmail());
    }

}
