package com.cybersource;

import com.cybersource.authsdk.core.ConfigException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin()
@RequestMapping("/cb")
public class CybersourcePaymentController {
    private final CybersourcePaymentService cybersourcePaymentService;

    public CybersourcePaymentController(CybersourcePaymentService cybersourcePaymentService) {
        this.cybersourcePaymentService = cybersourcePaymentService;
    }

    @PostMapping("/start-payment")
    public ResponseEntity<?> pay(@RequestParam Double amount) throws IOException, ConfigException {
        String jwt = cybersourcePaymentService.initiatePayment(amount);
        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/sign")
    public ResponseEntity<?> sign(@RequestBody Map<String, Object> params) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        Object amountObj = params.get("amount");
        double amt;
        if (amountObj instanceof String) {
            amt = Double.parseDouble((String) params.get("amount"));
        } else if (amountObj instanceof Number) {
            amt = (double) amountObj;
        } else {
            throw new RuntimeException("Invalid amaout");
        }

        List<KeyValueResponse> keyValueResponses = CyberSourceSecurity.getAllKeyValueForPayment(amt);
        return ResponseEntity.ok(keyValueResponses);
    }
}
