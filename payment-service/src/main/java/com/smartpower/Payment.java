package com.smartpower;

import jakarta.persistence.Entity;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Payment {
    private Long id;
    private String userEmail;
    private double amount;
    private LocalDate paymentDate;
    private String method;
    private String status;
}
