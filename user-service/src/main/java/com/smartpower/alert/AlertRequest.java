package com.smartpower.alert;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AlertRequest {

    @NotBlank(message = "Pincode is required")
    @Pattern(regexp = "\\d{6}", message ="Pincode must be exactly 6 digits" )
    private String pincode;

    private String title;
    private String content;
}
