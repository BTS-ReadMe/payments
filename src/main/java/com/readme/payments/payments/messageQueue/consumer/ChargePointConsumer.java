package com.readme.payments.payments.messageQueue.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.readme.payments.payments.dto.ChargePointResultDto;
import com.readme.payments.payments.model.ChargeRecord;
import com.readme.payments.payments.repository.ChargeRepository;
import com.readme.payments.payments.responseObject.ResponseChargePoint;
import com.readme.payments.sseEmitter.repository.SseEmitterRepository;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class ChargePointConsumer {

    private final ChargeRepository chargeRepository;
    private final SseEmitterRepository sseEmitterRepository;

    @KafkaListener(id = "chargePointResult", topics = "chargePointResult")
    public void listen(String kafkaMessage) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        ResponseChargePoint responseChargePoint = new ResponseChargePoint();

        ChargePointResultDto result = mapper.readValue(kafkaMessage, ChargePointResultDto.class);
        SseEmitter emitter = sseEmitterRepository.findById(result.getId());

        if (emitter != null) {
            chargeRepository.save(ChargeRecord.builder()
                .uuid(result.getUuid())
                .price(result.getPoint())
                .build());

            responseChargePoint.setUuid(result.getUuid());
            responseChargePoint.setTotal(result.getTotal());
            responseChargePoint.setPoint(result.getPoint());
            responseChargePoint.setChargeDate(result.getChargedDate());
        }

        String jsonMessage = mapper.writeValueAsString(responseChargePoint);
        emitter.send(SseEmitter.event().data(jsonMessage).name("chargePointResult"));
        sseEmitterRepository.deleteById(result.getId());
    }
}
