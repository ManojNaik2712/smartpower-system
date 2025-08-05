package com.smartpower;

import com.smartpower.AuthException.AdminSecretMismatchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * Service class for handling authentication business logic.
 * Handles registration,login.
 */
@Service
@Slf4j
public class AuthService {

    private final JwtService jwtService;
    private final UserClient userClient;
    private final AuthenticationManager authenticationManager;

    @Value("${admin.secret}")
    private String adminSecret;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService,
                       UserClient userClient) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userClient = userClient;
    }

    /**
     * Registers a new user and generates a JWT token for them.
     * Registers a new user and generates a JWT token for them.
     *
     * @param userRequest DTO contaning the new user data
     * @return JWT token for newly registered user
     */
    public String createUser(UserRequest userRequest) {
        log.info("Attempting to register user with email: {}", userRequest.getEmail());
        userClient.saveUser(userRequest);
        String token = jwtService.generateToken(userRequest.getEmail(), userRequest.getRole());
        log.info("User registered successfully, JWT token generated");
        return token;
    }

    /**
     * Authenticates a user and generates a JWT token on success.
     * Admin users must also provide a valid admin secret to log in.
     *
     * @param loginDTO contaning the login  credentials adn role
     * @return JWT token for the authenticated user
     */

    public String loginUser(LoginDTO loginDTO) {
        log.info("Login attempt for email: {}", loginDTO.getEmail());

        //If role is ADMIN, validate the admin secret
        if (loginDTO.getRole().equals(Role.ADMIN)) {
            if (loginDTO.getAdminSecret() == null ||
                    !loginDTO.getAdminSecret().equals(adminSecret)) {
                throw new AdminSecretMismatchException("Invalid secretcode");
            }
        }

        //Authenticate the user credentials
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

        if (!authentication.isAuthenticated()) {
            throw new BadCredentialsException("Invalid credentials");
        }

        log.info("Authentication successful for email: {}", loginDTO.getEmail());

        //Fetch user details from user-service via FiegnClient and generate token
        UserResponse user = userClient.getUser(loginDTO.getEmail());
        String token = jwtService.generateToken(user.getEmail(), user.getRole());

        log.info("JWT token generated for user: {}", loginDTO.getEmail());
        return token;
    }
}
