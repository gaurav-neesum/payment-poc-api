package com.cybersource;

import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.test.PaulinaCybersourceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
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
    public ResponseEntity<?> startPayment(@RequestParam Double amount) throws IOException, ConfigException {
        String jwt = cybersourcePaymentService.initiatePayment(amount);
        System.out.println(jwt);
        return ResponseEntity.ok(jwt);
    }

    @GetMapping("/test/start-payment/{amount}")
    public ResponseEntity<?> testPay(@PathVariable Double amount) throws IOException, NoSuchAlgorithmException, InvalidKeyException, ConfigException {
        String jwt = cybersourcePaymentService.initiatePaymentWithRestTemplate(amount);
        return ResponseEntity.ok(PaulinaCybersourceService.getToken());
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

        Enumeration<String> headerEnumeration = servletRequest.getHeaderNames();
        List<String> headers = new ArrayList<>();
        while (headerEnumeration.hasMoreElements()){
            headers.add(headerEnumeration.nextElement());
        }
        Map<String, Object> headerKeyVal = new HashMap<>();
        for(String header : headers){
            headerKeyVal.put(header, servletRequest.getHeader(header));
        }

//        boolean successfulPayment = cybersourcePaymentService

        String body = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(receiptBody);

        System.out.println(body);

        return ResponseEntity.ok("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<div>\n" +
                "  <form id=\"receipt-redirect\" action=\"http://localhost:8080/receipt\"></form>\n" +
                "</div>\n" +
                "<script>\n" +
                "  document.getElementById('receipt-redirect').submit();\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>\n");
    }

    @GetMapping("/ola")
    public ResponseEntity<?> getMethod() {
        return ResponseEntity.ok("waalaaa");
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
