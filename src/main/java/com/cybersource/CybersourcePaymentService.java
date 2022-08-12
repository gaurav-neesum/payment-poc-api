package com.cybersource;

import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Properties;
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
        checkoutApiInitialization.setReferenceNumber(UUID.randomUUID().toString());
        checkoutApiInitialization.setTransactionUUID(UUID.randomUUID().toString());

        checkoutApiInitialization.setTransactionType("authorization,create_payment_token");
        checkoutApiInitialization.setPaymentMethod("card");
        checkoutApiInitialization.setCurrency("GBP");
        checkoutApiInitialization.setAmount("24.0");
        checkoutApiInitialization.setLocale("en");
        checkoutApiInitialization.setSignedDateTime(LocalDateTime.now().atZone(ZoneId.of("Z")).toString());
        checkoutApiInitialization.setBillToForename("Gaurav");
        checkoutApiInitialization.setBillToSurname("Shah");
        checkoutApiInitialization.setBillToPhone("07899899987");
        checkoutApiInitialization.setBillToEmail("g09.shah@gmail.com");
        checkoutApiInitialization.setBillToAddressLine1("1 My Apartment");
        checkoutApiInitialization.setBillToAddressCity("Nuneaton");
        checkoutApiInitialization.setBillToAddressState("CA");
        checkoutApiInitialization.setBillToAddressPostalCode("CV10 0IS");
        checkoutApiInitialization.setBillToAddressCountry("UK");
        checkoutApiInitialization.setOverrideBackofficePostUrl("https://webhook.site/aef5e955-9a59-44b3-a186-f9a42d23181a");
        checkoutApiInitialization.setOverrideCustomReceiptPage("http://localhost:8080/web/receipt");
        checkoutApiInitialization.setIgnoreAvs("false");
        checkoutApiInitialization.setIgnoreCvn("false");
        String signedFieldNames = "profile_id,access_key,reference_number,transaction_uuid,transaction_type,payment_method,currency,amount,locale,signed_date_time,bill_to_forename,bill_to_surname,bill_to_phone,bill_to_email,bill_to_address_line1,bill_to_address_city,bill_to_address_state,bill_to_address_postal_code,bill_to_address_country,override_backoffice_post_url,override_custom_receipt_page,ignore_avs,ignore_cvn,partner_solution_id,signed_field_names,unsigned_field_names";
        checkoutApiInitialization.setSignedFieldNames(signedFieldNames);

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

