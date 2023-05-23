package com.readme.payments.controller;

import com.readme.payments.requestObject.RequestCharge;
import com.readme.payments.requestObject.RequestPurchase;
import com.readme.payments.service.PaymentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payment")
public class paymentsController {

    private final PaymentsService paymentsService;

    @PostMapping("/charge")
    public String chargePoint(RequestCharge requestCharge){
        return paymentsService.chargePoint(requestCharge);
    }

    @PostMapping("/pay")
    public String purchase(RequestPurchase requestPurchase){
        return paymentsService.purchase(requestPurchase);
    }

}