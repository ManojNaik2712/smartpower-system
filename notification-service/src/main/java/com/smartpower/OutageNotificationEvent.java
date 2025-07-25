package com.smartpower;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class OutageNotificationEvent {
    private String email;
    private String name;
    private String message;
}
