package com.judopayweb.service;

import com.judopayweb.dto.response.PaymentSessionResponse;

import java.io.IOException;

public interface PaymentService {

    PaymentSessionResponse initiatePayment(double amount) throws IOException;
}
