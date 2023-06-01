package com.readme.payments.payments.service;

import com.readme.payments.payments.requestObject.RequestApprove;
import com.readme.payments.payments.requestObject.RequestGetAllChargeHistory;
import com.readme.payments.payments.requestObject.RequestPurchase;
import com.readme.payments.payments.requestObject.RequestReady;
import com.readme.payments.payments.responseObject.Message;
import com.readme.payments.payments.responseObject.ResponseGetAllChargeHistory;
import com.readme.payments.payments.responseObject.ResponseReady;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface PaymentsService{

    public ResponseEntity<Message<ResponseReady>> ready(RequestReady requestReady);

    public SseEmitter approve(RequestApprove requestApprove);

    public SseEmitter purchase(RequestPurchase requestPurchase);

    public ResponseEntity<Message<ResponseGetAllChargeHistory>> getAllChargeHistory(
        RequestGetAllChargeHistory requestGetAllChargeHistory);
}
