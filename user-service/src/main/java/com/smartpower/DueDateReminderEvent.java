package com.smartpower;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DueDateReminderEvent {
    private String email;
    private LocalDate dueDate;
    private String message;
}
