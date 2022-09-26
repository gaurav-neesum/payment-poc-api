package com.cybersource.test;

import com.cybersource.authsdk.core.MerchantConfig;

import Api.*;
import Invokers.ApiClient;
import Model.*;

import java.util.Properties;

public class CapturePayment {
    private static String responseCode = null;
    private static String status = null;
    private static Properties merchantProp;

    public static void main(String args[]) throws Exception {
        run();
    }

    public static PtsV2PaymentsCapturesPost201Response run() {
        String id = "6641886501156638004002";

        CapturePaymentRequest requestObj = new CapturePaymentRequest();

        Ptsv2paymentsClientReferenceInformation clientReferenceInformation = new Ptsv2paymentsClientReferenceInformation();
        clientReferenceInformation.code("886366b5-04ee-4829-a40b-ec1eef24b720");
        requestObj.clientReferenceInformation(clientReferenceInformation);

        Ptsv2paymentsidcapturesOrderInformation orderInformation = new Ptsv2paymentsidcapturesOrderInformation();
        Ptsv2paymentsidcapturesOrderInformationAmountDetails orderInformationAmountDetails = new Ptsv2paymentsidcapturesOrderInformationAmountDetails();
        orderInformationAmountDetails.totalAmount("10.00");
        orderInformationAmountDetails.currency("GBP");
        orderInformation.amountDetails(orderInformationAmountDetails);

        requestObj.orderInformation(orderInformation);

        PtsV2PaymentsCapturesPost201Response result = null;
        try {
            merchantProp = Config.getMerchantDetails();
            ApiClient apiClient = new ApiClient();
            MerchantConfig merchantConfig = new MerchantConfig(merchantProp);
            apiClient.merchantConfig = merchantConfig;

            CaptureApi apiInstance = new CaptureApi(apiClient);
            result = apiInstance.capturePayment(requestObj, id);

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

