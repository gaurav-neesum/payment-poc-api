package com.judopayweb.dto;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "judoId",
        "yourConsumerReference",
        "yourPaymentReference",
        "yourPaymentMetaData",
        "currency",
        "amount",
        "cardAddress",
        "expiryDate",
        "isPayByLink",
        "isJudoAccept",
        "paymentSuccessUrl",
        "paymentCancelUrl",
        "emailAddress",
        "mobileNumber",
        "phoneCountryCode",
        "threeDSecure",
        "hideBillingInfo",
        "hideReviewInfo",
        "primaryAccountDetails"
})
@Generated("jsonschema2pojo")
public class JudopayRequest {

    @JsonProperty("judoId")
    private String judoId;
    @JsonProperty("yourConsumerReference")
    private String yourConsumerReference;
    @JsonProperty("yourPaymentReference")
    private String yourPaymentReference;
    @JsonProperty("yourPaymentMetaData")
    private YourPaymentMetaData yourPaymentMetaData;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("cardAddress")
    private CardAddress cardAddress;
    @JsonProperty("expiryDate")
    private String expiryDate;
    @JsonProperty("isPayByLink")
    private Boolean isPayByLink;
    @JsonProperty("isJudoAccept")
    private Boolean isJudoAccept;
    @JsonProperty("paymentSuccessUrl")
    private String paymentSuccessUrl;
    @JsonProperty("paymentCancelUrl")
    private String paymentCancelUrl;
    @JsonProperty("emailAddress")
    private String emailAddress;
    @JsonProperty("mobileNumber")
    private String mobileNumber;
    @JsonProperty("phoneCountryCode")
    private String phoneCountryCode;
    @JsonProperty("threeDSecure")
    private ThreeDSecure threeDSecure;
    @JsonProperty("hideBillingInfo")
    private Boolean hideBillingInfo;
    @JsonProperty("hideReviewInfo")
    private Boolean hideReviewInfo;
    @JsonProperty("primaryAccountDetails")
    private PrimaryAccountDetails primaryAccountDetails;

    public String getJudoId() {
        return judoId;
    }

    public JudopayRequest setJudoId(String judoId) {
        this.judoId = judoId;
        return this;
    }

    public String getYourConsumerReference() {
        return yourConsumerReference;
    }

    public JudopayRequest setYourConsumerReference(String yourConsumerReference) {
        this.yourConsumerReference = yourConsumerReference;
        return this;
    }

    public String getYourPaymentReference() {
        return yourPaymentReference;
    }

    public JudopayRequest setYourPaymentReference(String yourPaymentReference) {
        this.yourPaymentReference = yourPaymentReference;
        return this;
    }

    public YourPaymentMetaData getYourPaymentMetaData() {
        return yourPaymentMetaData;
    }

    public JudopayRequest setYourPaymentMetaData(YourPaymentMetaData yourPaymentMetaData) {
        this.yourPaymentMetaData = yourPaymentMetaData;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public JudopayRequest setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public JudopayRequest setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public CardAddress getCardAddress() {
        return cardAddress;
    }

    public JudopayRequest setCardAddress(CardAddress cardAddress) {
        this.cardAddress = cardAddress;
        return this;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public JudopayRequest setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    public Boolean getPayByLink() {
        return isPayByLink;
    }

    public JudopayRequest setPayByLink(Boolean payByLink) {
        isPayByLink = payByLink;
        return this;
    }

    public Boolean getJudoAccept() {
        return isJudoAccept;
    }

    public JudopayRequest setJudoAccept(Boolean judoAccept) {
        isJudoAccept = judoAccept;
        return this;
    }

    public String getPaymentSuccessUrl() {
        return paymentSuccessUrl;
    }

    public JudopayRequest setPaymentSuccessUrl(String paymentSuccessUrl) {
        this.paymentSuccessUrl = paymentSuccessUrl;
        return this;
    }

    public String getPaymentCancelUrl() {
        return paymentCancelUrl;
    }

    public JudopayRequest setPaymentCancelUrl(String paymentCancelUrl) {
        this.paymentCancelUrl = paymentCancelUrl;
        return this;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public JudopayRequest setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public JudopayRequest setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        return this;
    }

    public String getPhoneCountryCode() {
        return phoneCountryCode;
    }

    public JudopayRequest setPhoneCountryCode(String phoneCountryCode) {
        this.phoneCountryCode = phoneCountryCode;
        return this;
    }

    public ThreeDSecure getThreeDSecure() {
        return threeDSecure;
    }

    public JudopayRequest setThreeDSecure(ThreeDSecure threeDSecure) {
        this.threeDSecure = threeDSecure;
        return this;
    }

    public Boolean getHideBillingInfo() {
        return hideBillingInfo;
    }

    public JudopayRequest setHideBillingInfo(Boolean hideBillingInfo) {
        this.hideBillingInfo = hideBillingInfo;
        return this;
    }

    public Boolean getHideReviewInfo() {
        return hideReviewInfo;
    }

    public JudopayRequest setHideReviewInfo(Boolean hideReviewInfo) {
        this.hideReviewInfo = hideReviewInfo;
        return this;
    }

    public PrimaryAccountDetails getPrimaryAccountDetails() {
        return primaryAccountDetails;
    }

    public JudopayRequest setPrimaryAccountDetails(PrimaryAccountDetails primaryAccountDetails) {
        this.primaryAccountDetails = primaryAccountDetails;
        return this;
    }
}
