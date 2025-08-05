package com.smartpower;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Endpoint to make a payment
     *
     * @param request - Payment details in the request body
     * @return Confirmation message
     */
    @PostMapping("/payment")
    public ResponseEntity<String> pay(@RequestBody PaymentRequest request) {
        log.info("Received payment request for user: ");
        paymentService.makePayment(request);
        return ResponseEntity.ok("Payment done");
    }

    /**
     * Endpoint to fetch all payment records
     *
     * @return List of payments
     */
    @GetMapping("/get/payment")
    public List<Payment> getPayment() {
        log.info("Fetching all payment records");
        List<Payment> payments = paymentService.getPayment();
        log.info("Total payments found: {}", payments.size());
        return payments;
    }
}
