package com.judopayweb.domain;

public class PaymentProvider {
    private static final String judoId = "100156553";
    private static final String token = "viLGU5oMjLq9ClSW";
    private static final String secret = "1f33a76e16be8caf6260aa1f3d44a1ba0d396aec7dabc9d2819618d828d1e191";
    private static final String paymentURL = "https://api-sandbox.judopay.com/paymentsession";

    public static String getJudoId() {
        return judoId;
    }

    public static String getToken() {
        return token;
    }

    public static String getSecret() {
        return secret;
    }

    public static String getPaymentURL(){
        return paymentURL;
    }
}
