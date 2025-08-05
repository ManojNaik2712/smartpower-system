package com.smartpower;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//Feign client interface for communicating with the User Service.
@FeignClient(name = "user-service", url = "${user-service.url}")
public interface UserClient {

    /**
     * Calls the user-service to save a new user.
     *
     * @param userRequest The user details to be saved
     */
    @PostMapping("/user/saveUser")
    void saveUser(@RequestBody UserRequest userRequest);

    /**
     * Retrieves user details by email from user-service.
     *
     * @param email The email of the user to fetch
     * @return UserResponse containing user details
     */
    @GetMapping("/user/getUser/{email}")
    UserResponse getUser(@PathVariable("email") String email);

}
