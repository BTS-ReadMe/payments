package com.readme.payments.payments.responseObject;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseChargePoint {

    private Integer total;
    private Integer point;
    private LocalDateTime chargeDate;
}
