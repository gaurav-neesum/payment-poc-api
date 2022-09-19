package com.cybersource.test;

import Api.TransactionDetailsApi;
import Invokers.ApiClient;
import Invokers.ApiException;
import Model.TssV2TransactionsGet200Response;
import com.cybersource.CybersourceConstants;
import com.cybersource.authsdk.core.Authorization;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.http.HttpSignatureToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Properties;

import static com.cybersource.test.TransactionDetailsCB.getMerchantDetails;

public class RetrieveTransactionDetails {
    private static final String date = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT")));

    public static void unirestCall() throws UnirestException, ConfigException {
        Unirest.setTimeouts(0, 0);

        Authorization authorization = new Authorization();
        String token = authorization.getToken(getMerchantConfig(getMerchantDetails()));
        HttpSignatureToken httpSignatureToken = new HttpSignatureToken(getMerchantConfig(getMerchantDetails()));
        String tok = httpSignatureToken.getToken();
        String sig = getSignature();
        HttpResponse<String> response = Unirest.get("https://apitest.cybersource.com/tss/v2/transactions/6624634111936538504003")
                .header("signature", tok)
//                .header("signature", "	keyid=\"291c53de-b6cf-4356-ab75-a9e52231e674\", algorithm=\"HmacSHA256\", headers=\"host  (request-target) v-c-merchant-id\", signature=\"94zwMvpobEecmFCPdQKOB/MN+Oy2ELnrqFzhW06n0Gk=\"")
                .header("v-c-merchant-id", "novacroft_sandbox")
                .header("v-c-date", "Sun, 18 Sep 2022 10:19:06 GMT")
                .asString();
        System.out.println("UNIREST--------------------------");
        System.out.println(response.getBody());
    }

    public static void main(String[] args) throws ConfigException, ApiException, UnirestException, URISyntaxException {
        unirestCall();
//        process("6634947915186859104003");
        if (true) {
            return;
        }


        MerchantConfig merchantConfig = getMerchantConfig(null);
        ApiClient apiClient = new ApiClient();
        apiClient.merchantConfig = merchantConfig;
        TransactionDetailsApi apiInstance = new TransactionDetailsApi(apiClient);
        TssV2TransactionsGet200Response result = apiInstance.getTransaction("6624634111936538504003");

        String responseCode = apiClient.responseCode;
        String status = apiClient.status;
        System.out.println("ResponseCode :" + responseCode);
        System.out.println("ResponseMessage :" + status);
        System.out.println(result);
    }


    public static RestTemplate getRestTemplate() {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setConnectTimeout(3000);
        simpleClientHttpRequestFactory.setReadTimeout(3000);
        return new RestTemplate(simpleClientHttpRequestFactory);
    }

    public static void process(String transactionId) throws ConfigException, URISyntaxException {
        RestTemplate restTemplate = getRestTemplate();
        HttpHeaders headers = new HttpHeaders();

        Authorization authorization = new Authorization();
        String token = authorization.getToken(getMerchantConfig(getMerchantDetails()));
        String tokenNoDate = token.replace("date", "");
        String pmToken = "keyid=\"291c53de-b6cf-4356-ab75-a9e52231e674\", algorithm=\"HmacSHA256\", headers=\"host  (request-target) v-c-merchant-id\", signature=\"94zwMvpobEecmFCPdQKOB/MN+Oy2ELnrqFzhW06n0Gk=\"";
        headers.set("Host", "apitest.cybersource.com");
        headers.set("v-c-date", date);
        headers.set("date", date);
        headers.set("v-c-merchant-id", "novacroft_sandbox");
        headers.set("signature", tokenNoDate);
        String url = String.format("https://apitest.cybersource.com/tss/v2/transactions/%s", transactionId);
        URI uri = new URI(url);
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

            String responseStr = response.getBody();
        } catch (HttpClientErrorException e) {
            System.out.print(e.getResponseBodyAsString());
        }

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


    public static MerchantConfig getMerchantConfig(Properties properties) throws ConfigException {
        MerchantConfig merchantConfig = new MerchantConfig(properties);
//        merchantConfig.setMerchantID("novacroft_sandbox");
//        merchantConfig.setMerchantKeyId(CybersourceConstants.restApiKey);
//        merchantConfig.setMerchantSecretKey(CybersourceConstants.restApiSharedSecret);
//        merchantConfig.setAuthenticationType("http_signature");
//        merchantConfig.setRunEnvironment("apitest.cybersource.com");
        merchantConfig.setRequestType("GET");
//        merchantConfig.setRequestHost("apitest.cybersource.com");
        return merchantConfig;
    }

    public static String getSignature() throws ConfigException {

        Authorization authorization = new Authorization();
        String token = authorization.getToken(getMerchantConfig(getMerchantDetails()));
        String keyId = CybersourceConstants.restApiKey;
        String algorithm = "HmacSHA256";
        String headers = "host(request-target) v-c-merchant-id";
        return "keyid=\"" + keyId + "\", algorithm=\"HmacSHA256\", headers=\"host date (request-target) digest v-c-merchant-id\", signature=\"" + token + "\"";


    }
}
