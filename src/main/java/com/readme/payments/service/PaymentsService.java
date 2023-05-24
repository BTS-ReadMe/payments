package com.readme.payments.service;

import com.readme.payments.requestObject.RequestReady;
import com.readme.payments.responseObject.Message;
import com.readme.payments.responseObject.ResponseReady;
import org.springframework.http.ResponseEntity;

public interface PaymentsService{

    public ResponseEntity<Message<ResponseReady>> ready(RequestReady requestReady);

}
