package com.cybersource.test;


import Api.ReversalApi;
import Invokers.ApiClient;
import Model.*;
import com.cybersource.authsdk.core.MerchantConfig;

import java.util.Properties;

public class AuthorizationReversal {
    private static String responseCode = null;
    private static String status = null;
    private static Properties merchantProp;

    public static void main(String args[]) throws Exception {
        run();
    }

    public static PtsV2PaymentsReversalsPost201Response run() {
        String id = "6641890026876589204006";

        AuthReversalRequest requestObj = new AuthReversalRequest();

        Ptsv2paymentsidreversalsClientReferenceInformation clientReferenceInformation = new Ptsv2paymentsidreversalsClientReferenceInformation();
        clientReferenceInformation.code("TC50171_3");
        requestObj.clientReferenceInformation(clientReferenceInformation);

        Ptsv2paymentsidreversalsReversalInformation reversalInformation = new Ptsv2paymentsidreversalsReversalInformation();
        Ptsv2paymentsidreversalsReversalInformationAmountDetails reversalInformationAmountDetails = new Ptsv2paymentsidreversalsReversalInformationAmountDetails();
        reversalInformationAmountDetails.totalAmount("12.12");
        reversalInformation.amountDetails(reversalInformationAmountDetails);

        reversalInformation.reason("Invalid Address");
        requestObj.reversalInformation(reversalInformation);

        PtsV2PaymentsReversalsPost201Response result = null;
        try {
            merchantProp = Config.getMerchantDetails();
            ApiClient apiClient = new ApiClient();
            MerchantConfig merchantConfig = new MerchantConfig(merchantProp);
            apiClient.merchantConfig = merchantConfig;

            ReversalApi apiInstance = new ReversalApi(apiClient);
            result = apiInstance.authReversal(id, requestObj);

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
