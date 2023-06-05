package com.readme.payments.payments.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PurchaseEpisodeDto {

    private String id;
    private String uuid;
    private Long episodeId;
}
