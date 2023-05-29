package com.readme.payments.payments.controller;

import com.readme.payments.payments.requestObject.RequestApprove;
import com.readme.payments.payments.requestObject.RequestPurchase;
import com.readme.payments.payments.requestObject.RequestReady;
import com.readme.payments.payments.responseObject.Message;
import com.readme.payments.payments.responseObject.ResponseApprove;
import com.readme.payments.payments.responseObject.ResponsePurchase;
import com.readme.payments.payments.responseObject.ResponseReady;
import com.readme.payments.payments.service.PaymentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payments")
public class paymentsController {

    private final PaymentsService paymentsService;

    @PostMapping("/ready")
    public ResponseEntity<Message<ResponseReady>> purchaseReady(@RequestBody RequestReady requestReady) {
       return paymentsService.ready(requestReady);
    }

    @PostMapping("/approve")
    public ResponseEntity<Message<ResponseApprove>> purchaseApprove(@RequestBody RequestApprove requestApprove){
        return paymentsService.approve(requestApprove);
    }

    @PostMapping("/purchase")
    public ResponseEntity<Message<ResponsePurchase>> purchaseEpisode(@RequestBody RequestPurchase requestPurchase){
        return paymentsService.purchase(requestPurchase);
    }
}