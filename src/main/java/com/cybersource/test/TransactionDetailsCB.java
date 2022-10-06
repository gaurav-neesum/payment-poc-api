package com.cybersource.test;
import java.io.Serializable;
import java.util.*;

import Invokers.Configuration;
import com.cybersource.CybersourceConstants;
import com.cybersource.NovaCustomApiClient;
import com.cybersource.authsdk.core.MerchantConfig;

import Api.*;
import Invokers.ApiClient;
import Invokers.ApiException;
import Model.*;
import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import org.springframework.lang.Nullable;

import static com.cybersource.test.Config.getMerchantDetails;

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
        run("6647473609296689704953");
    }

    public static TssV2TransactionsGet200Response run(String id) throws InterruptedException {

        Thread.sleep(15000);

        TssV2TransactionsGet200Response result = null;
        try {
            merchantProp = getMerchantDetails();
//            NovaCustomApiClient apiClient = new NovaCustomApiClient();
            ApiClient apiClient = new ApiClient();
            apiClient.merchantConfig = new MerchantConfig(merchantProp);
            apiClient.setJSON(new CustomJSON(apiClient));// Your Custom JSON class here
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

    class CustomOkHttpClient extends OkHttpClient{

    }

    class CustomResponse extends ResponseBody implements Serializable{
        private final @Nullable String contentTypeString;
        private final long contentLength;
        private final BufferedSource source;

        public CustomResponse(
                @Nullable String contentTypeString, long contentLength, BufferedSource source) {
            this.contentTypeString = contentTypeString;
            this.contentLength = contentLength;
            this.source = source;
        }

        @Override public MediaType contentType() {
            return contentTypeString != null ? MediaType.parse(contentTypeString) : null;
        }

        @Override public long contentLength() {
            return contentLength;
        }

        @Override public BufferedSource source() {
            return source;
        }
    }
}
