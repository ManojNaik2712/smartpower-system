package com.smartpower;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtService jwtService;
    private final UserClient userClient;
    private final AuthenticationManager authenticationManager;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService,
                       UserClient userClient) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userClient = userClient;
    }

    public String createUser(UserRequest userRequest) {
        userClient.saveUser(userRequest);
        return jwtService.generateToken(userRequest.getEmail(), userRequest.getRole());
    }

    public String loginUser(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

        if (!authentication.isAuthenticated()) {
            throw new BadCredentialsException("Invalid credentials");
        }

        UserResponse user = userClient.getUser(loginDTO.getEmail());
        return jwtService.generateToken(user.getEmail(), user.getRole());

    }
}
