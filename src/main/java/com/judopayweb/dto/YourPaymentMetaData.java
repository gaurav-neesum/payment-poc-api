package com.judopayweb.dto;

public class YourPaymentMetaData {
    private String internalLocationRef;
    private String internalId;

    public String getInternalLocationRef() {
        return internalLocationRef;
    }

    public YourPaymentMetaData setInternalLocationRef(String internalLocationRef) {
        this.internalLocationRef = internalLocationRef;
        return this;
    }

    public String getInternalId() {
        return internalId;
    }

    public YourPaymentMetaData setInternalId(String internalId) {
        this.internalId = internalId;
        return this;
    }
}
