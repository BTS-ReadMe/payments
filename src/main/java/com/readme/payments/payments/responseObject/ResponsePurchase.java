package com.readme.payments.payments.responseObject;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Getter
@Setter
public class ResponsePurchase {

    private String result;
}
