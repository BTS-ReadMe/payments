package com.readme.payments.controller;

import com.readme.payments.requestObject.RequestPurchase;
import com.readme.payments.service.PaymentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payment")
public class paymentsController {

    private final PaymentsService paymentsService;

    @PostMapping("/purchase")
    public String purchaseItem(@RequestBody RequestPurchase requestPurchase) {
        try {
            return paymentsService.purchaseItem(requestPurchase);
        } catch (Exception e) {
            return null;
        }
    }

}