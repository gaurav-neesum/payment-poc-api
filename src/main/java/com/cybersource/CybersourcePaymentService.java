package com.cybersource;

import Api.CaptureApi;
import Invokers.ApiClient;
import Invokers.ApiException;
import Model.*;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

@Service
public class CybersourcePaymentService {
    private static final String date = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT")));
    String DATE_TIME_UTC = "yyyy-MM-dd HH:mm:ss z";
    private static final Log logger = LogFactory.getLog(CybersourcePaymentService.class);

    public String initiatePayment(double amount) throws ConfigException, IOException {

        MerchantConfig merchantConfig = new MerchantConfig();
        merchantConfig.setMerchantID("novacroft_sandbox");
        merchantConfig.setMerchantKeyId(CybersourceConstants.restApiKey);
        merchantConfig.setMerchantSecretKey(CybersourceConstants.restApiSharedSecret);
        merchantConfig.setAuthenticationType("http_signature");
        merchantConfig.setRunEnvironment("apitest.cybersource.com");
        merchantConfig.setRequestType("POST");
        merchantConfig.setRequestHost("apitest.cybersource.com");

        String merchantReference = UUID.randomUUID().toString();

        SessionRequest sessionRequest = new SessionRequest();
        ArrayList<String> targetOrigins = new ArrayList<>();
        targetOrigins.add("http://localhost:8080");
        sessionRequest.setTargetOrigins(targetOrigins);

        CheckoutApiInitialization checkoutApiInitialization = new CheckoutApiInitialization();
        checkoutApiInitialization.setProfileId(CybersourceConstants.profileId);
        checkoutApiInitialization.setAccessKey(CybersourceConstants.accessKey);
        checkoutApiInitialization.setReferenceNumber(merchantReference);
        checkoutApiInitialization.setTransactionUUID(merchantReference);

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
        // Call RestTemplate Impl Here and pass in the ReqBody
        return response.getResponseMessage();

    }

    public String initiatePaymentWithRestTemplate(double amount) throws IOException, NoSuchAlgorithmException, InvalidKeyException {

        SessionRequest sessionRequest = new SessionRequest();
        ArrayList<String> targetOrigins = new ArrayList<>();
        targetOrigins.add("http://localhost:8080");
        sessionRequest.setTargetOrigins(targetOrigins);
        String merchantReference = "701040298373620710";

        CheckoutApiInitialization checkoutApiInitialization = new CheckoutApiInitialization();
        checkoutApiInitialization.setProfileId(CybersourceConstants.profileId);
        checkoutApiInitialization.setAccessKey(CybersourceConstants.accessKey);
        String uuid1 = UUID.randomUUID().toString();
        checkoutApiInitialization.setReferenceNumber(merchantReference);
        checkoutApiInitialization.setTransactionUUID(merchantReference);
        System.out.println("UUID: " + uuid1);

        checkoutApiInitialization.setTransactionType("authorization");
        checkoutApiInitialization.setCurrency("GBP");
        checkoutApiInitialization.setAmount(String.valueOf(amount));
        checkoutApiInitialization.setLocale("en");
        checkoutApiInitialization.setBillToForename("Gaurav");
        checkoutApiInitialization.setBillToSurname("Shah");
        checkoutApiInitialization.setBillToPhone("07899899987");
        checkoutApiInitialization.setBillToEmail("g09.shah@gmail.com");
        checkoutApiInitialization.setBillToAddressLine1("1 My Apartment");
        checkoutApiInitialization.setBillToAddressCity("Nuneaton");
        checkoutApiInitialization.setBillToAddressPostalCode("CV11 6QS");
        checkoutApiInitialization.setBillToAddressCountry("GB");
        checkoutApiInitialization.setOverrideBackofficePostUrl("https://devmelaapi.gauribaba.com/check-payment?reason_code=100");
        checkoutApiInitialization.setOverrideCustomReceiptPage("http://localhost:5005/cb/check-payment");

        checkoutApiInitialization.setIgnoreAvs("true");
        checkoutApiInitialization.setIgnoreCvn("false");
        checkoutApiInitialization.setUnsignedFieldNames("transient_token");

        sessionRequest.setCheckoutApiInitialization(checkoutApiInitialization);
        String requestBody = new ObjectMapper().writeValueAsString(sessionRequest);
        // Call RestTemplate Impl Here and pass in the ReqBody
        Response response = CyberSourceSession.restTemplateImpl3(requestBody);
        return response != null ? response.getResponseMessage() : null;

    }

