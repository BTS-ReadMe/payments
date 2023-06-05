package com.readme.payments.payments.messageQueue.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.readme.payments.payments.dto.ChargePointDto;
import com.readme.payments.sseEmitter.repository.SseEmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChargePointProducer {

    private final SseEmitterRepository sseEmitterRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public SseEmitter sendChargePoint(String uuid, Integer point) {

        SseEmitter emitter = new SseEmitter();
        ObjectMapper mapper = new ObjectMapper();
        String id = uuid + System.currentTimeMillis();

        ChargePointDto chargePointDto =
            ChargePointDto.builder()
                .id(id)
                .uuid(uuid)
                .point(point)
                .build();

        try {
            String data = mapper.writeValueAsString(chargePointDto);
            kafkaTemplate.send("chargePoint", data);
            return sseEmitterRepository.save(id, emitter);
        } catch (Exception e) {
            log.info(e.toString());
            return null;
        }
    }
}
