package com.smartpower;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration class for Twilio API integration.
 */
@Component
@Slf4j
@Data
@ConfigurationProperties(prefix = "twilio")
public class TwilioConfig {
    private String accountSid;
    private String authToken;
    private String fromNumber;

    /**
     * Initializes the Twilio SDK with provided credentials after the bean is created.
     * This ensures that the Twilio service is ready to use when needed.
     */
    @PostConstruct
    public void init() {
        log.info("Initializing Twilio with account SID: {}", accountSid);
        Twilio.init(accountSid, authToken);
        log.info("Twilio initialized successfully.");
    }
}
