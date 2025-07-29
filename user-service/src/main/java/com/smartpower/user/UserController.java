package com.smartpower.user;

import com.smartpower.alert.AlertRequest;
import com.smartpower.outage.OutageMessage;
import com.smartpower.outage.OutageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final OutageService outageService;
    private final UserRepository userRepository;

    public UserController(UserService userService, OutageService outageService, UserRepository userRepository) {
        this.userService = userService;
        this.outageService = outageService;
        this.userRepository = userRepository;
    }

    @PostMapping("/saveUser")
    void createUser(@RequestBody UserRequest userRequest) {
        userService.createUser(userRequest);
    }

    @GetMapping("/getUser/{email}")
    public UserResponse getUser(@PathVariable("email") String email) {
        return userService.getUser(email);
    }
    @PutMapping("/update/profile")
    public ResponseEntity<String> updateUser(@RequestBody UserRequest userRequest){
        String email= SecurityContextHolder.getContext().getAuthentication().getName();
        userService.updateUser(userRequest,email);
        return ResponseEntity.ok("User updated succesfully");
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser() {
        String email= SecurityContextHolder.getContext().getAuthentication().getName();
        userService.deleteUser(email);
        return ResponseEntity.ok("user deleted succesfully");
    }

    @PostMapping("/send-alert")
    public ResponseEntity<String> sendAlert(@RequestBody AlertRequest request) {
        outageService.sendOutageMessage(request.getPincode(), request.getTitle(), request.getContent());
        return ResponseEntity.ok("Message sent to users in pincode:" + request.getPincode());
    }

    @GetMapping("/getMessage")
    public List<OutageMessage> getMessages(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        return outageService.getMessages(user.getPincode());
    }

    @PutMapping("/update-due-date")
    public ResponseEntity<String> updateDueDate(@RequestParam("email") String email) {
        return userService.updateDueDate(email);
    }

    @GetMapping("/getAllUser")
    public List<User> getUsers(){
        return userService.getUsers();
    }
}
