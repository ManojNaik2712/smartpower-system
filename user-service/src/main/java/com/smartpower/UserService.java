package com.smartpower;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService {
    @Value("${admin.secret}")
    private String adminSecret;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void createUser(UserRequest userRequest) {
        if (userRequest.getRole().equals(Role.ADMIN)) {
            if (userRequest.getAdminSecret() == null ||
                    !userRequest.getAdminSecret().equals(adminSecret)) {
                throw new RuntimeException("Invalid secretcode");
            }
        }
        User user = new User();
        user.setName(userRequest.getName());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(userRequest.getRole());
        user.setEmail(userRequest.getEmail());
        user.setPincode(userRequest.getPincode());
        userRepository.save(user);
    }

    public UserResponse getUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return UserResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .password(user.getPassword())
                .pincode(user.getPincode())
                .build();
    }

    public void deleteUser(String email) {
        userRepository.deleteByEmail(email);
    }

    public ResponseEntity<String> updateDueDate(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User with this email:" + email + "not found");
        }

        user.setDuedate(LocalDate.now().plusDays(30));
        userRepository.save(user);
        return ResponseEntity.ok("Due date is updated");
    }
}
