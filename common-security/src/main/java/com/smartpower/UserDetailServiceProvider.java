package com.smartpower;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Custom interface to provide user details for authentication.
 * Extends Spring Security's UserDetailsService.
 */
public interface UserDetailServiceProvider extends UserDetailsService {
    UserDetails loadUserByUsername(String username);
}
