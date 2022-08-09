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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Properties;

@Service
public class CybersourcePaymentService {
    private static String date = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT")));

    public String initiatePayment(double amount) throws ConfigException, IOException {

        String authenticationType;
        Properties merchantProp;
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
//        checkoutApiInitialization.setReferenceNumber(UUID.randomUUID().toString());
//        checkoutApiInitialization.setTransactionUUID(UUID.randomUUID().toString());

        checkoutApiInitialization.setReferenceNumber("989c8442-4de0-421e-936e-4aae1c2e7f93");
        checkoutApiInitialization.setTransactionUUID("da068362-3823-4b89-a3e7-118d6872611d");
        checkoutApiInitialization.setTransactionType("authorization, create_payment_token");
        checkoutApiInitialization.setPaymentMethod("card");
        checkoutApiInitialization.setCurrency("GBP");
        checkoutApiInitialization.setAmount("24.0");
        checkoutApiInitialization.setLocale("en");
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
//        checkoutApiInitialization.setOverrideCustomReceiptPage("http://localhost:8082/testing0804/1.php%20-%20WP%20Iframe%20PA&Token&Override%20URL/web/receipt.php");
        checkoutApiInitialization.setIgnoreAvs("true");
        checkoutApiInitialization.setIgnoreCvn("true");
//        checkoutApiInitialization.setPartnerSolutionId("");
        checkoutApiInitialization.setUnsignedFieldNames("transient_token");

        sessionRequest.setCheckoutApiInitialization(checkoutApiInitialization);
        String requestBody = new ObjectMapper().writeValueAsString(sessionRequest);
        merchantConfig.setRequestData(requestBody);
        merchantConfig.setRequestTarget("/microform/v2/sessions/");
        String url = "https://" + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
        Response response = processPayment(merchantConfig);
        return response.getResponseMessage();

    }

    public static RestTemplate getRestTemplate() {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setConnectTimeout(3000);
        simpleClientHttpRequestFactory.setReadTimeout(3000);
        return new RestTemplate(simpleClientHttpRequestFactory);
    }

    public static String generateDigest(String jsonPayload) throws NoSuchAlgorithmException {
        String bodyText = jsonPayload;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(bodyText.getBytes(StandardCharsets.UTF_8));
        byte[] digest = md.digest();
        return "SHA-256=" + Base64.getEncoder().encodeToString(digest);
    }

    public static String generateSignatureFromParams(String signatureParams) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeyException {
        String keyString = CybersourceConstants.restApiSharedSecret;
        String apiKey = CybersourceConstants.restApiKey;
        byte[] decodedKey = Base64.getDecoder().decode(keyString);
        SecretKey originalKey = new SecretKeySpec(decodedKey, "HmacSHA256");
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        hmacSha256.init(originalKey);
        hmacSha256.update(signatureParams.getBytes());
        byte[] HmachSha256DigestBytes = hmacSha256.doFinal();
        String signature = Base64.getEncoder().encodeToString(HmachSha256DigestBytes);
        return "keyid=\"" + apiKey + "\", algorithm=\"HmacSHA256\", headers=\"host date (request-target) digest v-c-merchant-id\", signature=\"" + signature + "\"";
    }


    static Response processPayment(MerchantConfig merchantConfig) throws IOException {

        HttpConnection connection = new HttpConnection(merchantConfig);
        return connection.httpConnection();


    }


    void restTemplateImpl(String requestBody) throws NoSuchAlgorithmException, InvalidKeyException {


        System.out.println("requestBody: " + requestBody);
        String digest = generateDigest(requestBody);
        System.out.println("digest: " + digest);
        String signatureParam = "host: apitest.cybersource.com\n date: " + date + "\n (request-target): post /microform/v2/sessions/" + "\ndigest: " + digest + "\n v-c-merchant-id: novacroft_sandbox";
        String signature = generateSignatureFromParams(signatureParam);
        System.out.println("\n");
        System.out.println("Signaure: " + signature);
        System.out.println("\n");
        String signatureHeader = signatureParam + "\n signature: " + signature;

        RestTemplate restTemplate = getRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Host", "apitest.cybersource.com");
        headers.set("Date", date);
        headers.set("Digest", digest);
        headers.set("v-c-merchant-id", "novacroft_sandbox");
        headers.set("Signature", signature);
        headers.set("User-Agent", "Mozilla/5.0");
//        headers.set("accept", "application/json");
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<String>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(CybersourceConstants.sessionURL, HttpMethod.POST, entity, String.class);

        String responseStr = response.getBody();
        System.out.println(responseStr);
    }

}

