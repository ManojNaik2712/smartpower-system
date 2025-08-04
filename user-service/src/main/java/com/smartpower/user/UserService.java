package com.smartpower.user;

import com.smartpower.AuthException.AdminSecretMismatchException;
import com.smartpower.Role;
import com.smartpower.UserException.DuplicateUserException;
import com.smartpower.UserException.UserNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.secret}")
    private String adminSecret;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void createUser(UserRequest userRequest) {
        if (userRequest.getRole().equals(Role.ADMIN)) {
            if (userRequest.getAdminSecret() == null ||
                    !userRequest.getAdminSecret().equals(adminSecret)) {
                throw new AdminSecretMismatchException("Invalid secretcode");
            }
        } else if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new DuplicateUserException("User already exists with email: " + userRequest.getEmail());
        }

        User user = new User();
        user.setName(userRequest.getName());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(userRequest.getRole());
        user.setEmail(userRequest.getEmail());
        user.setPincode(userRequest.getPincode());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        userRepository.save(user);
    }

    public UserResponse getUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        return UserResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .pincode(user.getPincode())
                .build();
    }

    public void deleteUser(String email) {
        userRepository.deleteByEmail(email);
    }

    public ResponseEntity<String> updateDueDate(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User with this email:" + email + "not found");
        }

        user.setDuedate(LocalDate.now().plusDays(30));
        userRepository.save(user);
        return ResponseEntity.ok("Due date is updated");
    }

    public List<User> getUsers() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);
        return userRepository.findByPincode(user.getPincode());
    }

    public void updateUser(UserRequest userRequest, String email) {
        User user = userRepository.findByEmail(email);
        user.setName(userRequest.getName());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(userRequest.getRole());
        user.setEmail(userRequest.getEmail());
        user.setPincode(userRequest.getPincode());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        userRepository.save(user);
    }
}
