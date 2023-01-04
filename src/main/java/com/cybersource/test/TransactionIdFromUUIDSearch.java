package com.cybersource.test;

import Api.SearchTransactionsApi;
import Invokers.ApiClient;
import Model.*;
import com.cybersource.NovaCustomApiClient;
import com.cybersource.authsdk.core.MerchantConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Properties;

public class TransactionIdFromUUIDSearch {
    public static void main(String[] args) {
        String id = getTransactionIdFromUUIDSearch("7f72a451-c8f9-48be-be38-00e635e967d2");

        System.out.println("ID is:" + id);
    }

    /**
     * clientReferenceInformation.code:%s AND applicationInformation.applications.name:(ics_auth OR ics_bill) AND submitTimeUtc:[NOW/DAY-7DAYS TO NOW/DAY+1DAY}
     * sort: submitTimeUtc:desc
     *
     * @param transactionUUID
     * @return
     */
    public static String getTransactionIdFromUUIDSearch(String transactionUUID) {

        CreateSearchRequest requestObj = new CreateSearchRequest();


        requestObj.save(false);
        requestObj.name("MRN");
        requestObj.timezone("Europe/London");
        String query = String.format("clientReferenceInformation.code:%s AND applicationInformation.applications.name:(ics_auth OR ics_bill) AND submitTimeUtc:[NOW/DAY-7DAYS TO NOW/DAY+1DAY}", transactionUUID); // add week back query

        requestObj.query(query);
        requestObj.sort("submitTimeUtc:desc");
        TssV2TransactionsPost201Response result = null;
        try {
            Properties merchantProp = Config.getMerchantDetails();
            NovaCustomApiClient apiClient = new NovaCustomApiClient();
//            ApiClient
            apiClient.merchantConfig = new MerchantConfig(merchantProp);

            SearchTransactionsApi apiInstance = new SearchTransactionsApi(apiClient);
            result = apiInstance.createSearch(requestObj);
            String resultJson = new ObjectMapper().writeValueAsString(result);
            if (result.getCount() <= 0) {
                return null;
            }
            TssV2TransactionsPost201ResponseEmbedded embedded = result.getEmbedded();
            List<TssV2TransactionsPost201ResponseEmbeddedTransactionSummaries> transactionSummaries = embedded.getTransactionSummaries();
            if (transactionSummaries.isEmpty()) {
                return null;
            }
            TssV2TransactionsPost201ResponseEmbeddedTransactionSummaries transactionSummary = transactionSummaries.get(0);

            TssV2TransactionsGet200Response details=  TransactionDetailsCB.run(transactionSummary.getId());

            return transactionSummary.getId();


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

}
