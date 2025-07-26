package com.smartpower;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final JwtService jwtService;
    private final UserClient userClient;

    public PaymentService(PaymentRepository paymentRepository, JwtService jwtService, UserClient userClient) {
        this.paymentRepository = paymentRepository;
        this.jwtService = jwtService;
        this.userClient = userClient;
    }

    public void makePayment(PaymentRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Payment payment = new Payment();
        payment.setAmount(request.getAmount());
        payment.setUserEmail(email);
        payment.setPaymentDate(LocalDate.now());
        payment.setMethod(request.getMethod());
        payment.setStatus("PAID");
        paymentRepository.save(payment);

        userClient.updateDueDate(email);
    }
}
