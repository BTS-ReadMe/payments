package com.readme.payments.payments.service.sseEmitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.base.Charsets;
import com.readme.payments.payments.dto.ChargePointDto;
import com.readme.payments.payments.dto.ChargePointResultDto;
import com.readme.payments.payments.dto.PurchaseEpisodeResultDto;
import com.readme.payments.payments.model.ChargeRecord;
import com.readme.payments.payments.model.PurchaseRecord;
import com.readme.payments.payments.repository.ChargeRepository;
import com.readme.payments.payments.responseObject.Message;
import com.readme.payments.payments.responseObject.ResponseChargePoint;
import com.readme.payments.payments.responseObject.ResponsePurchase;
import com.readme.payments.payments.service.producer.SendChargePointService;
import java.io.IOException;
import java.time.LocalDateTime;
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
public class ChargePointService {

    private Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final SendChargePointService sendChargePointService;
    private final ChargeRepository chargeRepository;

    public SseEmitter sendChargePoint(String id, String message) {

        SseEmitter emitter = new SseEmitter();
        ChargePointDto chargePointDto =
            ChargePointDto.builder()
                .id(id)
                .point(Integer.valueOf(message.split("_")[1]))
                .build();
        sendChargePointService.sendChargePoint("chargePoint", chargePointDto);
        this.emitters.put(id, emitter);
        return emitter;
    }

    @KafkaListener(id = "chargePointResult", topics = "chargePointResult")
    public void listen(String kafkaMessage) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charsets.UTF_8));

        Message message = new Message();
        ResponseChargePoint responseChargePoint = new ResponseChargePoint();

        ChargePointResultDto result = mapper.readValue(kafkaMessage, ChargePointResultDto.class);
        SseEmitter emitter = this.emitters.get(result.getId());
        if (emitter != null) {

            chargeRepository.save(ChargeRecord.builder()
                .uuid(result.getId().split("_")[0])
                .price(result.getPoint())
                .build());

            responseChargePoint.setTotal(result.getTotal());
            responseChargePoint.setPoint(result.getPoint());
            responseChargePoint.setChargeDate(result.getChargeDate());
        }

        message.setData(responseChargePoint);

        emitter.send(ResponseEntity.status(HttpStatus.OK).headers(headers).body(message));
        emitter.complete();
    }

    private LocalDateTime convertToDateTime(int[] arr) {
        int year = arr[0];
        int month = arr[1];
        int day = arr[2];
        int hour = arr[3];
        int minute = arr[4];
        int second = arr[5];
        return LocalDateTime.of(year, month, day, hour, minute, second);
    }
}
