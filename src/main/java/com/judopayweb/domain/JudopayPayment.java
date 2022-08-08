package com.judopayweb.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class JudopayPayment {
    @Id
    @GeneratedValue
    private Integer id;
    private Double amount;
    private LocalDateTime expiryDateTime;
    private String judopayReference;

    public Integer getId() {
        return id;
    }

    public JudopayPayment setId(Integer id) {
        this.id = id;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public JudopayPayment setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public LocalDateTime getExpiryDateTime() {
        return expiryDateTime;
    }

    public JudopayPayment setExpiryDateTime(LocalDateTime expiryDateTime) {
        this.expiryDateTime = expiryDateTime;
        return this;
    }

    public String getJudopayReference() {
        return judopayReference;
    }

    public JudopayPayment setJudopayReference(String judopayReference) {
        this.judopayReference = judopayReference;
        return this;
    }
}