    static Response processPayment(MerchantConfig merchantConfig) throws IOException {

        PocHttpConnection connection = new PocHttpConnection(merchantConfig);
        return connection.httpConnection();


    }

    MerchantConfig getMerchantConfig() throws ConfigException {
        MerchantConfig merchantConfig = new MerchantConfig();
        merchantConfig.setMerchantID("novacroft_sandbox");
        merchantConfig.setMerchantKeyId(CybersourceConstants.restApiKey);
        merchantConfig.setMerchantSecretKey(CybersourceConstants.restApiSharedSecret);
        merchantConfig.setAuthenticationType("http_signature");
        merchantConfig.setRunEnvironment("apitest.cybersource.com");
        merchantConfig.setRequestType("POST");
        merchantConfig.setRequestHost("apitest.cybersource.com");
        return merchantConfig;
    }
    public Properties createMerchantProps() {
    Properties props = new Properties();

        // HTTP_Signature = http_signature
        props.setProperty("authenticationType", "http_signature");
        props.setProperty("merchantID", "novacroft_sandbox");
        props.setProperty("runEnvironment", "apitest.cybersource.com");
        props.setProperty("merchantKeyId", CybersourceConstants.restApiKey);
        props.setProperty("merchantsecretKey", CybersourceConstants.restApiSharedSecret);
        return props;
    }
    public PtsV2PaymentsCapturesPost201Response capturePayment(Map<String, Object> receipt) throws ConfigException {
        Double amount = Double.valueOf((String) receipt.get("req_amount"));
        String id = (String) receipt.get("transaction_id");
        MerchantConfig merchantConfig = new MerchantConfig(createMerchantProps());

        String transactionUUID = (String) receipt.get("req_transaction_uuid");
        return capturePayment(id, amount, transactionUUID, merchantConfig);
    }

    public PtsV2PaymentsCapturesPost201Response capturePayment(String id, Double amount, String transactionUUID, MerchantConfig merchantConfig) {

        CapturePaymentRequest requestObj = new CapturePaymentRequest();

        Ptsv2paymentsClientReferenceInformation clientReferenceInformation = new Ptsv2paymentsClientReferenceInformation();
        clientReferenceInformation.code(transactionUUID);
        requestObj.clientReferenceInformation(clientReferenceInformation);

        Ptsv2paymentsidcapturesOrderInformation orderInformation = new Ptsv2paymentsidcapturesOrderInformation();
        Ptsv2paymentsidcapturesOrderInformationAmountDetails orderInformationAmountDetails = new Ptsv2paymentsidcapturesOrderInformationAmountDetails();
        orderInformationAmountDetails.totalAmount(String.valueOf(amount));
        orderInformationAmountDetails.currency("GBP");
        orderInformation.amountDetails(orderInformationAmountDetails);

        requestObj.orderInformation(orderInformation);

        PtsV2PaymentsCapturesPost201Response result = null;
        try {
            ApiClient apiClient = new ApiClient();
            apiClient.merchantConfig = merchantConfig;

            CaptureApi apiInstance = new CaptureApi(apiClient);
            result = apiInstance.capturePayment(requestObj, id);

            System.out.println("ResponseCode :" + apiClient.responseCode);
            System.out.println("ResponseMessage :" + apiClient.status);
            System.out.println(result);
            return result;
        } catch (ApiException e) {
            String errorMessage = "Unable to Capture Cybersource Payment";
            logger.error(errorMessage, e);
            throw new RuntimeException(errorMessage);
        }
    }


}

