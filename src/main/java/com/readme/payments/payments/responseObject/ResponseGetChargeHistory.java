package com.readme.payments.payments.responseObject;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseGetChargeHistory {

    private Integer price;
    private LocalDateTime date;

    public ResponseGetChargeHistory(Integer price, LocalDateTime createDate) {
        this.price = price;
        date = createDate;
    }
}
