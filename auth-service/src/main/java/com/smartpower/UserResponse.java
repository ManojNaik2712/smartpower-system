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
    private String pincode;

//    @Enumerated(EnumType.STRING)
    private Role role;
}
