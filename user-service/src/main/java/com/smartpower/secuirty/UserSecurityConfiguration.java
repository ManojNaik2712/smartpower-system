package com.smartpower.secuirty;

import com.smartpower.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class UserSecurityConfiguration {

    private final JwtFilter jwtFilter;

    public UserSecurityConfiguration(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/saveUser", "/user/getUser/{email}").permitAll()
                        .requestMatchers("/user/deleteUser", "/user/getMessage","/user/update/profile","/complaint").hasRole("USER")
                        .requestMatchers("/user/send-alert","/user/getAllUser","/getAll/complaints").hasRole("ADMIN")
                        .requestMatchers("/get/complaint").hasAnyRole("USER","ADMIN")
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
