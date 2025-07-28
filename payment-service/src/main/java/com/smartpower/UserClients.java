package com.smartpower;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "${user-service.url}",configuration = FeignClientConfig.class)
public interface UserClients {

    @PutMapping("/user/update-due-date")
    ResponseEntity<String> updateDueDate(@RequestParam("email") String email);
}
