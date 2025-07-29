package com.smartpower.alert;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DueDateReminderEvent {
    private String email;
    private LocalDate dueDate;
    private String message;
}
