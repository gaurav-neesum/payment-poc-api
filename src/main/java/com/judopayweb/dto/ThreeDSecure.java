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
        "challengeRequestIndicator",
        "scaExemption"
})
@Generated("jsonschema2pojo")
public class ThreeDSecure {

    @JsonProperty("challengeRequestIndicator")
    private String challengeRequestIndicator;
    @JsonProperty("scaExemption")
    private String scaExemption;

    public String getChallengeRequestIndicator() {
        return challengeRequestIndicator;
    }

    public ThreeDSecure setChallengeRequestIndicator(String challengeRequestIndicator) {
        this.challengeRequestIndicator = challengeRequestIndicator;
        return this;
    }

    public String getScaExemption() {
        return scaExemption;
    }

    public ThreeDSecure setScaExemption(String scaExemption) {
        this.scaExemption = scaExemption;
        return this;
    }
}
