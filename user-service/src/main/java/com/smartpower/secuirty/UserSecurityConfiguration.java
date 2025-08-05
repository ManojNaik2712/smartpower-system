package com.smartpower.secuirty;

import com.smartpower.JwtFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for defining security rules for the SmartPower application.
 * It sets up HTTP request access rules and integrates JWT-based authentication.
 */
@Configuration
@Slf4j
public class UserSecurityConfiguration {

    private final JwtFilter jwtFilter;

    public UserSecurityConfiguration(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    /**
     * Defines the security filter chain for the application.
     * Sets authorization rules, disables CSRF, form login, and basic auth,
     * and adds the JWT filter to the security chain.
     *
     * @param http the HttpSecurity object
     * @return the configured SecurityFilterChain
     * @throws Exception if any error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public APIs for Swagger and basic user actions
                        .requestMatchers(
                                "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**",
                                "/swagger-resources/**", "/webjars/**").permitAll()

                        .requestMatchers("/user/saveUser", "/user/getUser/{email}").permitAll()

                        // User role endpoints
                        .requestMatchers("/user/deleteUser", "/user/getMessage", "/user/update/profile",
                                "/complaint", "/getmy/complaint").hasRole("USER")

                        // Admin role endpoints
                        .requestMatchers("/user/send-alert", "/user/getAllUser",
                                "/getAll/complaints", "/get/complaint").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                // Add JWT filter before default auth filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable());

        log.info("Security filter chain configured successfully.");
        return http.build();
    }
}
