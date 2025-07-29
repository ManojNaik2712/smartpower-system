package com.smartpower;

import lombok.Data;

@Data
public class OutageNotificationEvent {
    private String email;
    private String name;
    private String message;
    private String phoneNumber;
    private String pincode;
}
