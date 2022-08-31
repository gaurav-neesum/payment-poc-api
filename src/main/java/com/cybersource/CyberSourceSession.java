package com.cybersource;

import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.payloaddigest.PayloadDigest;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
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
import java.util.*;

public class CyberSourceSession {
    private static String date = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT")));
    private static Logger logger = LogManager.getLogger(PayloadDigest.class);

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException, ConfigException {

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

        Arrays.asList("profile_id", "access_key", "reference_number", "transaction_uuid", "transaction_type", "payment_method",
                "currency", "amount", "locale", "signed_date_time", "bill_to_forename", "bill_to_surname", "bill_to_phone", "bill_to_email",
                "bill_to_address_line1", "bill_to_address_city", "bill_to_address_state", "bill_to_address_postal_code", "bill_to_address_country",
                "override_backoffice_post_url", "override_custom_receipt_page", "ignore_avs", "ignore_cvn", "partner_solution_id", "signed_field_names", "unsigned_field_names");

        SessionRequest sessionRequest = new SessionRequest();
        ArrayList<String> targetOrigins = new ArrayList<>();
        targetOrigins.add("http://localhost:8082");
        sessionRequest.setTargetOrigins(targetOrigins);

        CheckoutApiInitialization checkoutApiInitialization = new CheckoutApiInitialization();
        checkoutApiInitialization.setProfileId(CybersourceConstants.profileId);
        checkoutApiInitialization.setAccessKey(CybersourceConstants.accessKey);
        checkoutApiInitialization.setReferenceNumber(UUID.randomUUID().toString());
        checkoutApiInitialization.setTransactionUUID(UUID.randomUUID().toString());
        checkoutApiInitialization.setTransactionType("authorization,create_payment_token");
//        checkoutApiInitialization.setPaymentMethod("card");
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
//        checkoutApiInitialization.setBillToAddressState("CA");
        checkoutApiInitialization.setBillToAddressPostalCode("CV10 0IS");
        checkoutApiInitialization.setBillToAddressCountry("UK");
        checkoutApiInitialization.setOverrideBackofficePostUrl("https://localhost:8080/check-payment");
        checkoutApiInitialization.setOverrideCustomReceiptPage("http://localhost:8080/receipt");
        checkoutApiInitialization.setIgnoreAvs("true");
        checkoutApiInitialization.setIgnoreCvn("true");
//        checkoutApiInitialization.setPartnerSolutionId("");
        checkoutApiInitialization.setUnsignedFieldNames("transient_token");

        sessionRequest.setCheckoutApiInitialization(checkoutApiInitialization);
        String requestBody = new ObjectMapper().writeValueAsString(sessionRequest);
        merchantConfig.setRequestData(requestBody);
        System.out.println("From Own Implementation: " + generateDigest2(requestBody));
        System.out.println("From Cybersource Copy: " + generateDigest(requestBody));
        merchantConfig.setRequestTarget("/microform/v2/sessions/");
        String url = "https://" + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();
        Response response = processPayment(merchantConfig);
//        restTemplateImpl(requestBody);
        String code = response.getResponseCode();


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

    public static String generateDigest2(String jsonPayload) {

        MessageDigest digestString;
        byte[] digestBytes = null;
        try {
            digestString = MessageDigest.getInstance("SHA-256");
            digestBytes = digestString.digest(jsonPayload.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            logger.fatal(GlobalLabelParameters.DIGEST_GEN_FAILED);
            logger.error(e);
            return null;
        }
        String bluePrint = Base64.getEncoder().encodeToString(digestBytes);
        bluePrint = "SHA-256" + "=" + bluePrint;
        return bluePrint;
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


    static void restTemplateImpl(String requestBody) throws NoSuchAlgorithmException, InvalidKeyException {


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
//keyid="291c53de-b6cf-4356-ab75-a9e52231e674",algorithm="HmacSHA256",headers="host date (request-target) digest v-c-merchant-id",signature="JY0xhbL90ckSZhbIl4158nBxHHuvDDL4zWW6twbKkIM="
//keyid="291c53de-b6cf-4356-ab75-a9e52231e674",algorithm="HmacSHA256",headers="host date (request-target) digest v-c-merchant-id",signature="fMTDQjRhfcnJ3+BY1hUfoEKoNlH3n/JGp4B2WLUSBRI="
/**
 * host: apitest.cybersource.com
 * date: Thu, 4 Aug 2022 15:23:57 GMT
 * (request-target): post /microform/v2/sessions/
 * digest: SHA-256=8+dQLtUO/+Z3ZVAiF7vecwYdFJ9tmsTTplD0cYfaqac=
 * v-c-merchant-id: novacroft_sandbox
 */