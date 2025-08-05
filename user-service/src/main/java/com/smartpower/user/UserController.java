package com.smartpower.user;

import com.smartpower.alert.AlertRequest;
import com.smartpower.outage.OutageMessage;
import com.smartpower.outage.OutageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for handling user-related operations.
 * Includes endpoints for user creation, retrieval, update, deletion,
 * sending alerts, and fetching outage messages.
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;
    private final OutageService outageService;
    private final UserRepository userRepository;

    public UserController(UserService userService, OutageService outageService, UserRepository userRepository) {
        this.userService = userService;
        this.outageService = outageService;
        this.userRepository = userRepository;
    }

    /**
     * Registers a new user in the system.
     *
     * @param userRequest The user details to be saved
     */
    @PostMapping("/saveUser")
    void createUser(@RequestBody UserRequest userRequest) {
        log.info("Creating user with email: {}", userRequest.getEmail());
        userService.createUser(userRequest);
        log.info("User created successfully");
    }

    /**
     * Retrieves a user based on their email.
     *
     * @param email The email of the user
     * @return The user data
     */
    @GetMapping("/getUser/{email}")
    public UserResponse getUser(@PathVariable("email") String email) {
        log.info("Fetching user with email: {}", email);
        return userService.getUser(email);
    }

    /**
     * Updates the profile of the currently authenticated user.
     *
     * @param userRequest The updated user data
     * @return HTTP 200 OK if update is successful
     */
    @PutMapping("/update/profile")
    public ResponseEntity<String> updateUser(@RequestBody UserRequest userRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Updating user profile for email: {}", email);
        userService.updateUser(userRequest, email);
        log.info("User profile updated successfully for email: {}", email);
        return ResponseEntity.ok("User updated succesfully");
    }

    /**
     * Deletes the currently authenticated user.
     *
     * @param authentication Spring Security authentication object
     * @return HTTP 200 OK if deletion is successful
     */
    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(Authentication authentication) {
        String email = authentication.getName();
        log.info("Deleting user with email: {}", email);
        userService.deleteUser(email);
        log.info("User deleted successfully: {}", email);
        return ResponseEntity.ok("user deleted succesfully");
    }

    /**
     * Sends an outage alert to users in a specific pincode.
     *
     * @param request Contains pincode, title, and content of the alert
     * @return HTTP 200 OK with confirmation message
     */
    @PostMapping("/send-alert")
    public ResponseEntity<String> sendAlert(@RequestBody AlertRequest request) {
        log.info("Sending outage alert to pincode: {}", request.getPincode());
        outageService.sendOutageMessage(request.getPincode(), request.getTitle(), request.getContent());
        log.info("Outage alert sent to pincode: {}", request.getPincode());
        return ResponseEntity.ok("Message sent to users in pincode:" + request.getPincode());
    }

    /**
     * Retrieves outage messages for the currently authenticated user's pincode.
     *
     * @param authentication Spring Security authentication object
     * @return List of outage messages
     */
    @GetMapping("/getMessage")
    public List<OutageMessage> getMessages(Authentication authentication) {
        String email = authentication.getName();
        log.info("Fetching outage messages for user: {}", email);
        User user = userRepository.findByEmail(email);
        List<OutageMessage> messages = outageService.getMessages(user.getPincode());
        log.info("Found {} outage messages for pincode: {}", messages.size(), user.getPincode());
        return messages;
    }

    /**
     * Updates the due date for a specific user.
     *
     * @param email The email of the user whose due date is to be updated
     * @return HTTP 200 OK with confirmation message
     */
    @PutMapping("/update-due-date")
    public ResponseEntity<String> updateDueDate(@RequestParam("email") String email) {
        log.info("Updating due date for user: {}", email);
        return userService.updateDueDate(email);
    }

    /**
     * Retrieves all users from the system.
     *
     * @return List of all users
     */
    @GetMapping("/getAllUser")
    public List<User> getUsers() {
        log.info("Fetching all users");
        return userService.getUsers();
    }
}
