package com.smartpower;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.PublicKey;
import java.util.List;

@RestController
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/payment")
    public ResponseEntity<String> pay(@RequestBody PaymentRequest request) {
        paymentService.makePayment(request);
        return ResponseEntity.ok("Payment done");
    }

    @GetMapping("/get/Payment")
    public List<Payment> getPayment(){
         return paymentService.getPayment();
    }
}
