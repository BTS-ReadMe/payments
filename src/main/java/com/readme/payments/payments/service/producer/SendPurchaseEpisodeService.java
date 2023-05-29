package com.readme.payments.payments.service.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.readme.payments.payments.dto.PurchaseEpisodeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendPurchaseEpisodeService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPurchaseEpisode(String topic, PurchaseEpisodeDto purchaseEpisodeDto) {
        ObjectMapper mapper = new ObjectMapper();
        String data = "";

        try {
            data = mapper.writeValueAsString(purchaseEpisodeDto);
        } catch (Exception e) {
            return;
        }

        kafkaTemplate.send(topic, data);
    }
}
