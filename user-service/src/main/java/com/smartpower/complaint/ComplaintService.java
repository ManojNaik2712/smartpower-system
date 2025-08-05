package com.smartpower.complaint;

import com.smartpower.ComplaintEvent;
import com.smartpower.Role;
import com.smartpower.user.User;
import com.smartpower.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ComplaintService {

    private final UserRepository userRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ComplaintRepository repository;

    public ComplaintService(UserRepository userRepository, KafkaTemplate<String, Object> kafkaTemplate, ComplaintRepository repository) {
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.repository = repository;
    }

    /**
     * Creates a complaint and sends a Kafka event to the relevant admin based on user's pincode.
     */
    public void createComplaint(ComplaintRequest request) {
        // Get the currently authenticated user's email
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail);
        log.info("Creating complaint for user: {}", userEmail);

        // Find the admin with the same pincode
        Optional<User> adminOptional = userRepository.findFirstByPincodeAndRole(user.getPincode(), Role.ADMIN);

        if (!adminOptional.isPresent()) {
            throw new RuntimeException("Admin not found");
        }
        User admin = adminOptional.get();
        log.info("Admin found for complaint: {}", admin.getEmail());

        // Create and send Kafka event to notify the admin
        ComplaintEvent complaintEvent = new ComplaintEvent(
                request.getSubject(),
                request.getMessage(),
                user.getUsername(),
                userEmail,
                admin.getEmail()
        );

        kafkaTemplate.send("complaint-topic", complaintEvent);
        log.info("Complaint event sent to Kafka for admin: {}", admin.getEmail());

        // Save the complaint in the database
        Complaint complaint = Complaint.builder()
                .subject(request.getSubject())
                .message(request.getMessage())
                .userName(user.getName())
                .userEmail(userEmail)
                .pincode(user.getPincode()).build();

        repository.save(complaint);
        log.info("Complaint saved in database for user: {}", userEmail);
    }

    /**
     * Fetch complaints for a specific user by email.
     */
    public List<Complaint> getComplaint(String email) {
        log.info("Fetching complaints for user: {}", email);
        return repository.findByUserEmail(email);
    }

    /**
     * Fetch all complaints. Intended for admin access.
     */
    public List<Complaint> getAllComplaints() {
        log.info("Fetching all complaints from database");
        return repository.findAll();
    }

    /**
     * Fetches the complaints of logged-in user
     */
    public List<Complaint> getMyComplaint(String email) {
        return repository.findByUserEmail(email);
    }
}
