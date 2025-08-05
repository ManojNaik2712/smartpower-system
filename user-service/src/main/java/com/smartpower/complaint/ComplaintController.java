package com.smartpower.complaint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    /**
     * Creates a new complaint based on the request data.
     *
     * @param request the complaint request containing details
     * @return a success response
     */
    @PostMapping("/complaint")
    public ResponseEntity<String> createComplaint(@RequestBody ComplaintRequest request) {
        complaintService.createComplaint(request);
        return ResponseEntity.ok("Complaint sent successfully");
    }

    /**
     * Retrieves complaints submitted by the currently authenticated user.
     *
     * @param authentication the authentication object containing the user details
     * @return list of complaints by the user
     */
    @GetMapping("/getmy/complaint")
    public ResponseEntity<List<Complaint>> getComplaint(Authentication authentication) {
        String email = authentication.getName();
        log.info("Fetching complaints for authenticated user: {}", email);
        List<Complaint> complaints = complaintService.getMyComplaint(email);
        log.info("Found {} complaints for user {}", complaints.size(), email);
        return ResponseEntity.ok(complaints);
    }

    /**
     * Retrieves complaints for a specific user by email.
     *
     * @param email the email of the user
     * @return list of complaints for the provided email
     */
    @GetMapping("/get/complaint")
    public ResponseEntity<List<Complaint>> getComplaint(@RequestParam("email") String email) {
        log.info("Fetching complaints for user with email: {}", email);
        List<Complaint> complaints = complaintService.getComplaint(email);
        log.info("Found {} complaints for email: {}", complaints.size(), email);
        return ResponseEntity.ok(complaints);
    }

    /**
     * Retrieves all complaints in the system.
     *
     * @return list of all complaints
     */
    @GetMapping("/getAll/complaints")
    public ResponseEntity<List<Complaint>> getAllComplaints() {
        log.info("Fetching all complaints in the system");
        List<Complaint> complaints = complaintService.getAllComplaints();
        log.info("Total complaints found: {}", complaints.size());
        return ResponseEntity.ok(complaints);
    }
}
