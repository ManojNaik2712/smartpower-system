package com.smartpower;

import lombok.Data;

@Data
public class ComplaintEvent {
    private String subject;
    private String message;
    private String username;
    private String userEmail;
    private String adminEmail;
}
