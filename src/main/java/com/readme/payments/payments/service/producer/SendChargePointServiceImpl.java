package com.readme.payments.payments.service.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.readme.payments.payments.dto.ChargePointDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendChargePointServiceImpl implements SendChargePointService{

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendChargePoint(String topic, ChargePointDto chargePointDto) {
        ObjectMapper mapper = new ObjectMapper();
        String data = "";

        mapper.registerModule(new JavaTimeModule());
//        mapper.disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);

        try {
            data = mapper.writeValueAsString(chargePointDto);
        } catch (Exception e) {
            return;
        }

        kafkaTemplate.send(topic, data);
    }
}
