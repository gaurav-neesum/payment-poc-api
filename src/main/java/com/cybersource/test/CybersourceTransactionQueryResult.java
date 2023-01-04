package com.cybersource.test;

import com.cybersource.test.backgrd.TransactionAction;

public class CybersourceTransactionQueryResult {
    private String cybersourceTransactionId;
    private String cybersourceTransactionUUID;
    private String status;
    private String returnCode;
    private TransactionAction transactionAction;

    public String getCybersourceTransactionId() {
        return cybersourceTransactionId;
    }

    public void setCybersourceTransactionId(String cybersourceTransactionId) {
        this.cybersourceTransactionId = cybersourceTransactionId;
    }

    public String getCybersourceTransactionUUID() {
        return cybersourceTransactionUUID;
    }

    public void setCybersourceTransactionUUID(String cybersourceTransactionUUID) {
        this.cybersourceTransactionUUID = cybersourceTransactionUUID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }


    public TransactionAction getTransactionAction() {
        return transactionAction;
    }

    public void setTransactionAction(TransactionAction transactionAction) {
        this.transactionAction = transactionAction;
    }
}
