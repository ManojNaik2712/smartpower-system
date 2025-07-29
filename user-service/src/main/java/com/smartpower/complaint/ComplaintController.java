package com.smartpower.complaint;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/user")
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @PostMapping("/complaint")
    public ResponseEntity<String> createComplaint(@RequestBody ComplaintRequest request){
        complaintService.createComplaint(request);
        return ResponseEntity.ok("Complaint sent successfully");
    }

    @GetMapping("/get/complaint")
    public ResponseEntity<Complaint> getComplaint(@RequestParam String email){
        Complaint complaint=complaintService.getComplaint(email);
        return ResponseEntity.ok(complaint);
    }
    @GetMapping("/getAll/complaints")
    public ResponseEntity<List<Complaint>> getAllComplaints(){
        List<Complaint> complaints=complaintService.getAllComplaints();
        return ResponseEntity.ok(complaints);
    }
}
