package com.smartpower;

import com.smartpower.AuthException.AdminSecretMismatchException;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${admin.secret}")
    private String adminSecret;

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
        if (loginDTO.getRole().equals(Role.ADMIN)) {
            if (loginDTO.getAdminSecret() == null ||
                    !loginDTO.getAdminSecret().equals(adminSecret)) {
                throw new AdminSecretMismatchException("Invalid secretcode");
            }
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

        if (!authentication.isAuthenticated()) {
            throw new BadCredentialsException("Invalid credentials");
        }

        UserResponse user = userClient.getUser(loginDTO.getEmail());
        return jwtService.generateToken(user.getEmail(), user.getRole());

    }
}
