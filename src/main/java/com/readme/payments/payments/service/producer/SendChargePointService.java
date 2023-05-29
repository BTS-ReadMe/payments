package com.readme.payments.payments.service.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.readme.payments.payments.dto.ChargePointDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendChargePointService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendChargePoint(String topic, ChargePointDto chargePointDto) {
        ObjectMapper mapper = new ObjectMapper();
        String data = "";

        mapper.registerModule(new JavaTimeModule());

        try {
            data = mapper.writeValueAsString(chargePointDto);
        } catch (Exception e) {
            return;
        }

        kafkaTemplate.send(topic, data);
    }
}
