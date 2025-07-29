package com.smartpower.complaint;

import com.smartpower.Role;
import com.smartpower.user.User;
import com.smartpower.user.UserRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComplaintService {

    private final UserRepository userRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ComplaintRepository repository;

    public ComplaintService(UserRepository userRepository, KafkaTemplate<String, Object> kafkaTemplate, ComplaintRepository repository) {
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.repository = repository;
    }

    public void createComplaint(ComplaintRequest request) {

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail);
        Optional<User> admin = userRepository.findFirstByPincodeAndRole(user.getPincode(), Role.ADMIN);

        ComplaintEvent complaintEvent = new ComplaintEvent(request.getSubject(), request.getMessage(),
                user.getUsername(), userEmail, admin.get().getEmail());

        kafkaTemplate.send("complaint-topic", complaintEvent);

        Complaint complaint = Complaint.builder()
                .subject(request.getSubject())
                .message(request.getMessage())
                .userName(user.getName())
                .userEmail(userEmail)
                .pincode(user.getPincode()).build();

        repository.save(complaint);
    }

    public Complaint getComplaint(String email) {
        return repository.findByUserEmail(email);
    }

    public List<Complaint> getAllComplaints() {
        return repository.findAll();
    }
}
