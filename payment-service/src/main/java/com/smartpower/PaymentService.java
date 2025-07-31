package com.smartpower;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
        String token = jwtServiceUtil.getToken();
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

    public List<Payment> getPayment() {
        String token = jwtServiceUtil.getToken();
        String email = jwtServiceUtil.extractEmail(token);
        return paymentRepository.findByUserEmail(email);
    }
}
