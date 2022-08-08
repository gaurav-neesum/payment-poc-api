package com.cybersource;

public class Response {

    public String responseMessage;
    public String responseCode;
    public String VcCorelationId;

    public Response() {
    }

    public String getVcCorelationId() {
        return this.VcCorelationId;
    }

    public void setVcCorelationId(String resVcCorelationId) {
        this.VcCorelationId = resVcCorelationId;
    }

    public String getResponseMessage() {
        return this.responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseCode() {
        return this.responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
}
