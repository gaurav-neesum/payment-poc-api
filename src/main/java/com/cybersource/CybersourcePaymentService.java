package com.cybersource;

import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class CybersourcePaymentService {
    private static String date = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT")));
    String DATE_TIME_UTC = "yyyy-MM-dd HH:mm:ss z";

    public String initiatePayment(double amount) throws ConfigException, IOException {

        MerchantConfig merchantConfig = new MerchantConfig();
        merchantConfig.setMerchantID("novacroft_sandbox");
        merchantConfig.setMerchantKeyId(CybersourceConstants.restApiKey);
        merchantConfig.setMerchantSecretKey(CybersourceConstants.restApiSharedSecret);
        merchantConfig.setAuthenticationType("http_signature");
        merchantConfig.setRunEnvironment("apitest.cybersource.com");
        merchantConfig.setRequestType("POST");
        merchantConfig.setRequestHost("apitest.cybersource.com");


        SessionRequest sessionRequest = new SessionRequest();
        ArrayList<String> targetOrigins = new ArrayList<>();
        targetOrigins.add("http://localhost:8080");
        sessionRequest.setTargetOrigins(targetOrigins);

        CheckoutApiInitialization checkoutApiInitialization = new CheckoutApiInitialization();
        checkoutApiInitialization.setProfileId(CybersourceConstants.profileId);
        checkoutApiInitialization.setAccessKey(CybersourceConstants.accessKey);
        String uuid1 = UUID.randomUUID().toString();
        checkoutApiInitialization.setReferenceNumber(uuid1);
        checkoutApiInitialization.setTransactionUUID(uuid1);
        System.out.println("UUID: " + uuid1);

        checkoutApiInitialization.setTransactionType("authorization");
        checkoutApiInitialization.setCurrency("GBP");
        checkoutApiInitialization.setAmount(String.valueOf(amount));
        checkoutApiInitialization.setLocale("en");
        checkoutApiInitialization.setBillToForename("Bob");
        checkoutApiInitialization.setBillToSurname("Minion");
        checkoutApiInitialization.setBillToPhone("07899899987");
        checkoutApiInitialization.setBillToEmail("bobminion@gru.com");
        checkoutApiInitialization.setBillToAddressLine1("billToAddressLine1");
        checkoutApiInitialization.setBillToAddressCity("Nuneaton");
        checkoutApiInitialization.setBillToAddressPostalCode("CV11 6QS");
        //4111 1111 1111 1111
        checkoutApiInitialization.setBillToAddressCountry("GB");
        checkoutApiInitialization.setOverrideBackofficePostUrl("https://devmelaapi.gauribaba.com/check-payment?reason_code=100");
        checkoutApiInitialization.setOverrideCustomReceiptPage("http://localhost:5005/cb/check-payment");

        checkoutApiInitialization.setIgnoreAvs("false");
        checkoutApiInitialization.setIgnoreCvn("false");
        checkoutApiInitialization.setUnsignedFieldNames("transient_token");

        sessionRequest.setCheckoutApiInitialization(checkoutApiInitialization);
        String requestBody = new ObjectMapper().writeValueAsString(sessionRequest);
        merchantConfig.setRequestData(requestBody);
        merchantConfig.setRequestTarget("/microform/v2/sessions/");
        Response response = processPayment(merchantConfig);
        return response.getResponseMessage();

    }


    static Response processPayment(MerchantConfig merchantConfig) throws IOException {

        HttpConnection connection = new HttpConnection(merchantConfig);
        return connection.httpConnection();


    }


}

