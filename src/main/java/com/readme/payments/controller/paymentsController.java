package com.readme.payments.controller;

import com.readme.payments.requestObject.RequestReady;
import com.readme.payments.responseObject.Message;
import com.readme.payments.responseObject.ResponseReady;
import com.readme.payments.service.PaymentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payment")
public class paymentsController {

    private final PaymentsService paymentsService;

    @PostMapping("/ready")
    public ResponseEntity<Message<ResponseReady>> purchaseReady(@RequestBody RequestReady requestReady) {
        try {
            return paymentsService.purchaseItem(requestReady);
        } catch (Exception e) {
            return null;
        }
    }

}