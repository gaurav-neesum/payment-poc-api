package com.cybersource;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class SessionRequest {
    @JsonProperty("targetOrigins")
    private ArrayList<String> targetOrigins;
    @JsonProperty("checkoutApiInitialization")
    private CheckoutApiInitialization checkoutApiInitialization;

    public ArrayList<String> getTargetOrigins() {
        return targetOrigins;
    }

    public SessionRequest setTargetOrigins(ArrayList<String> targetOrigins) {
        this.targetOrigins = targetOrigins;
        return this;
    }

    public CheckoutApiInitialization getCheckoutApiInitialization() {
        return checkoutApiInitialization;
    }

    public SessionRequest setCheckoutApiInitialization(CheckoutApiInitialization checkoutApiInitialization) {
        this.checkoutApiInitialization = checkoutApiInitialization;
        return this;
    }
}
