package com.smartpower;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentRepository paymentRepository;
    private final UserClients userClient;
    private final JwtServiceUtil jwtServiceUtil;

    public PaymentService(PaymentRepository paymentRepository, UserClients userClient, JwtServiceUtil jwtServiceUtil) {
        this.paymentRepository = paymentRepository;
        this.userClient = userClient;
        this.jwtServiceUtil = jwtServiceUtil;
    }

    public void makePayment(PaymentRequest request) {
        //Get the email from token
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpRequest = attr.getRequest();

        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        String email = jwtServiceUtil.extractEmail(token);

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
