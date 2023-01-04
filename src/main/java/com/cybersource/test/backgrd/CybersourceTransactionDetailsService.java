package com.cybersource.test.backgrd;

import Api.TransactionDetailsApi;
import Invokers.ApiClient;
import Model.TssV2TransactionsGet200Response;
import Model.TssV2TransactionsGet200ResponseApplicationInformation;
import com.cybersource.NovaCustomApiClient;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.test.Config;
import com.cybersource.test.CybersourceTransactionQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class CybersourceTransactionDetailsService {
    public static void main(String[] args) {
        getTransactionDetail("4000204506767837", null);
    }
    private static final Logger logger = LoggerFactory.getLogger(CybersourceTransactionDetailsService.class);

    public static CybersourceTransactionQueryResult getTransactionDetail(String transactionId, String UUID) {
        CybersourceTransactionQueryResult queryResult = new CybersourceTransactionQueryResult();

        if (transactionId == null) {
            if (UUID == null) {
                Exception e = new Exception("Exception processing Cybersource Pending Payments in get Transaction Details");
                e.printStackTrace();
                logger.error("Exception processing Cybersource Pending Payments in get Transaction Details", e);
//                ErrorLogDatabase.add(e);
                return null;
            }
            transactionId = CybersourceSearchQueryService.getTransactionIdFromUUIDSearch(UUID);
            if (transactionId == null) {
                return null;
            }
        }
        queryResult.setCybersourceTransactionId(transactionId);
        TssV2TransactionsGet200Response result;
        try {
            Properties merchantProp = Config.getMerchantDetails();
            NovaCustomApiClient apiClient = new NovaCustomApiClient();
            apiClient.merchantConfig = new MerchantConfig(merchantProp);
            TransactionDetailsApi apiInstance = new TransactionDetailsApi(apiClient);
            result = apiInstance.getTransaction(transactionId);
            if (result == null) {
                return null;
            }
            TssV2TransactionsGet200ResponseApplicationInformation applicationInformation = result.getApplicationInformation();
            String status = applicationInformation.getStatus();
            if (status != null && status.equals("TRANSMITTED")) {
                queryResult.setStatus("TRANSMITTED");
                queryResult.setTransactionAction(TransactionAction.Refund);
                queryResult.setReturnCode("1");
            } else if (status != null && status.equals("PENDING")) {
                queryResult.setStatus("REFUSED");
                queryResult.setTransactionAction(TransactionAction.Void);
                queryResult.setReturnCode("7");
            } else {
                queryResult.setStatus("REFUSED");
                queryResult.setTransactionAction(TransactionAction.Reverse);
                queryResult.setReturnCode("7");
            }
            return queryResult;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
