package com.readme.payments.payments.service.sseEmitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.readme.payments.payments.dto.PurchaseEpisodeDto;
import com.readme.payments.payments.dto.PurchaseEpisodeResultDto;
import com.readme.payments.payments.model.PurchaseRecord;
import com.readme.payments.payments.repository.PurchaseRepository;
import com.readme.payments.payments.responseObject.Message;
import com.readme.payments.payments.responseObject.ResponsePurchase;
import com.readme.payments.payments.service.producer.SendPurchaseEpisodeService;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class SendPurchaseEpisodeResultService {

    private Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final SendPurchaseEpisodeService sendPurchaseEpisodeService;
    private final PurchaseRepository purchaseRepository;

    public SseEmitter sendPurchaseEpisode(String id, String message) {
        SseEmitter emitter = new SseEmitter();
        PurchaseEpisodeDto purchaseEpisodeDto = new PurchaseEpisodeDto();
        purchaseEpisodeDto.setId(id);
        purchaseEpisodeDto.setMessage(message);
        sendPurchaseEpisodeService.sendPurchaseEpisode(purchaseEpisodeDto);
        this.emitters.put(id, emitter);
        return emitter;
    }

    @KafkaListener(id = "purchaseEpisodeResult", topics = "purchaseEpisodeResult")
    public void listen(String kafkaMessage) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charsets.UTF_8));

        Message message=new Message();
        ResponsePurchase responsePurchase=new ResponsePurchase();

        PurchaseEpisodeResultDto result = mapper.readValue(kafkaMessage, PurchaseEpisodeResultDto.class);
        SseEmitter emitter = this.emitters.get(result.getId());
        if (emitter != null) {
            if (result.getResult().equals(true)) {
                String[] parts = result.getId().split("_");
                purchaseRepository.save(PurchaseRecord.builder()
                    .uuid(parts[0])
                    .episodeId(Long.valueOf(parts[1]))
                    .build());
                responsePurchase.setResult("success");
            }
            else{
                responsePurchase.setResult("fail");
            }
        }

        message.setData(responsePurchase);

        emitter.send(ResponseEntity.status(HttpStatus.OK).headers(headers).body(message));
        emitter.complete();
    }
}

