package com.readme.payments.requestObject;

import lombok.Getter;

@Getter
public class RequestReady {

    private Long novelId;
    private Integer point;
    private String uuid;

    public void setNovelId(Long novelId) {
        this.novelId = novelId;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
