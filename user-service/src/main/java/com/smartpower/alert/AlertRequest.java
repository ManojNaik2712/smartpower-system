package com.smartpower.alert;

import lombok.Data;

@Data
public class AlertRequest {
    private String pincode;
    private String title;
    private String content;
}
