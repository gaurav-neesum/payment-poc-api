package com.judopayweb;

import com.judopayweb.dto.response.PaymentSessionResponse;
import com.judopayweb.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@CrossOrigin()
public class JudopayWebPaymentController {
    private final PaymentService  paymentService;

    public JudopayWebPaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/start-payment")
    public ResponseEntity<?> pay(@RequestParam Double amount) throws IOException {
        PaymentSessionResponse response = paymentService.initiatePayment(amount);
        return ResponseEntity.ok(response);
    }
}
