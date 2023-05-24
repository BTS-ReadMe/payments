package com.readme.payments.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.readme.payments.requestObject.RequestPurchase;
import com.readme.payments.responseObject.Message;
import com.readme.payments.responseObject.ResponseReady;
import org.springframework.http.ResponseEntity;

public interface PaymentsService{

    public ResponseEntity<Message<ResponseReady>> purchaseItem(RequestPurchase requestPurchase) throws JsonProcessingException;

}
