package com.readme.payments.payments.responseObject;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponsePurchaseEpisode {

    private String uuid;
    private Long episodeId;
    private String result;
    private LocalDateTime purchasedDate;
}
