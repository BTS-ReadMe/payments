package com.readme.payments.payments.controller;

import com.readme.payments.payments.requestObject.RequestApprove;
import com.readme.payments.payments.requestObject.RequestPurchase;
import com.readme.payments.payments.requestObject.RequestReady;
import com.readme.payments.payments.responseObject.Message;
import com.readme.payments.payments.responseObject.ResponseCheckPurchased;
import com.readme.payments.payments.responseObject.ResponseGetChargeHistory;
import com.readme.payments.payments.responseObject.ResponseReady;
import com.readme.payments.payments.service.PaymentsService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payments")
public class paymentsController {

    private final PaymentsService paymentsService;

    @Operation(summary = "포인트 충전 준비", description = "결제 전 필요 정보 받기", tags = {"결제"})
    @PostMapping("/ready")
    public ResponseEntity<Message<ResponseReady>> purchaseReady(
        @RequestBody RequestReady requestReady) {
        return paymentsService.ready(requestReady);
    }

    @Operation(summary = "포인트 충전 요청하기", description = "결제", tags = {"결제"})
    @PostMapping("/approve")
    public SseEmitter purchaseApprove(@RequestBody RequestApprove requestApprove) {
        return paymentsService.approve(requestApprove);
    }

    @Operation(summary = "포인트 충전 내역 조회", description = "포인트 충전 내역 전체 보기", tags = {"결제"})
    @GetMapping("/chargeHistory")
    public ResponseEntity<Message<List<ResponseGetChargeHistory>>> getAllChargeHistory(
        @RequestHeader(value = "uuid") String uuid) {
        return paymentsService.getAllChargeHistory(uuid);
    }

    @Operation(summary = "에피소드 구매(100원)", description = "결제", tags = {"결제"})
    @PostMapping("/purchase")
    public SseEmitter purchaseEpisode(@RequestBody RequestPurchase requestPurchase) {
        return paymentsService.purchase(requestPurchase);
    }

    @Operation(summary = "에피소드 구매 확인", description = "에피소드 결제했는지 확인", tags = {"결제"})
    @GetMapping("/checkPurchased")
    public ResponseEntity<Message<ResponseCheckPurchased>> purchaseEpisode(
        @RequestHeader(value = "uuid") String uuid,
        @RequestParam Long episodeId) {
        return paymentsService.checkPurchased(uuid, episodeId);
    }
}