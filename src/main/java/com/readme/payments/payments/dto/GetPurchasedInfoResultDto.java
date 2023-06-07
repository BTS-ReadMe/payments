package com.readme.payments.payments.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPurchasedInfoResultDto {

    String id;
    private List<Episode> purchased;

    @Setter
    @Getter
    public class Episode{
        Long novelId;
        String novelTitle;
        int grade;
        String thumbnail;
        String episodeTitle;
        LocalDateTime purchasedDate;
    }

}
