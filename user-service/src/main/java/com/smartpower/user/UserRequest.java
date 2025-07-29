package com.smartpower.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.smartpower.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequest {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String pincode;
    private Role role;
    private String adminSecret;

}

