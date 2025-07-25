package com.smartpower;

import lombok.Data;

@Data
public class AlertRequest {
    private String pincode;
    private String title;
    private String content;
}
