package com.readme.payments.requestObject;

import lombok.Getter;

@Getter
public class RequestCharge {

    private Integer point;
    private String uuid;

    public void setPoint(Integer point) {
        this.point = point;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
