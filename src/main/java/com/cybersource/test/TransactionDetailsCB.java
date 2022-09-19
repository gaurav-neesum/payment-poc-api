package com.cybersource.test;
import java.util.*;

import Invokers.Configuration;
import com.cybersource.CybersourceConstants;
import com.cybersource.NovaCustomApiClient;
import com.cybersource.authsdk.core.MerchantConfig;

import Api.*;
import Invokers.ApiClient;
import Invokers.ApiException;
import Model.*;

/**
 * v-c-merchant-id: novacroft_sandbox
 * Accept: application/hal+json;charset=utf-8
 * v-c-client-id: cybs-rest-sdk-java-0.0.47
 * Signature: keyid="291c53de-b6cf-4356-ab75-a9e52231e674", algorithm="HmacSHA256", headers="host date (request-target) v-c-merchant-id", signature="WZNMMZH1XZP9pFGDAi2iYht0hbLs2LMjqnfXm2sooXM="
 * User-Agent: Mozilla/5.0
 * Host: apitest.cybersource.com
 * Date: Sun, 18 Sep 2022 12:57:47 GMT
 * Content-Type: application/json;charset=utf-8
 * Connection: keep-alive
 */
public class TransactionDetailsCB {
    private static String responseCode = null;
    private static String status = null;
    private static Properties merchantProp;

    public static void main(String args[]) throws Exception {
        run("6624634111936538504003");
    }

    public static TssV2TransactionsGet200Response run(String id) throws InterruptedException {

        Thread.sleep(15000);

        TssV2TransactionsGet200Response result = null;
        try {
            merchantProp =getMerchantDetails();
            NovaCustomApiClient apiClient = new NovaCustomApiClient();
            apiClient.merchantConfig = new MerchantConfig(merchantProp);

            TransactionDetailsApi apiInstance = new TransactionDetailsApi(apiClient);
            result = apiInstance.getTransaction(id);

            responseCode = apiClient.responseCode;
            status = apiClient.status;
            System.out.println("ResponseCode :" + responseCode);
            System.out.println("ResponseMessage :" + status);
            System.out.println(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public  static Properties getMerchantDetails(){
        Properties props = new Properties();

        // HTTP_Signature = http_signature and JWT = jwt
        props.setProperty("authenticationType", "http_signature");
        props.setProperty("merchantID", "novacroft_sandbox");
        props.setProperty("runEnvironment", "apitest.cybersource.com");
//        props.setProperty("requestJsonPath", "src/main/resources/request.json");


        // HTTP Parameters
        props.setProperty("merchantKeyId", CybersourceConstants.restApiKey);
        props.setProperty("merchantsecretKey", CybersourceConstants.restApiSharedSecret);


        return props;
    }

}
