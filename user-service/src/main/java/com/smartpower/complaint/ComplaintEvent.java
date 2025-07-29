package com.smartpower.complaint;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintEvent {
    private String subject;
    private String message;
    private String username;
    private String userEmail;
    private String adminEmail;
}
