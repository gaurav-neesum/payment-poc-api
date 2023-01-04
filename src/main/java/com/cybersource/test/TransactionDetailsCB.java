package com.cybersource.test;

import Api.TransactionDetailsApi;
import Invokers.ApiClient;
import Invokers.ApiException;
import Model.TssV2TransactionsGet200Response;
import Model.TssV2TransactionsGet200ResponseApplicationInformation;
import Model.TssV2TransactionsGet200ResponseApplicationInformationApplications;
import com.cybersource.NovaCustomApiClient;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.test.backgrd.TransactionAction;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

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
        run("6708558677846651504951");
    }

    public static TssV2TransactionsGet200Response run(String id) throws InterruptedException {

        Thread.sleep(15000);

        TssV2TransactionsGet200Response result = null;
        try {
            merchantProp = getMerchantDetails();
            merchantProp.setProperty("userDefinedConnectionTimeout", String.valueOf(1));
            merchantProp.setProperty("userDefinedReadTimeout", String.valueOf(1));
            merchantProp.setProperty("userDefinedWriteTimeout", String.valueOf(1));
            NovaCustomApiClient apiClient = new NovaCustomApiClient();
            apiClient.merchantConfig = new MerchantConfig(merchantProp);

            TransactionDetailsApi apiInstance = new TransactionDetailsApi(apiClient);
            result = apiInstance.getTransaction(id);

            responseCode = apiClient.responseCode;
            status = apiClient.status;
            System.out.println("ResponseCode :" + responseCode);
            System.out.println("ResponseMessage :" + status);
            System.out.println(result);
            TssV2TransactionsGet200ResponseApplicationInformation applicationInformation = result.getApplicationInformation();

            List<TssV2TransactionsGet200ResponseApplicationInformationApplications> applications = applicationInformation.getApplications().stream().filter(app -> app.getName().equals("ics_auth")).collect(Collectors.toList());

            if (applications.isEmpty()) {
                System.out.println("Do Nothing");
            } else {
                TssV2TransactionsGet200ResponseApplicationInformationApplications icsAuthApplication = applications.get(0);
                if (icsAuthApplication.getStatus() != null && icsAuthApplication.getStatus().equals("100")) {
                    System.out.println("Yes Reverse");
                } else {
                    System.out.println("Do Nothing");

                }
            }
            return result;
        } catch (ConfigException | ApiException  e) {
            if (e instanceof ApiException && ((ApiException) e).getCode() == 404) {
                System.out.println("not found budd");
                return null;
            } else {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

        }
    }
}
