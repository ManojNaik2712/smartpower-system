package com.smartpower;

import lombok.Data;

@Data
public class PaymentRequest {
    private String method;
    private double amount;
}
