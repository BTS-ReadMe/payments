package com.readme.payments.payments.service.producer;

import com.readme.payments.payments.dto.ChargePointDto;

public interface SendChargePointService {

    public void sendChargePoint(String topic, ChargePointDto chargePointDto);
}
