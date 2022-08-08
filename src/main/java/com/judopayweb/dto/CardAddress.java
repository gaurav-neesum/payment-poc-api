package com.judopayweb.dto;


import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "address1",
        "address2",
        "address3",
        "town",
        "postCode",
        "countryCode",
        "state",
        "cardHolderName"
})
@Generated("jsonschema2pojo")
public class CardAddress {

    @JsonProperty("address1")
    private String address1;
    @JsonProperty("address2")
    private String address2;
    @JsonProperty("address3")
    private String address3;
    @JsonProperty("town")
    private String town;
    @JsonProperty("postCode")
    private String postCode;
    @JsonProperty("countryCode")
    private Integer countryCode;
    @JsonProperty("state")
    private String state;
    @JsonProperty("cardHolderName")
    private String cardHolderName;

    public String getAddress1() {
        return address1;
    }

    public CardAddress setAddress1(String address1) {
        this.address1 = address1;
        return this;
    }

    public String getAddress2() {
        return address2;
    }

    public CardAddress setAddress2(String address2) {
        this.address2 = address2;
        return this;
    }

    public String getAddress3() {
        return address3;
    }

    public CardAddress setAddress3(String address3) {
        this.address3 = address3;
        return this;
    }

    public String getTown() {
        return town;
    }

    public CardAddress setTown(String town) {
        this.town = town;
        return this;
    }

    public String getPostCode() {
        return postCode;
    }

    public CardAddress setPostCode(String postCode) {
        this.postCode = postCode;
        return this;
    }

    public Integer getCountryCode() {
        return countryCode;
    }

    public CardAddress setCountryCode(Integer countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public String getState() {
        return state;
    }

    public CardAddress setState(String state) {
        this.state = state;
        return this;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public CardAddress setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
        return this;
    }
}
