package com.readme.payments.payments.responseObject;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseGetPurchasedInfo {

    Long novelId;
    String novelTitle;
    int grade;
    String thumbnail;
    String episodeTitle;
    LocalDateTime purchasedDate;
}
