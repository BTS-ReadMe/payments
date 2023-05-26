package com.readme.payments.payments.repository;

import com.readme.payments.payments.model.ChargeRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargeRepository extends JpaRepository<ChargeRecord, Long> {

}
