package com.smartpower;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Custom implementation of UserDetailServiceProvider.
 * Used by Spring Security to retrieve user information from the User microservice via Feign client.
 */
@Service
@Slf4j
public class CustomUserDetailService implements UserDetailServiceProvider {

    private final UserClient userClient;

    public CustomUserDetailService(UserClient userClient) {
        this.userClient = userClient;
    }

    /**
     * Loads the user details from the User microservice using the provided email.
     *
     * @param email The email of user trying to log in
     * @return UserDetails object used by Spring Security for authentication
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Loading user by username: {}", email);
        UserResponse user = userClient.getUser(email);

        if (user == null) {
            throw new UsernameNotFoundException("Username not found with email:" + email);
        }

        log.info("User loaded with role: {}", user.getRole());

        // Return Spring Security UserDetails with a single granted authority
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }
}
