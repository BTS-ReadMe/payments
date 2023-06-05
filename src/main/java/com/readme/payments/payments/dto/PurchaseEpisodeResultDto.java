package com.readme.payments.payments.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseEpisodeResultDto {

    private String id;
    private String uuid;
    private Long episodeId;
    private Boolean result;
    private LocalDateTime purchasedDate;
}
