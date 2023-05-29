package com.readme.payments.payments.requestObject;

import lombok.Getter;

@Getter
public class RequestApprove {

    private String tid;
    private String partnerOrderId;
    private String uuid;
    private String pgToken;
}
