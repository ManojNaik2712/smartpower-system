package com.smartpower;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


/**
 * Service class responsible for handling payment-related operations.
 * This includes making payments and retrieving payment history for a user.
 */
@Service
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserClients userClient;
    private final JwtServiceUtil jwtServiceUtil;

    public PaymentService(PaymentRepository paymentRepository, UserClients userClient, JwtServiceUtil jwtServiceUtil) {
        this.paymentRepository = paymentRepository;
        this.userClient = userClient;
        this.jwtServiceUtil = jwtServiceUtil;

    }

    /**
     * Makes a payment for the currently authenticated user.
     * Saves the payment details and updates the user's due date.
     *
     * @param request The payment request containing amount and payment method.
     */
    public void makePayment(PaymentRequest request) {
        String token = jwtServiceUtil.getToken();
        String email = jwtServiceUtil.extractEmail(token);

        log.info("Initiating payment for user: {}", email);

        Payment payment = new Payment();
        payment.setAmount(request.getAmount());
        payment.setUserEmail(email);
        payment.setPaymentDate(LocalDate.now());
        payment.setMethod(request.getMethod());
        payment.setStatus("PAID");

        paymentRepository.save(payment);
        log.info("Payment of ₹{} recorded for user: {}", request.getAmount(), email);

        userClient.updateDueDate(email);
        log.info("Due date updated for user: {}", email);
    }

    /**
     * Retrieves the list of payments made by the currently authenticated user.
     *
     * @return List of Payment records.
     */
    public List<Payment> getPayment() {
        String token = jwtServiceUtil.getToken();
        String email = jwtServiceUtil.extractEmail(token);
        log.info("Fetching payment history for user: {}", email);

        List<Payment> payments = paymentRepository.findByUserEmail(email);

        log.info("Found {} payment(s) for user: {}", payments.size(), email);
        return payments;
    }
}
