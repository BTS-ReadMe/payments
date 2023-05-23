package com.readme.payments.service;

import com.readme.payments.requestObject.RequestCharge;
import com.readme.payments.requestObject.RequestPurchase;

public interface PaymentsService {

    public String chargePoint(RequestCharge requestCharge);

    public String purchase(RequestPurchase requestPurchase);
}
