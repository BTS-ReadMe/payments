package com.readme.payments.payments.service;

import com.readme.payments.payments.requestObject.RequestApprove;
import com.readme.payments.payments.requestObject.RequestPurchase;
import com.readme.payments.payments.requestObject.RequestReady;
import com.readme.payments.payments.responseObject.Message;
import com.readme.payments.payments.responseObject.ResponseCheckPurchased;
import com.readme.payments.payments.responseObject.ResponseGetChargeHistory;
import com.readme.payments.payments.responseObject.ResponseReady;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface PaymentsService {

    public ResponseEntity<Message<ResponseReady>> ready(RequestReady requestReady);

    public ResponseEntity<SseEmitter> approve(RequestApprove requestApprove);

    public ResponseEntity<SseEmitter> purchase(RequestPurchase requestPurchase);

    public ResponseEntity<Message<List<ResponseGetChargeHistory>>> getAllChargeHistory(String uuid);

    public ResponseEntity<Message<ResponseCheckPurchased>> checkPurchased(String uuid, Long episodeId);
}
