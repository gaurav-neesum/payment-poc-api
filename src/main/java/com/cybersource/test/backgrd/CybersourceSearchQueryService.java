package com.cybersource.test.backgrd;

import Api.SearchTransactionsApi;
import Invokers.ApiClient;
import Model.CreateSearchRequest;
import Model.TssV2TransactionsPost201Response;
import Model.TssV2TransactionsPost201ResponseEmbedded;
import Model.TssV2TransactionsPost201ResponseEmbeddedTransactionSummaries;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.test.Config;

import java.util.List;
import java.util.Properties;

public class CybersourceSearchQueryService {
    /**
     * Searches for Authorization Specific Application Information
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
            ApiClient apiClient = new ApiClient();
            apiClient.merchantConfig = new MerchantConfig(merchantProp);

            SearchTransactionsApi apiInstance = new SearchTransactionsApi(apiClient);
            result = apiInstance.createSearch(requestObj);
            if (result.getCount() <= 0) {
                return null;
            }
            TssV2TransactionsPost201ResponseEmbedded embedded = result.getEmbedded();
            List<TssV2TransactionsPost201ResponseEmbeddedTransactionSummaries> transactionSummaries = embedded.getTransactionSummaries();
            if (transactionSummaries.isEmpty()) {
                return null;
            }
            TssV2TransactionsPost201ResponseEmbeddedTransactionSummaries transactionSummary = transactionSummaries.get(0);
            return transactionSummary.getId();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

}


