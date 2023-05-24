package com.readme.payments.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.readme.payments.requestObject.RequestPurchase;

public interface PaymentsService{

    public String purchaseItem(RequestPurchase requestPurchase) throws JsonProcessingException;

}
