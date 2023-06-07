package com.readme.payments.payments.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetPurchasedInfoDto {

    String id;
    List<Long> episodeIds;
}
