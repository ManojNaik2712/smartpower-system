package com.smartpower.outage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutageNotificationEvent {
    private String email;
    private String name;
    private String message;
    private String phoneNumber;
    private String pincode;
}
