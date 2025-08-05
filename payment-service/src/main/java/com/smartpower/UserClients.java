package com.smartpower;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign client interface for communicating with the user-service.
 */
@FeignClient(name = "user-service", url = "${user-service.url}", configuration = FeignClientConfig.class)
public interface UserClients {

    /**
     * Sends a request to the user-service to update the due date for the specified user.
     *
     * @param email The email of the user whose due date needs to be updated.
     * @return ResponseEntity indicating the result of the operation.
     */
    @PutMapping("/user/update-due-date")
    ResponseEntity<String> updateDueDate(@RequestParam("email") String email);
}
