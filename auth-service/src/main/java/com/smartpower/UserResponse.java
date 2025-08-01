package com.smartpower;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String pincode;
    private Role role;
}
