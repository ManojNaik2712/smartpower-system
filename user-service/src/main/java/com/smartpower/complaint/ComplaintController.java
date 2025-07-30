package com.smartpower.complaint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @PostMapping("/complaint")
    public ResponseEntity<String> createComplaint(@RequestBody ComplaintRequest request) {
        complaintService.createComplaint(request);
        return ResponseEntity.ok("Complaint sent successfully");
    }

    @GetMapping("/get/complaint")
    public ResponseEntity<List<Complaint>> getComplaint(@RequestParam("email") String email) {
        List<Complaint> complaints = complaintService.getComplaint(email);
        return ResponseEntity.ok(complaints);
    }

    @GetMapping("/getAll/complaints")
    public ResponseEntity<List<Complaint>> getAllComplaints() {
        List<Complaint> complaints = complaintService.getAllComplaints();
        return ResponseEntity.ok(complaints);
    }
}
