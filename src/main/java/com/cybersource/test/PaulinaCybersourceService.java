package com.cybersource.test;

import com.cybersource.*;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class PaulinaCybersourceService  {
    public static void main(String[] args) throws IOException, ConfigException {

            getToken();
        }

        public static String getToken() throws ConfigException, IOException {

            final var checkoutApiInitialization = prepareCheckoutApiInitialization();
            final var sessionRequest = prepareSessionRequest(checkoutApiInitialization);
            final var merchantConfig = prepareMerchantConfig(sessionRequest);
            final var connection = new PocHttpConnection(merchantConfig);
            Response response = connection.httpConnection();
            return response.getResponseMessage();
        }

        private static MerchantConfig prepareMerchantConfig ( final SessionRequest sessionRequest) throws
        ConfigException, IOException {
            final var merchantConfig = new MerchantConfig();
            merchantConfig.setMerchantID("novacroft_sandbox");
            merchantConfig.setMerchantKeyId(CybersourceConstants.restApiKey);
            merchantConfig.setMerchantSecretKey(CybersourceConstants.restApiSharedSecret);
            merchantConfig.setAuthenticationType("http_signature");
            merchantConfig.setRunEnvironment("apitest.cybersource.com");
            merchantConfig.setRequestType("POST");
            merchantConfig.setRequestHost("apitest.cybersource.com");
            merchantConfig.setRequestTarget("/microform/v2/sessions/");
            final var requestBody = new ObjectMapper().writeValueAsString(sessionRequest);
            merchantConfig.setRequestData(requestBody);

            return merchantConfig;
        }

        private static SessionRequest prepareSessionRequest ( final CheckoutApiInitialization checkoutApiInitialization){
            final var sessionRequest = new SessionRequest();
            ArrayList<String> targetOrigins = new ArrayList<>();
            targetOrigins.add("http://localhost");
//            targetOrigins.add("http://localhost/innovator");
            targetOrigins.add("http://localhost:80");
            sessionRequest.setTargetOrigins(targetOrigins);
            sessionRequest.setCheckoutApiInitialization(checkoutApiInitialization);

            return sessionRequest;
        }

        private static CheckoutApiInitialization prepareCheckoutApiInitialization () {
            final var checkoutApiInitialization = new CheckoutApiInitialization();
            checkoutApiInitialization.setProfileId(CybersourceConstants.profileId);
            checkoutApiInitialization.setAccessKey(CybersourceConstants.accessKey);
            checkoutApiInitialization.setReferenceNumber(UUID.randomUUID().toString());
            checkoutApiInitialization.setTransactionUUID(UUID.randomUUID().toString());
            checkoutApiInitialization.setTransactionType("authorization");
            checkoutApiInitialization.setCurrency("GBP");
            checkoutApiInitialization.setAmount("24.00");
            checkoutApiInitialization.setLocale("en");
            checkoutApiInitialization.setBillToForename("Gaurav");
            checkoutApiInitialization.setBillToSurname("Shah");
            checkoutApiInitialization.setBillToPhone("07899899987");
            checkoutApiInitialization.setBillToEmail("g09.shah@gmail.com");
            checkoutApiInitialization.setBillToAddressLine1("1 My Apartment");
            checkoutApiInitialization.setBillToAddressCity("Nuneaton");
            checkoutApiInitialization.setBillToAddressPostalCode("CV10 0IS");
            checkoutApiInitialization.setBillToAddressCountry("GB");
            checkoutApiInitialization.setOverrideBackofficePostUrl("https://dev-api.melaxpress.com/check-payment");
            checkoutApiInitialization.setOverrideCustomReceiptPage("http://localhost/receipt");
            checkoutApiInitialization.setIgnoreAvs("true");
            checkoutApiInitialization.setIgnoreCvn("false");
            checkoutApiInitialization.setUnsignedFieldNames("transient_token");

            return checkoutApiInitialization;
        }

}
