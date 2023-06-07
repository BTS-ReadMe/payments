package com.readme.payments.payments.messageQueue.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.readme.payments.payments.dto.GetPurchasedInfoDto;
import com.readme.payments.payments.model.PurchaseRecord;
import com.readme.payments.payments.repository.PurchaseRepository;
import com.readme.payments.sseEmitter.repository.SseEmitterRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetPurchasedInfoProducer {

    private final SseEmitterRepository sseEmitterRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final PurchaseRepository purchaseRepository;

    public SseEmitter sendPurchasedInfo(String uuid) {

        SseEmitter emitter = new SseEmitter();
        ObjectMapper mapper = new ObjectMapper();
        String id = uuid + "_" + System.currentTimeMillis();

        List<PurchaseRecord> records = purchaseRepository.findAllByUuid(uuid);
        List<Long> episodeIds = records.stream().map(PurchaseRecord::getEpisodeId)
            .collect(Collectors.toList());

        GetPurchasedInfoDto getPurchasedInfoDto =
            GetPurchasedInfoDto.builder()
                .id(id)
                .episodeIds(episodeIds)
                .build();

        try {
            String data = mapper.writeValueAsString(getPurchasedInfoDto);
            kafkaTemplate.send("getPurchasedInfo", data);
            return sseEmitterRepository.save(id, emitter);
        } catch (Exception e) {
            log.info(e.toString());
            return null;
        }
    }
}
