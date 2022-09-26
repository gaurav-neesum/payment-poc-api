package com.cybersource;

import com.cybersource.authsdk.core.Authorization;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.payloaddigest.PayloadDigest;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CyberSourceSession {
    public static final String APITEST_CYBERSOURCE_COM = "apitest.cybersource.com";
    public static final String NOVACROFT_SANDBOX = "novacroft_sandbox";
    public static final String HMAC_SHA_256 = "HmacSHA256";
    private static String date;

    private static RestTemplate restTemplate;

    static {
        restTemplate = getRestTemplate();
    }

    private static Logger logger = LogManager.getLogger(PayloadDigest.class);

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException, ConfigException {
        MerchantConfig merchantConfig = new MerchantConfig();
        merchantConfig.setMerchantID(NOVACROFT_SANDBOX);
        merchantConfig.setMerchantKeyId(CybersourceConstants.restApiKey);
        merchantConfig.setMerchantSecretKey(CybersourceConstants.restApiSharedSecret);
        merchantConfig.setAuthenticationType("http_signature");
        merchantConfig.setRunEnvironment("apitest.cybersource.com");
        merchantConfig.setRequestType("POST");
        merchantConfig.setRequestHost(APITEST_CYBERSOURCE_COM);


        SessionRequest sessionRequest = new SessionRequest();
        ArrayList<String> targetOrigins = new ArrayList<>();
        targetOrigins.add("http://localhost:8082");
        sessionRequest.setTargetOrigins(targetOrigins);

        String transactionUUID = UUID.randomUUID().toString();
        CheckoutApiInitialization checkoutApiInitialization = new CheckoutApiInitialization();
        checkoutApiInitialization.setProfileId(CybersourceConstants.profileId);
        checkoutApiInitialization.setAccessKey(CybersourceConstants.accessKey);
        checkoutApiInitialization.setReferenceNumber(UUID.randomUUID().toString());
        checkoutApiInitialization.setTransactionUUID(transactionUUID);
        checkoutApiInitialization.setTransactionType("authorization,create_payment_token");
        checkoutApiInitialization.setCurrency("GBP");
        checkoutApiInitialization.setAmount("24.0");
        checkoutApiInitialization.setLocale("en");
        checkoutApiInitialization.setBillToForename("Gaurav");
        checkoutApiInitialization.setBillToSurname("Shah");
        checkoutApiInitialization.setBillToPhone("07899899987");
        checkoutApiInitialization.setBillToEmail("g09.shah@gmail.com");
        checkoutApiInitialization.setBillToAddressLine1("1 My Apartment");
        checkoutApiInitialization.setBillToAddressCity("Nuneaton");
        checkoutApiInitialization.setBillToAddressPostalCode("CV10 0IS");
        checkoutApiInitialization.setBillToAddressCountry("UK");
        checkoutApiInitialization.setOverrideBackofficePostUrl("https://dev-api.melaxpress.com/check-payment");
        checkoutApiInitialization.setOverrideCustomReceiptPage("http://localhost:8080/receipt?transactionUUID=" + transactionUUID);
        checkoutApiInitialization.setIgnoreAvs("true");
        checkoutApiInitialization.setIgnoreCvn("true");
        checkoutApiInitialization.setUnsignedFieldNames("transient_token");

        sessionRequest.setCheckoutApiInitialization(checkoutApiInitialization);
        String requestBody = new ObjectMapper().writeValueAsString(sessionRequest);
        merchantConfig.setRequestData(requestBody);
        System.out.println("From Own Implementation: " + generateDigest2(requestBody));
        System.out.println("From Cybersource Copy: " + generateDigest(requestBody));
        merchantConfig.setRequestTarget("/microform/v2/sessions/");
        String url = "https://" + merchantConfig.getRequestHost() + merchantConfig.getRequestTarget();

        String signatureParam = "host: apitest.cybersource.com\n date: " + date + "\n (request-target): post /microform/v2/sessions/"
                + "\ndigest: " + generateDigest(requestBody) + "\n v-c-merchant-id: novacroft_sandbox";
        String signature = generateSignatureFromParams(signatureParam);
        System.out.println("From Own Signature: " + signature);
        Authorization authorization = new Authorization();
        System.out.println("Cybersource Signature: " + authorization.getToken(merchantConfig));
        //Response response = processPayment(merchantConfig);
        //restTemplateImpl(requestBody, merchantConfig);
        restTemplateImpl3(requestBody);
        // String code = response.getResponseCode();


    }

    public static String getNewDate() {
        CyberSourceSession.date = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT")));
        return CyberSourceSession.date;
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


    static void restTemplateImpl(String requestBody, MerchantConfig merchantConfig) throws NoSuchAlgorithmException, InvalidKeyException, ConfigException {


        System.out.println("requestBody: " + requestBody);
        String digest = generateDigest(requestBody);
        System.out.println("digest: " + digest);
        String signatureParam = "host: apitest.cybersource.com\n date: " + CyberSourceSession.getNewDate() + "\n (request-target): post /microform/v2/sessions/" + "\ndigest: " + digest + "\n v-c-merchant-id: novacroft_sandbox";
        String signature = generateSignatureFromParams(signatureParam);
        System.out.println("\n");
        System.out.println("Signaure: " + signature);
        System.out.println("\n");
        String signatureHeader = signatureParam + "\n signature: " + signature;
//        RestTemplate restTemplate = getRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("host", "apitest.cybersource.com");
        headers.set("v-c-date", CyberSourceSession.getNewDate());
        headers.set("digest", digest);
        headers.set("v-c-merchant-id", "novacroft_sandbox");
        Authorization authorization = new Authorization();
        headers.set("signature", signature);
        // headers.set("User-Agent", "Mozilla/5.0");
        // headers.set("accept", "application/json");
        headers.set("Content-type", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<String> entity = new HttpEntity<String>(merchantConfig.getRequestData(), headers);
        ResponseEntity<String> response = restTemplate.exchange(CybersourceConstants.sessionURL, HttpMethod.POST, entity, String.class);
        String responseStr = response.getBody();
        System.out.println(responseStr);
    }

    static void restTemplateImpl2(MerchantConfig merchantConfig) throws ConfigException {
        /**
         * Signature might be the cause of 401 Error
         * From Own Signature: keyid="291c53de-b6cf-4356-ab75-a9e52231e674", algorithm="HmacSHA256", headers="host (request-target) digest v-c-merchant-id", signature="S8UN2YN0x37CU40JyyFOJD51bKPtctzqFqQgH6NSaDY="
         * Cybersource Signature: keyid="291c53de-b6cf-4356-ab75-a9e52231e674", algorithm="HmacSHA256", headers="host date (request-target) digest v-c-merchant-id", signature="tPYRzGzOBtpMwrGbBM4iibFzW/dEgspj6dhAFAdeUF0="
         */
//        RestTemplate restTemplate = getRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("v-c-merchant-id", merchantConfig.getMerchantID());
        headers.set("host", merchantConfig.getRequestHost());

        PayloadDigest payloadDigest = new PayloadDigest(merchantConfig);
        headers.set("date", PropertiesUtil.getNewDate());

        String type = merchantConfig.getRequestType();
        if (!type.equalsIgnoreCase("GET"))
            headers.set("digest", payloadDigest.getDigest());

        Authorization authorization = new Authorization();
        headers.set("signature", authorization.getToken(merchantConfig));
        headers.set("Content-type", MediaType.APPLICATION_JSON_VALUE);


        HttpEntity<String> entity = new HttpEntity<String>(merchantConfig.getRequestData(), headers);
        ResponseEntity<String> response = restTemplate.exchange(CybersourceConstants.sessionURL,
                HttpMethod.POST, entity, String.class);
        String responseStr = response.getBody();
        System.out.println(responseStr);
    }


    // Call this in Controller in place of processPayment
    static Response restTemplateImpl3(String payload) throws NoSuchAlgorithmException, InvalidKeyException {

        HttpHeaders headers = new HttpHeaders();
        headers.set(CybersourceConstants.MERCHANT_ID, NOVACROFT_SANDBOX);
        headers.set(CybersourceConstants.HOST, APITEST_CYBERSOURCE_COM);
        headers.set(CybersourceConstants.DATE, CyberSourceSession.getNewDate());

        headers.set(CybersourceConstants.DIGEST, generateDigest(payload));
        headers.set(CybersourceConstants.SIGNATURE, generateSignature(payload, CybersourceConstants.restApiKey));
        headers.set(CybersourceConstants.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);


        HttpEntity<String> entity = new HttpEntity<String>(payload, headers);
        try {

            ResponseEntity<String> response = restTemplate.exchange(CybersourceConstants.sessionURL, HttpMethod.POST, entity, String.class);
            Response res = new Response();
            res.setVcCorelationId(response.getHeaders().get(CybersourceConstants.V_C_CORRELATION_ID).get(0));
            res.setResponseCode(String.valueOf(response.getStatusCodeValue()));
            res.setResponseMessage(response.getBody());
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String generateSignature(String payload, String merchantKeyId) throws InvalidKeyException, NoSuchAlgorithmException {
        StringBuilder signatureHeader = new StringBuilder();
        signatureHeader.append("keyid=\"")
                .append(merchantKeyId)
                .append("\"")
                .append(", algorithm=\"HmacSHA256\"")
                .append(", headers=\"")
                .append(getRequestHeaders())
                .append("\"")
                .append(", signature=\"")
                .append(signatureGeneration(payload))
                .append("\"");
        return signatureHeader.toString();
    }

    private static String getRequestHeaders() {
        return "host date (request-target) digest v-c-merchant-id";
    }

    private static String getRequestHeaders(String reqType) {
        return reqType.equalsIgnoreCase("GET") ? "host date (request-target) v-c-merchant-id" : "host date (request-target) digest v-c-merchant-id";
    }

    private static String signatureGeneration(String payload) throws NoSuchAlgorithmException, InvalidKeyException {

        StringBuilder signatureString = new StringBuilder();
        signatureString.append('\n');
        signatureString.append(CybersourceConstants.HOST);
        signatureString.append(": ");
        signatureString.append(APITEST_CYBERSOURCE_COM);
        signatureString.append('\n');
        signatureString.append(CybersourceConstants.DATE);
        signatureString.append(": ");
        signatureString.append(CyberSourceSession.getNewDate());
        signatureString.append('\n');
        signatureString.append("(request-target)");
        signatureString.append(": ");

        signatureString.append("post " + "/microform/v2/sessions/");
        signatureString.append('\n');

        signatureString.append(CybersourceConstants.DIGEST);
        signatureString.append(": ");
        signatureString.append(generateDigest(payload));
        signatureString.append('\n');

        signatureString.append(GlobalLabelParameters.V_C_MERCHANTID);
        signatureString.append(": ");
        signatureString.append(NOVACROFT_SANDBOX);
        signatureString.delete(0, 1);
        String signatureStr = signatureString.toString();

        SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(CybersourceConstants.restApiSharedSecret), HMAC_SHA_256);
        Mac aKeyId = Mac.getInstance(HMAC_SHA_256);
        aKeyId.init(secretKey);
        aKeyId.update(signatureStr.getBytes());
        byte[] aHeaders = aKeyId.doFinal();
        secretKey = null;
        return Base64.getEncoder().encodeToString(aHeaders);
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

