package com.readme.payments.payments.service;

import com.readme.payments.payments.requestObject.RequestApprove;
import com.readme.payments.payments.requestObject.RequestPurchase;
import com.readme.payments.payments.requestObject.RequestReady;
import com.readme.payments.payments.responseObject.Message;
import com.readme.payments.payments.responseObject.ResponseApprove;
import com.readme.payments.payments.responseObject.ResponseReady;
import org.springframework.http.ResponseEntity;

public interface PaymentsService{

    public ResponseEntity<Message<ResponseReady>> ready(RequestReady requestReady);

    public ResponseEntity<Message<ResponseApprove>> approve(RequestApprove requestApprove);

    public ResponseEntity<Message<RequestApprove>> purchase(RequestPurchase requestPurchase);
}
