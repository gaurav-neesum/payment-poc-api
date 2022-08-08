package com.judopayweb.dao;

import com.judopayweb.domain.JudopayPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JudopayPaymentDao extends JpaRepository<JudopayPayment, Integer> {
}
