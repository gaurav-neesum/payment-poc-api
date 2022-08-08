package com.judopayweb.dto.response;


public class PaymentSessionResponse {
    private String expiryDate;
    private String reference;
    private String apiToken;
    private String yourPaymentReference;
    private String yourConsumerReference;


    public String getExpiryDate() {
        return expiryDate;
    }

    public PaymentSessionResponse setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    public String getReference() {
        return reference;
    }

    public PaymentSessionResponse setReference(String reference) {
        this.reference = reference;
        return this;
    }

    public String getApiToken() {
        return apiToken;
    }

    public PaymentSessionResponse setApiToken(String apiToken) {
        this.apiToken = apiToken;
        return this;
    }

    public String getYourPaymentReference() {
        return yourPaymentReference;
    }

    public PaymentSessionResponse setYourPaymentReference(String yourPaymentReference) {
        this.yourPaymentReference = yourPaymentReference;
        return this;
    }

    public String getYourConsumerReference() {
        return yourConsumerReference;
    }

    public PaymentSessionResponse setYourConsumerReference(String yourConsumerReference) {
        this.yourConsumerReference = yourConsumerReference;
        return this;
    }
}
