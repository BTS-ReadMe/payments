package com.readme.payments.payments.messageQueue.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.readme.payments.payments.dto.GetPurchasedInfoResultDto;
import com.readme.payments.payments.dto.GetPurchasedInfoResultDto.Episode;
import com.readme.payments.payments.model.PurchaseRecord;
import com.readme.payments.payments.repository.PurchaseRepository;
import com.readme.payments.payments.responseObject.ResponseGetPurchasedInfo;
import com.readme.payments.sseEmitter.repository.SseEmitterRepository;
import java.io.IOException;
import java.time.LocalDateTime;
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
        List<ResponseGetPurchasedInfo> responseGetPurchasedInfos = null;

        GetPurchasedInfoResultDto result = mapper.readValue(kafkaMessage,
            GetPurchasedInfoResultDto.class);
        SseEmitter emitter = sseEmitterRepository.findById(result.getId());

        if (emitter != null) {
            List<PurchaseRecord> list = purchaseRepository.findAllByUuid(
                result.getId().split("_")[0]);
            List<LocalDateTime> purchasedDate = list.stream()
                .map(PurchaseRecord::getCreateDate).collect(Collectors.toList());

            int index = 0;
            for (Episode episode : result.getPurchased()) {
                responseGetPurchasedInfos.add(
                    ResponseGetPurchasedInfo.builder()
                        .novelId(episode.getNovelId())
                        .novelTitle(episode.getNovelTitle())
                        .grade(episode.getGrade())
                        .thumbnail(episode.getThumbnail())
                        .episodeTitle(episode.getEpisodeTitle())
                        .purchasedDate(purchasedDate.get(index))
                        .build());
                index += 1;
            }

            String jsonMessage = mapper.writeValueAsString(responseGetPurchasedInfos);
            emitter.send(SseEmitter.event().data(jsonMessage).name("PurchasedInfoResult"));
            sseEmitterRepository.deleteById(result.getId());
        }
    }
}
