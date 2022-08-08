package com.judopayweb.judo_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.judopayweb.domain.PaymentProvider;
import com.judopayweb.dto.CardAddress;
import com.judopayweb.dto.JudopayRequest;
import com.judopayweb.dto.ThreeDSecure;
import com.judopayweb.dto.YourPaymentMetaData;
import com.judopayweb.dto.response.PaymentSessionResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.UUID;

public class JudopayRequestTest {

    public static void main(String[] args) throws IOException {
        String judoId = PaymentProvider.getJudoId();
        String token = PaymentProvider.getToken();
        String secret = PaymentProvider.getSecret();
        String paymentURL = PaymentProvider.getPaymentURL();

        CardAddress cardAddress = new CardAddress()
                .setAddress1("CardHolder House")
                .setAddress2("1 CardHolder Street")
                .setAddress3("CardHolder Area")
                .setTown("My Town")
                .setPostCode("AB12 2AS")
                .setCountryCode(325)
                .setCardHolderName("Gauri Baba");


        JudopayRequest judopayRequest = new JudopayRequest();
        judopayRequest.setJudoId(judoId)
                .setYourConsumerReference(UUID.randomUUID().toString())
                .setYourPaymentReference(UUID.randomUUID().toString())
                .setYourPaymentMetaData(new YourPaymentMetaData()
                        .setInternalId("123")
                        .setInternalLocationRef("Fexamble"))
                .setCurrency("GBP")
                .setAmount(19.90)
                .setCardAddress(cardAddress)
//                .setExpiryDate(LocalDateTime.now().atZone(ZoneId.of("Europe/London")).toString())
                .setPayByLink(false)
                .setJudoAccept(false)
                .setPaymentSuccessUrl("https://tfl-dev.applications-online.com:9000/application/payment/checkPayment")
                .setPaymentCancelUrl("https://tfl-dev.applications-online.com:9000/application/payment/checkPayment")
                .setEmailAddress("gaurav.shah@novacroft.com")
                .setMobileNumber("07429566625")
                .setPhoneCountryCode("44")
                .setThreeDSecure(new ThreeDSecure()
                        .setChallengeRequestIndicator("challengePreferred")
                        .setScaExemption("lowValue"))
                .setHideBillingInfo(true)
                .setHideReviewInfo(true);
        RestTemplate restTemplate = getRestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Api-Version", "6.16");
        headers.set("accept", "application/json");
        headers.set("Content-Type", "application/json");
        String tokenSecret = String.format("%s:%s", token, secret);
        String base63TokenSecret = Base64.getEncoder().encodeToString(tokenSecret.getBytes());
        String authorization = String.format("Basic %s", base63TokenSecret);
        headers.set("Authorization", authorization);


        HttpEntity<String> entity = new HttpEntity<String>(new ObjectMapper().writeValueAsString(judopayRequest), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(paymentURL, entity, String.class);

        String responseStr = response.getBody();
        System.out.println(responseStr);
        PaymentSessionResponse paymentSessionResponse = new ObjectMapper().readValue(responseStr, PaymentSessionResponse.class);
        System.out.println(paymentSessionResponse.getReference());

    }

    public static RestTemplate getRestTemplate() {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setConnectTimeout(3000);
        simpleClientHttpRequestFactory.setReadTimeout(3000);
        return new RestTemplate(simpleClientHttpRequestFactory);
    }
}
