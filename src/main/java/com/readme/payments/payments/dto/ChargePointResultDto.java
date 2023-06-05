package com.readme.payments.payments.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChargePointResultDto {

    private String id;
    private String uuid;
    private Integer total;
    private Integer point;
    private LocalDateTime chargedDate;
}
