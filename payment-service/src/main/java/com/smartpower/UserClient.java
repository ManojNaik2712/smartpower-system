package com.smartpower;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-ervice", url = "${user-service.url}")
public interface UserClient {

    @PutMapping("/user/update-due-date")
    void updateDueDate(@RequestParam String email);
}
