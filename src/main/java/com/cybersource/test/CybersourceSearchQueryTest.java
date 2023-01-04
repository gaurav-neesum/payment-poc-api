package com.cybersource.test;

import Api.SearchTransactionsApi;
import Invokers.ApiClient;
import Model.*;
import com.cybersource.NovaCustomApiClient;
import com.cybersource.authsdk.core.MerchantConfig;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class CybersourceSearchQueryTest {

    public static void main(String[] args) {
        createSearchRequest("9f14405c-5cd9-4513-bda9-b7198b156628");
    }

    public static CybersourceTransactionQueryResult createSearchRequest(String transactionUUID) {


        CreateSearchRequest requestObj = new CreateSearchRequest();

        requestObj.save(false);
        requestObj.name("MRN");
        requestObj.timezone("Europe/London");
        String query = String.format("clientReferenceInformation.code:%s AND applicationInformation.applications.name:ics_auth AND submitTimeUtc:[NOW/DAY-7DAYS TO NOW/DAY+1DAY}", transactionUUID);
        requestObj.query(query);
        requestObj.sort("id:asc,submitTimeUtc:asc");
        TssV2TransactionsPost201Response result = null;
        try {
            NovaCustomApiClient apiClient = new NovaCustomApiClient();
            Properties merchantProp = Config.getMerchantDetails();
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
            String cybersourceTransactionId = transactionSummary.getId();
            TssV2TransactionsPost201ResponseEmbeddedApplicationInformation applicationInformation = transactionSummary.getApplicationInformation();
            List<TssV2TransactionsPost201ResponseEmbeddedApplicationInformationApplications> icsBillApplications = applicationInformation.getApplications().stream().filter(app -> app.getName().equals("ics_bill")).collect(Collectors.toList());
            if (icsBillApplications.isEmpty()) {
                return null;
            }
            TssV2TransactionsPost201ResponseEmbeddedApplicationInformationApplications icsBillApplication = icsBillApplications.get(0);
            CybersourceTransactionQueryResult queryResult = new CybersourceTransactionQueryResult();

            if (icsBillApplication.getReturnCode() == 100) {
                queryResult.setReturnCode("1");
            } else {
                queryResult.setReturnCode("7");
            }
            queryResult.setCybersourceTransactionId(cybersourceTransactionId);
            System.out.println(result);

            return queryResult;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }
}


