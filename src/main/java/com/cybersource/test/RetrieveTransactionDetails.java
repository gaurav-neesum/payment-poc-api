package com.cybersource.test;

import Api.TransactionDetailsApi;
import Invokers.ApiClient;
import Invokers.ApiException;
import Model.TssV2TransactionsGet200Response;
import com.cybersource.CybersourceConstants;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;

public class RetrieveTransactionDetails {
    public static void main(String[] args) throws ConfigException, ApiException {
        MerchantConfig merchantConfig = new MerchantConfig();
        merchantConfig.setMerchantID("novacroft_sandbox");
        merchantConfig.setMerchantKeyId(CybersourceConstants.restApiKey);
        merchantConfig.setMerchantSecretKey(CybersourceConstants.restApiSharedSecret);
        merchantConfig.setAuthenticationType("http_signature");
        merchantConfig.setRunEnvironment("apitest.cybersource.com");
        merchantConfig.setRequestType("POST");
        merchantConfig.setRequestHost("apitest.cybersource.com");
        ApiClient apiClient = new ApiClient();
        apiClient.merchantConfig = merchantConfig;

        TransactionDetailsApi apiInstance = new TransactionDetailsApi(apiClient);
        TssV2TransactionsGet200Response  result = apiInstance.getTransaction("6624634111936538504003");

       String responseCode = apiClient.responseCode;
     String   status = apiClient.status;
        System.out.println("ResponseCode :" + responseCode);
        System.out.println("ResponseMessage :" + status);
        System.out.println(result);
    }
}
