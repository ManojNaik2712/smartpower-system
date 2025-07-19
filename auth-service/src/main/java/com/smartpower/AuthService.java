package com.smartpower;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public String createUser(UserRequest userRequest) {
        userClient.saveUser(userRequest);
        return jwtService.generateToken(userRequest.getEmail(), userRequest.getRole());
    }

    public String loginUser(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

        if (!authentication.isAuthenticated()) {
            throw new BadCredentialsException("Invalid credentials");
        }
        System.out.println(loginDTO.getEmail());

        UserResponse user = userClient.getUser(loginDTO.getEmail());
        return jwtService.generateToken(user.getEmail(), user.getRole());

    }
}
