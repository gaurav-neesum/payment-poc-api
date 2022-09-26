package com.cybersource;

import com.cybersource.authsdk.core.ConfigException;
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
    public ResponseEntity<?> pay(@RequestParam Double amount) throws IOException, ConfigException {
        String jwt = cybersourcePaymentService.initiatePayment(amount);
        System.out.println(jwt);
//        jwt = "\"eyJraWQiOiJ6dSIsImFsZyI6IlJTMjU2In0.eyJmbHgiOnsicGF0aCI6Ii9mbGV4L3YyL3Rva2VucyIsImRhdGEiOiJ6dU5DeEpnRmYwVkhaWGIrZ0Q0STFSQUFFUHFsd2FwR09mZTZkcUQ1NzBjQ1hUcTU2VFltSnBhYjdFcm53TXVvVmFjejgxY21zVjNoK2hNS3BqQUF1eUVncDJQNFU3YkZ6cFBzYTJTY3hKTldMRFVpQS9KeGxHUG5HQmJkOEJPVTF4MFgiLCJvcmlnaW4iOiJodHRwczovL3Rlc3RmbGV4LmN5YmVyc291cmNlLmNvbSIsImp3ayI6eyJrdHkiOiJSU0EiLCJlIjoiQVFBQiIsInVzZSI6ImVuYyIsIm4iOiJsdndqdnNETHdfN3lRZ0ZRVnhha3JwdWdiaW8tR2NPaDU3QUJlcmZfUWVvM0t1S0dPSERFY1BZem5aenZ3bm1oYXBSR0p0UXZ3c3hsd1I5TEREa3Q0WTM2S1VOTENOV3E1dnlsbWxoaVBKUk51dV9PVGplai0tUGFuVTFaS1ZwRnVzeU1Mb3l5OS0xQlNyR2lTM2YteW9NOUZMTjNWajdidm5fMjZRN1gzSjhBMmV6aGVKS3B6QVRXYzVvYURLUm11am5URm9VU3ZXTGI0Sm10S2JLNW03MnVCUzZ5bXVOSGN1ZG9WZzNjMU5wSEN3MVhpOVVnWk5fVGhiVGc1cWZsSHN6OFpFRldkUEhRdl9lM1U3WHVoR2lCa2REQ0JmNE56bHRQR1ZqektHUjZkSjR2UHNMZnFLT1pZYkp1V05hWXdNeklodFVINHRMX1k4OUp0cFR0TlEiLCJraWQiOiIwOGVCd2FZck9WdUR5SzBNcEZrR1g3WlRmNmdYSlY2VCJ9fSwiY3R4IjpbeyJkYXRhIjp7ImNsaWVudExpYnJhcnkiOiJodHRwczovL3Rlc3RmbGV4LmN5YmVyc291cmNlLmNvbS9taWNyb2Zvcm0vYnVuZGxlL3YxL2ZsZXgtbWljcm9mb3JtLm1pbi5qcyIsInRhcmdldE9yaWdpbnMiOlsiaHR0cDovL2xvY2FsaG9zdDo4MDgwIl0sIm1mT3JpZ2luIjoiaHR0cHM6Ly90ZXN0ZmxleC5jeWJlcnNvdXJjZS5jb20ifSwidHlwZSI6Im1mLTEuMC4wIn0seyJkYXRhIjp7ImNoZWNrb3V0QXBpSW5pdGlhbGl6YXRpb24iOnsicHJvZmlsZV9pZCI6IkQ2NjYzM0M0LUI5RDQtNDYxQi05QjM5LTlEMTQ3OTBEQkY3QyIsImFjY2Vzc19rZXkiOiJiMmI0OTZhM2EwYjQzZTVjYmEyZWNjOWEwNWUxYjhkZSIsInJlZmVyZW5jZV9udW1iZXIiOiI2OWNlYjc0Yy05ODdhLTQxZmItODg3OS1mZDcyOTUzOWZjZjkiLCJ0cmFuc2FjdGlvbl91dWlkIjoiNjljZWI3NGMtOTg3YS00MWZiLTg4NzktZmQ3Mjk1MzlmY2Y5IiwidHJhbnNhY3Rpb25fdHlwZSI6ImF1dGhvcml6YXRpb24iLCJjdXJyZW5jeSI6IkdCUCIsImFtb3VudCI6IjEyLjIyIiwibG9jYWxlIjoiZW4iLCJiaWxsX3RvX2ZvcmVuYW1lIjoiR2F1cmF2IiwiYmlsbF90b19zdXJuYW1lIjoiU2hhaCIsImJpbGxfdG9fcGhvbmUiOiIwNzg5OTg5OTk4NyIsImJpbGxfdG9fZW1haWwiOiJnMDkuc2hhaEBnbWFpbC5jb20iLCJiaWxsX3RvX2FkZHJlc3NfbGluZTEiOiIxIE15IEFwYXJ0bWVudCIsImJpbGxfdG9fYWRkcmVzc19jaXR5IjoiTnVuZWF0b24iLCJiaWxsX3RvX2FkZHJlc3NfcG9zdGFsX2NvZGUiOiJDVjExIDZRUyIsImJpbGxfdG9fYWRkcmVzc19jb3VudHJ5IjoiR0IiLCJvdmVycmlkZV9iYWNrb2ZmaWNlX3Bvc3RfdXJsIjoiaHR0cHM6Ly9kZXZtZWxhYXBpLmdhdXJpYmFiYS5jb20vY2hlY2stcGF5bWVudD9yZWFzb25fY29kZVx1MDAzZDEwMCIsIm92ZXJyaWRlX2N1c3RvbV9yZWNlaXB0X3BhZ2UiOiJodHRwOi8vbG9jYWxob3N0OjUwMDUvY2IvY2hlY2stcGF5bWVudCIsImlnbm9yZV9hdnMiOiJmYWxzZSIsImlnbm9yZV9jdm4iOiJmYWxzZSIsInVuc2lnbmVkX2ZpZWxkX25hbWVzIjoidHJhbnNpZW50X3Rva2VuIn19LCJ0eXBlIjoic2EtMS4wLjAifV0sImlzcyI6IkZsZXggQVBJIiwiZXhwIjoxNjYzMzU0MTY4LCJpYXQiOjE2NjMzNTMyNjgsImp0aSI6Ikc4bFJlcHFZbmRaaEI2elMifQ.GS1loJ5Oao0kgMbn6FeGOE9LEeh5HgLL72LWU3Um5yIunEcFcRpZEzWM55p0YGVSPA9Nuu5FdmbFN6iDEjRQ6hda8XVdhxoY2p2S-SzjNqgF8B1U_V-vPrk6BRgKUAWgBT112lGF6T0uLUOl2MnOKthcmly9fRP0-ximDsgku2FT5rFyipPbNRI3gydu6HG7yRwwkQDUD37LO1ETRTOYSEpezbD3ENsfP-WHHgB5J5n8HDT8kQWms49jLM2PFINF1qvypoBcY_stRuq90aZ0pz3EQaoi1y5BNHctxTUUClGWJ8Q7ir2xY4QUSNsFUurSVanYD99YR_EX_yV0X6-T3g\"";
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
//        RestTemplate restTemplate = new RestTemplate();
//        String url = "http://localhost:8080/receipt";
//        URI uri = URI.create(url);
//        restTemplate.getForObject(url, Void.class);
        return ResponseEntity.ok("<div>\n" +
                "  <label style=\"background-color: #b0efe6\">Payment Successful</label>\n" +
                "  <form action=\"http://localhost:8080/receipt\">\n" +
                "    <input type=\"submit\" value=\"Return to your home\"/>\n" +
                "  </form>\n" +
                "</div>");
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
