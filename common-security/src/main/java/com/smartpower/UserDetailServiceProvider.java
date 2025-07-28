package com.smartpower;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserDetailServiceProvider extends UserDetailsService {
    UserDetails loadUserByUsername(String username);
}
