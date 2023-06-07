package com.readme.payments.payments.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPurchasedInfoResultDto {

    String id;
    private List<EpisodeNovelDto> purchased;

}
