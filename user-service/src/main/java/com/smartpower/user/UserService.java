package com.smartpower.user;

import com.smartpower.AuthException.AdminSecretMismatchException;
import com.smartpower.Role;
import com.smartpower.UserException.DuplicateUserException;
import com.smartpower.UserException.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service layer responsible for user-related business logic,
 * such as registration, update, deletion, and lookup.
 */
@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.secret}")
    private String adminSecret;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new user and saves it to the database.
     * Validates admin secret for admin role and checks for duplicate users.
     *
     * @param userRequest the user data to register
     * @throws AdminSecretMismatchException if admin secret is incorrect
     * @throws DuplicateUserException       if user already exists with given email
     */
    public void createUser(UserRequest userRequest) {
        log.info("Creating user with email: {}", userRequest.getEmail());

        if (userRequest.getRole().equals(Role.ADMIN)) {
            if (userRequest.getAdminSecret() == null || !userRequest.getAdminSecret().equals(adminSecret)) {
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

        log.info("User successfully created and saved: {}", user.getEmail());
    }


    public UserResponse getUser(String email) {
        log.info("Fetching user with email: {}", email);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        log.info("User found: {}", email);
        return UserResponse.builder().email(user.getEmail()).name(user.getName()).role(user.getRole()).password(user.getPassword()).phoneNumber(user.getPhoneNumber()).pincode(user.getPincode()).build();
    }

    public void deleteUser(String email) {
        log.info("Deleting user with email: {}", email);
        userRepository.deleteByEmail(email);
        log.info("User deleted: {}", email);
    }

    /**
     * Updates the due date for a specific user to 30 days from now.
     *
     * @param email the email of the user
     * @return ResponseEntity with success message
     * @throws UserNotFoundException if user is not found
     */
    public ResponseEntity<String> updateDueDate(String email) {
        log.info("Updating due date for user: {}", email);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User with this email:" + email + "not found");
        }

        user.setDuedate(LocalDate.now().plusDays(30));
        userRepository.save(user);
        log.info("Due date updated for user: {}", email);
        return ResponseEntity.ok("Due date is updated");
    }

    /**
     * Retrieves all users that share the same pincode as the currently authenticated user.
     *
     * @return list of users in the same pincode
     */
    public List<User> getUsers() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Fetching users by pincode for user: {}", email);
        User user = userRepository.findByEmail(email);
        List<User> users = userRepository.findByPincode(user.getPincode());
        log.info("Found {} users in pincode: {}", users.size(), user.getPincode());
        return users;
    }

    public void updateUser(UserRequest userRequest, String email) {
        log.info("Updating user with email: {}", email);
        User user = userRepository.findByEmail(email);
        user.setName(userRequest.getName());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(userRequest.getRole());
        user.setEmail(userRequest.getEmail());
        user.setPincode(userRequest.getPincode());
        user.setPhoneNumber(userRequest.getPhoneNumber());

        userRepository.save(user);
        log.info("User updated and saved: {}", email);
    }
}
