package com.readme.payments.payments.messageQueue.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.readme.payments.payments.dto.PurchaseEpisodeDto;
import com.readme.payments.sseEmitter.repository.SseEmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseEpisodeProducer {

    private final SseEmitterRepository sseEmitterRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public SseEmitter sendPurchaseEpisode(String uuid, Long episodeId) {

        SseEmitter emitter = new SseEmitter();
        ObjectMapper mapper = new ObjectMapper();
        String id = uuid + System.currentTimeMillis();

        PurchaseEpisodeDto purchaseEpisodeDto =
            PurchaseEpisodeDto.builder()
                .id(id)
                .uuid(uuid)
                .episodeId(episodeId)
                .build();

        try {
            String data = mapper.writeValueAsString(purchaseEpisodeDto);
            kafkaTemplate.send("purchaseEpisode", data);
            return sseEmitterRepository.save(id, emitter);
        } catch (Exception e) {
            log.info(e.toString());
            return null;
        }
    }
}
