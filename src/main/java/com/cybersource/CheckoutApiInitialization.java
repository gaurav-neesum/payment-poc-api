package com.cybersource;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckoutApiInitialization {
    @JsonProperty("profile_id")
    public String profileId;
    @JsonProperty("access_key")
    public String accessKey;
    @JsonProperty("reference_number")
    public String referenceNumber;
    @JsonProperty("transaction_uuid")
    public String transactionUUID;
    @JsonProperty("transaction_type")
    public String transactionType;
    @JsonProperty("payment_method")
    public String paymentMethod;
    @JsonProperty("currency")
    public String currency;
    @JsonProperty("amount")
    public String amount;
    @JsonProperty("locale")
    public String locale;
    @JsonProperty("signed_date_time")
    public String signedDateTime;
    @JsonProperty("bill_to_forename")
    public String billToForename;
    @JsonProperty("bill_to_surname")
    public String billToSurname;
    @JsonProperty("bill_to_phone")
    public String billToPhone;
    @JsonProperty("bill_to_email")
    public String billToEmail;
    @JsonProperty("bill_to_address_line1")
    public String billToAddressLine1;
    @JsonProperty("bill_to_address_city")
    public String billToAddressCity;
    @JsonProperty("bill_to_address_state")
    public String billToAddressState;
    @JsonProperty("bill_to_address_postal_code")
    public String billToAddressPostalCode;
    @JsonProperty("bill_to_address_country")
    public String billToAddressCountry;
    @JsonProperty("override_backoffice_post_url")
    public String overrideBackofficePostUrl;
    @JsonProperty("override_custom_receipt_page")
    public String overrideCustomReceiptPage;
    @JsonProperty("ignore_avs")
    public String ignoreAvs;
    @JsonProperty("ignore_cvn")
    public String ignoreCvn;
    @JsonProperty("partner_solution_id")
    public String partnerSolutionId;
    @JsonProperty("signed_field_names")
    public String signedFieldNames;
    @JsonProperty("unsigned_field_names")
    public String unsignedFieldNames;

    public String getProfileId() {
        return profileId;
    }

    public CheckoutApiInitialization setProfileId(String profileId) {
        this.profileId = profileId;
        return this;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public CheckoutApiInitialization setAccessKey(String accessKey) {
        this.accessKey = accessKey;
        return this;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public CheckoutApiInitialization setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
        return this;
    }

    public String getTransactionUUID() {
        return transactionUUID;
    }

    public CheckoutApiInitialization setTransactionUUID(String transactionUUID) {
        this.transactionUUID = transactionUUID;
        return this;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public CheckoutApiInitialization setTransactionType(String transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public CheckoutApiInitialization setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public CheckoutApiInitialization setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public CheckoutApiInitialization setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public String getLocale() {
        return locale;
    }

    public CheckoutApiInitialization setLocale(String locale) {
        this.locale = locale;
        return this;
    }

    public String getSignedDateTime() {
        return signedDateTime;
    }

    public CheckoutApiInitialization setSignedDateTime(String signedDateTime) {
        this.signedDateTime = signedDateTime;
        return this;
    }

    public String getBillToForename() {
        return billToForename;
    }

    public CheckoutApiInitialization setBillToForename(String billToForename) {
        this.billToForename = billToForename;
        return this;
    }

    public String getBillToSurname() {
        return billToSurname;
    }

    public CheckoutApiInitialization setBillToSurname(String billToSurname) {
        this.billToSurname = billToSurname;
        return this;
    }

    public String getBillToPhone() {
        return billToPhone;
    }

    public CheckoutApiInitialization setBillToPhone(String billToPhone) {
        this.billToPhone = billToPhone;
        return this;
    }

    public String getBillToEmail() {
        return billToEmail;
    }

    public CheckoutApiInitialization setBillToEmail(String billToEmail) {
        this.billToEmail = billToEmail;
        return this;
    }

    public String getBillToAddressLine1() {
        return billToAddressLine1;
    }

    public CheckoutApiInitialization setBillToAddressLine1(String billToAddressLine1) {
        this.billToAddressLine1 = billToAddressLine1;
        return this;
    }

    public String getBillToAddressCity() {
        return billToAddressCity;
    }

    public CheckoutApiInitialization setBillToAddressCity(String billToAddressCity) {
        this.billToAddressCity = billToAddressCity;
        return this;
    }

    public String getBillToAddressState() {
        return billToAddressState;
    }

    public CheckoutApiInitialization setBillToAddressState(String billToAddressState) {
        this.billToAddressState = billToAddressState;
        return this;
    }

    public String getBillToAddressPostalCode() {
        return billToAddressPostalCode;
    }

    public CheckoutApiInitialization setBillToAddressPostalCode(String billToAddressPostalCode) {
        this.billToAddressPostalCode = billToAddressPostalCode;
        return this;
    }

    public String getBillToAddressCountry() {
        return billToAddressCountry;
    }

    public CheckoutApiInitialization setBillToAddressCountry(String billToAddressCountry) {
        this.billToAddressCountry = billToAddressCountry;
        return this;
    }

    public String getOverrideBackofficePostUrl() {
        return overrideBackofficePostUrl;
    }

    public CheckoutApiInitialization setOverrideBackofficePostUrl(String overrideBackofficePostUrl) {
        this.overrideBackofficePostUrl = overrideBackofficePostUrl;
        return this;
    }

    public String getOverrideCustomReceiptPage() {
        return overrideCustomReceiptPage;
    }

    public CheckoutApiInitialization setOverrideCustomReceiptPage(String overrideCustomReceiptPage) {
        this.overrideCustomReceiptPage = overrideCustomReceiptPage;
        return this;
    }

    public String getIgnoreAvs() {
        return ignoreAvs;
    }

    public CheckoutApiInitialization setIgnoreAvs(String ignoreAvs) {
        this.ignoreAvs = ignoreAvs;
        return this;
    }

    public String getIgnoreCvn() {
        return ignoreCvn;
    }

    public CheckoutApiInitialization setIgnoreCvn(String ignoreCvn) {
        this.ignoreCvn = ignoreCvn;
        return this;
    }

    public String getPartnerSolutionId() {
        return partnerSolutionId;
    }

    public CheckoutApiInitialization setPartnerSolutionId(String partnerSolutionId) {
        this.partnerSolutionId = partnerSolutionId;
        return this;
    }

    public String getSignedFieldNames() {
        return signedFieldNames;
    }

    public CheckoutApiInitialization setSignedFieldNames(String signedFieldNames) {
        this.signedFieldNames = signedFieldNames;
        return this;
    }

    public String getUnsignedFieldNames() {
        return unsignedFieldNames;
    }

    public CheckoutApiInitialization setUnsignedFieldNames(String unsignedFieldNames) {
        this.unsignedFieldNames = unsignedFieldNames;
        return this;
    }
}
