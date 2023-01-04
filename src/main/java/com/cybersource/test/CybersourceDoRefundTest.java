package com.cybersource.test;

import Api.RefundApi;
import Invokers.ApiClient;
import Model.*;
import com.cybersource.authsdk.core.MerchantConfig;

import java.util.Properties;

public class CybersourceDoRefundTest {

    public static void main(String[] args) {
        refundPayment("6680986648106247604951", "7f72a451-c8f9-48be-be38-00e635e967d2", "15.00");
    }

    public static PtsV2PaymentsRefundPost201Response refundPayment(final String id, final String UUID, final String totalAmount) {
        final RefundPaymentRequest refundRequest = new RefundPaymentRequest();

        final Ptsv2paymentsClientReferenceInformation clientReferenceInformation = new Ptsv2paymentsClientReferenceInformation();
        clientReferenceInformation.code(UUID);
        refundRequest.clientReferenceInformation(clientReferenceInformation);

        final Ptsv2paymentsidrefundsOrderInformation orderInformation = new Ptsv2paymentsidrefundsOrderInformation();
        final Ptsv2paymentsidcapturesOrderInformationAmountDetails orderInformationAmountDetails = new Ptsv2paymentsidcapturesOrderInformationAmountDetails();
        orderInformationAmountDetails.totalAmount(totalAmount);
        orderInformationAmountDetails.currency("GBP");
        orderInformation.amountDetails(orderInformationAmountDetails);
        refundRequest.orderInformation(orderInformation);

        try {
            final Properties merchantProp = Config.getMerchantDetails();
            final ApiClient apiClient = new ApiClient();
            apiClient.merchantConfig = new MerchantConfig(merchantProp);

            final RefundApi apiInstance = new RefundApi(apiClient);
            final PtsV2PaymentsRefundPost201Response refundResponse = apiInstance.refundPayment(refundRequest, id);
            return refundResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
