package com.cybersource;

import com.cybersource.authsdk.core.ConfigException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

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

    @PostMapping("/check-payment")
    public ResponseEntity<?> checkPayment(HttpServletRequest servletRequest) throws JsonProcessingException {
        Iterator<String> paramIterable = servletRequest.getParameterNames().asIterator();

        List<String> paramNames = new ArrayList<>();
        while (paramIterable.hasNext()) {
            paramNames.add(paramIterable.next());
        }

        Map<String, Object> receiptBody = new HashMap<>();
        for (String param : paramNames) {
            receiptBody.put(param, servletRequest.getParameter(param));
        }
//        boolean successfulPayment = cybersourcePaymentService

        String body = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(receiptBody);

        System.out.println(body);
//        return ResponseEntity.ok(body);
        return ResponseEntity.ok("<div>\n" +
                "<label style=\"background-color: #b0efe6\">Payment Successful</label>\n" +
                "</div>");
    }

    @GetMapping("/ola")
    public ResponseEntity<?> getMethod() {
        return ResponseEntity.ok("waalaaa");
    }

    //todo expecting a token in the request body from the UI
    @PostMapping("/transient")
    public ResponseEntity<?> postMethod(@RequestBody Map<String, Object> obj) {

        return ResponseEntity.ok("");
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
