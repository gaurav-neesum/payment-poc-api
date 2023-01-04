package com.cybersource.test;

import Api.VoidApi;
import Invokers.ApiClient;
import Model.PtsV2PaymentsVoidsPost201Response;
import Model.Ptsv2paymentsidreversalsClientReferenceInformation;
import Model.VoidCaptureRequest;
import com.cybersource.authsdk.core.MerchantConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Properties;

public class TransactionVoidCaptureTest {

    public static void main(String[] args) throws Exception {
        run("6684383054426829704953");
    }

    public static PtsV2PaymentsVoidsPost201Response run(String id) {

        VoidCaptureRequest requestObj = new VoidCaptureRequest();

        Ptsv2paymentsidreversalsClientReferenceInformation clientReferenceInformation = new Ptsv2paymentsidreversalsClientReferenceInformation();
        clientReferenceInformation.code("e5432eaa-619e-4764-89d9-b3c5904a90d0");
        requestObj.clientReferenceInformation(clientReferenceInformation);

        PtsV2PaymentsVoidsPost201Response result = null;
        try {
            Properties merchantProp = Config.getMerchantDetails();
            ApiClient apiClient = new ApiClient();
            apiClient.merchantConfig = new MerchantConfig(merchantProp);

            VoidApi apiInstance = new VoidApi(apiClient);
            result = apiInstance.voidCapture(requestObj, id);
            String resultJson = new ObjectMapper().writeValueAsString(result);

            String responseCode = apiClient.responseCode;
            String status = apiClient.status;
            System.out.println("ResponseCode :" + responseCode);
            System.out.println("ResponseMessage :" + status);
            System.out.println(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}


