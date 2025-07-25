package com.smartpower;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String name;
    private String email;
    private String password;
    private String pincode;

//    @Enumerated(EnumType.STRING)
    private Role role;
}
