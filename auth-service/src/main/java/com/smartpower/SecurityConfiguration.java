package com.smartpower;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * This class configures Spring Security for the SmartPower application.
 * It defines the authentication provider, password encoding strategy,
 * authentication manager, and HTTP security rules.
 */
@Configuration
@Slf4j
public class SecurityConfiguration {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailServiceProvider userDetailServiceProvider;

    public SecurityConfiguration(PasswordEncoder passwordEncoder, UserDetailServiceProvider userDetailServiceProvider) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailServiceProvider = userDetailServiceProvider;
    }

    /**
     * Configures the authentication provider using a DAO-based provider
     * that fetches user details and encodes passwords.
     *
     * @return an instance of AuthenticationProvider
     */
    public AuthenticationProvider authenticationProvider() {
        log.info("Initializing AuthenticationProvider using DAO and custom UserDetailServiceProvider");
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailServiceProvider);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();

    }

    /**
     * Configures the HTTP security rules for the application
     *
     * @param http HttpSecurity instance
     * @return SecurityFilterChain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**",
                                "/swagger-resources/**", "/webjars/**").permitAll()
                        .requestMatchers("/auth/register", "/auth/login").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable());

        log.info("Security configuration initialized successfully");
        return http.build();
    }

}
