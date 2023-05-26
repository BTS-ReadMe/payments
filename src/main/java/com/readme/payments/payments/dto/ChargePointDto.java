package com.readme.payments.payments.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChargePointDto {

    private String uuid;
    private Integer point;
}
