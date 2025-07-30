package com.smartpower.complaint;

import lombok.Data;

@Data
public class ComplaintRequest {
    private String subject;
    private String message;
}
