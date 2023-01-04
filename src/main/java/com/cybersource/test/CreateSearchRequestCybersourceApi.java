package com.cybersource.test;


import Api.SearchTransactionsApi;
import Invokers.ApiClient;
import Model.CreateSearchRequest;
import Model.TssV2TransactionsPost201Response;
import com.cybersource.authsdk.core.MerchantConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Properties;

public class CreateSearchRequestCybersourceApi {
    private static String responseCode = null;
    private static String status = null;
    private static Properties merchantProp;

    public static void main(String args[]) throws Exception {
        run();
    }

    public static TssV2TransactionsPost201Response run() {

        CreateSearchRequest requestObj = new CreateSearchRequest();

        requestObj.save(false);
        requestObj.name("MRN");
        requestObj.timezone("America/Chicago");
        requestObj.query("clientReferenceInformation.code:781a9c35-d9af-4872-83f0-f2d572652e9d AND applicationInformation.applications.name:(ics_auth OR ics_bill) AND submitTimeUtc:[NOW/DAY-7DAYS TO NOW/DAY+1DAY}");
        requestObj.offset(0);
        requestObj.limit(100);
        requestObj.sort("id:asc,submitTimeUtc:asc");
        TssV2TransactionsPost201Response result = null;
        try {
            merchantProp = Config.getMerchantDetails();
            ApiClient apiClient = new ApiClient();
            MerchantConfig merchantConfig = new MerchantConfig(merchantProp);
            apiClient.merchantConfig = merchantConfig;

            SearchTransactionsApi apiInstance = new SearchTransactionsApi(apiClient);
            result = apiInstance.createSearch(requestObj);
            String resultJson = new ObjectMapper().writeValueAsString(result);

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
}
