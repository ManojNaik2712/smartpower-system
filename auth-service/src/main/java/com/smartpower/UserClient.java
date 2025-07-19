package com.smartpower;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "${user-service.url}")
public interface UserClient {

    @PostMapping("/user/saveUser")
    void saveUser(@RequestBody UserRequest userRequest);

    @GetMapping("/user/getUser/{email}")
    UserResponse getUser(@PathVariable("email") String email);

}
