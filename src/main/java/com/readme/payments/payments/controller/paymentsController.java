package com.readme.payments.payments.controller;

import com.readme.payments.payments.requestObject.RequestApprove;
import com.readme.payments.payments.requestObject.RequestPurchase;
import com.readme.payments.payments.requestObject.RequestReady;
import com.readme.payments.payments.responseObject.Message;
import com.readme.payments.payments.responseObject.ResponseApprove;
import com.readme.payments.payments.responseObject.ResponseReady;
import com.readme.payments.payments.service.PaymentsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payments")
public class paymentsController {

    private final PaymentsService paymentsService;

    @Operation(summary = "결제 준비", description = "결제 전 필요 정보 받기", tags = {"결제"})
    @PostMapping("/ready")
    public ResponseEntity<Message<ResponseReady>> purchaseReady(@RequestBody RequestReady requestReady) {
       return paymentsService.ready(requestReady);
    }

    @Operation(summary = "결제 요청하기", description = "결제", tags = {"결제"})
    @PostMapping("/approve")
    public ResponseEntity<Message<ResponseApprove>> purchaseApprove(@RequestBody RequestApprove requestApprove){
        return paymentsService.approve(requestApprove);
    }

    @Operation(summary = "에피소드 구매(100원)", description = "결제", tags = {"결제"})
    @PostMapping("/purchase")
    public SseEmitter purchaseEpisode(@RequestBody RequestPurchase requestPurchase){
        return paymentsService.purchase(requestPurchase);
    }
}