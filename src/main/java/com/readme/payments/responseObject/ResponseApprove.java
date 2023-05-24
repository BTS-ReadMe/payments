package com.readme.payments.responseObject;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseApprove {

    private Integer amount;
    private Integer point;
//    private Integer totalPoint;
    private LocalDateTime purchaseDate;
}
