package com.readme.payments.payments.messageQueue.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.readme.payments.payments.dto.EpisodeNovelDto;
import com.readme.payments.payments.dto.GetPurchasedInfoResultDto;
import com.readme.payments.payments.model.PurchaseRecord;
import com.readme.payments.payments.repository.PurchaseRepository;
import com.readme.payments.payments.responseObject.ResponseGetPurchasedInfo;
import com.readme.payments.sseEmitter.repository.SseEmitterRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetPurchasedInfoConsumer {

    private final PurchaseRepository purchaseRepository;
    private final SseEmitterRepository sseEmitterRepository;

    @KafkaListener(id = "getPurchasedInfoResult", topics = "getPurchasedInfoResult")
    public void listen(String kafkaMessage) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        List<ResponseGetPurchasedInfo> responseGetPurchasedInfos = new ArrayList<>();

        GetPurchasedInfoResultDto result = mapper.readValue(kafkaMessage,
            GetPurchasedInfoResultDto.class);
        SseEmitter emitter = sseEmitterRepository.findById(result.getId());

        if (emitter != null) {
            List<PurchaseRecord> list = purchaseRepository.findAllByUuid(
                result.getId().split("_")[0]);

            int index = 0;
            for (EpisodeNovelDto episodeNovelDto : result.getPurchased()) {
                responseGetPurchasedInfos.add(ResponseGetPurchasedInfo.builder()
                    .novelId(episodeNovelDto.getNovelId())
                    .novelTitle(episodeNovelDto.getNovelTitle())
                    .grade(episodeNovelDto.getGrade())
                    .thumbnail(episodeNovelDto.getThumbnail())
                    .episodeTitle(episodeNovelDto.getEpisodeTitle())
                    .purchasedDate(list.get(index).getCreateDate())
                    .purchasedId(list.get(index).getId())
                    .build());
                index += 1;
            }

            Comparator<ResponseGetPurchasedInfo> comparator = Comparator.comparingLong(ResponseGetPurchasedInfo::getPurchasedId).reversed();
            Collections.sort(responseGetPurchasedInfos, comparator);

            String jsonMessage = mapper.writeValueAsString(responseGetPurchasedInfos);
            emitter.send(SseEmitter.event().data(jsonMessage).name("PurchasedInfoResult"));
            sseEmitterRepository.deleteById(result.getId());
        }
    }
}
