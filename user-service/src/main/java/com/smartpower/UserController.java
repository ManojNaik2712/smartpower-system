package com.smartpower;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/saveUser")
    void createUser(@RequestBody UserRequest userRequest) {
        userService.createUser(userRequest);
    }

    @GetMapping("/getUser/{email}")
    public UserResponse getUser(@PathVariable("email") String email){
        return userService.getUser(email);

    }
    @DeleteMapping("/deleteUser/{email}")
    public String deleteUser(@PathVariable("email") String email){
        userService.deleteUser(email);
        return "deleted succesfully";
    }
}
