package com.readme.payments.payments.messageQueue.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.readme.payments.payments.dto.PurchaseEpisodeResultDto;
import com.readme.payments.payments.model.PurchaseRecord;
import com.readme.payments.payments.repository.PurchaseRepository;
import com.readme.payments.payments.responseObject.ResponsePurchaseEpisode;
import com.readme.payments.sseEmitter.repository.SseEmitterRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class PurchaseEpisodeConsumer {

    private final PurchaseRepository purchaseRepository;
    private final SseEmitterRepository sseEmitterRepository;

    @KafkaListener(id = "purchaseEpisodeResult", topics = "purchaseEpisodeResult")
    public void listen(String kafkaMessage) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        ResponsePurchaseEpisode responsePurchaseEpisode = new ResponsePurchaseEpisode();

        PurchaseEpisodeResultDto result = mapper.readValue(kafkaMessage,
            PurchaseEpisodeResultDto.class);
        String id = result.getId();
        String uuid = result.getUuid();
        Long episodeId = result.getEpisodeId();
        LocalDateTime purchasedDate = result.getPurchasedDate();

        responsePurchaseEpisode.setUuid(uuid);
        responsePurchaseEpisode.setEpisodeId(episodeId);
        responsePurchaseEpisode.setPurchasedDate(purchasedDate);
        SseEmitter emitter = sseEmitterRepository.findById(id);

        if (emitter != null && result.getResult().equals(true)) {
            purchaseRepository.save(PurchaseRecord.builder()
                .uuid(uuid)
                .episodeId(episodeId)
                .build());

            responsePurchaseEpisode.setResult("success");
        } else {
            responsePurchaseEpisode.setResult("fail");
        }

        String jsonMessage = mapper.writeValueAsString(responsePurchaseEpisode);
        emitter.send(SseEmitter.event().data(jsonMessage).name("purchaseEpisodeResult"));
        sseEmitterRepository.deleteById(result.getId());
    }
}
