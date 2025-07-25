package com.smartpower;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void createUser(UserRequest userRequest) {
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
}
