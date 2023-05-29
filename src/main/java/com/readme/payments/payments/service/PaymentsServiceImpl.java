package com.readme.payments.payments.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.readme.payments.payments.dto.ChargePointDto;
import com.readme.payments.payments.model.PurchaseRecord;
import com.readme.payments.payments.repository.PurchaseRepository;
import com.readme.payments.payments.requestObject.RequestPurchase;
import com.readme.payments.payments.responseObject.ResponsePurchase;
import com.readme.payments.payments.model.ChargeRecord;
import com.readme.payments.payments.repository.ChargeRepository;
import com.readme.payments.payments.requestObject.RequestApprove;
import com.readme.payments.payments.requestObject.RequestReady;
import com.readme.payments.payments.responseObject.Message;
import com.readme.payments.payments.responseObject.ResponseApprove;
import com.readme.payments.payments.responseObject.ResponseReady;
import com.readme.payments.payments.service.producer.SendChargePointService;
import java.time.LocalDateTime;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PaymentsServiceImpl implements PaymentsService {

    private final ChargeRepository chargeRepository;
    private final PurchaseRepository purchaseRepository;
    private final SendChargePointService sendChargePointService;

    @Value("${payment.key.cid}")
    private String CID;

    @Value("${payment.key.app_admin_key}")
    private String APP_ADMIN_KEY;

    @Value("${payment.redirect_url.approval_url}")
    private String APPROVAL_URL;

    @Value("${payment.redirect_url.cancel_url}")
    private String CANCEL_URL;

    @Value("${payment.redirect_url.fail_url}")
    private String FAIL_URL;

    @Value("${payment.provider.ready_uri}")
    private String READY_URI;

    @Value("${payment.provider.approve_uri}")
    private String APPROVE_URI;

    @Override
    public ResponseEntity<Message<ResponseReady>> ready(RequestReady requestReady) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + APP_ADMIN_KEY);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("cid", CID);

        LocalDateTime localDateTime = LocalDateTime.now();

        String partnerOrderId =
            requestReady.getUuid() + generatePartnerOrderId() + localDateTime.toString();
        body.add("partner_order_id", partnerOrderId);
        body.add("partner_user_id", requestReady.getUuid());

        Long novelId = requestReady.getNovelId();
        body.add("item_name", novelId == null ? "point" : novelId.toString());

        body.add("quantity", "1");
        body.add("total_amount", requestReady.getPoint().toString());
        body.add("tax_free_amount", "0");
        body.add("approval_url", APPROVAL_URL);
        body.add("cancel_url", CANCEL_URL);
        body.add("fail_url", FAIL_URL);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response;

        response = restTemplate.exchange(
            READY_URI,
            HttpMethod.POST,
            request,
            String.class
        ); // todo: try catch

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HttpHeaders header = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charsets.UTF_8));

        Message message = new Message();

        ResponseReady responseReady = new ResponseReady();
        responseReady.setPartner_order_id(partnerOrderId);
        responseReady.setTid(jsonNode.get("tid").asText());
        responseReady.setNext_redirect_app_url(jsonNode.get("next_redirect_app_url").asText());
        responseReady.setNext_redirect_mobile_url(jsonNode.get("next_redirect_mobile_url").asText());
        responseReady.setNext_redirect_pc_url(jsonNode.get("next_redirect_pc_url").asText());
        responseReady.setAndroid_app_scheme(jsonNode.get("android_app_scheme").asText());
        responseReady.setIos_app_scheme(jsonNode.get("ios_app_scheme").asText());

        message.setData(responseReady);

        return ResponseEntity.status(HttpStatus.OK).headers(header).body(message);
    }

    @Override
    public ResponseEntity<Message<ResponseApprove>> approve(RequestApprove requestApprove) {

//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "KakaoAK " + APP_ADMIN_KEY);
//
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("cid", CID);
//        body.add("tid", requestApprove.getTid());
//        body.add("partner_order_id", requestApprove.getPartnerOrderId());
//        body.add("partner_user_id", requestApprove.getUuid());
//        body.add("pg_token", requestApprove.getPgToken());
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> response;
//
//        response = restTemplate.exchange(
//            APPROVE_URI,
//            HttpMethod.POST,
//            request,
//            String.class
//        ); // todo: try catch
//
//        String responseBody = response.getBody();
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode;
//        try {
//            jsonNode = objectMapper.readTree(responseBody);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }

        chargeRepository.save(ChargeRecord.builder()
            .uuid(requestApprove.getUuid())
//            .price(jsonNode.get("amount").get("total").asInt())
//            .chargeType(jsonNode.get("payment_method_type").asText())
                .price(10000)
                .chargeType("MONEY")
            .build());

        sendChargePointService.sendChargePoint("chargePoint",
            ChargePointDto.builder()
                .uuid(requestApprove.getUuid())
//                .point(jsonNode.get("amount").get("total").asInt())
                .point(10000)
                .build());

        Message message = new Message();

//        ResponseApprove responseApprove = new ResponseApprove();
//        responseApprove.setAmount(jsonNode.get("amount").get("total").asInt());
//        responseApprove.setPoint(jsonNode.get("amount").get("total").asInt());
//        responseApprove.setPurchaseDate(LocalDateTime.parse(jsonNode.get("created_at").asText()));
//
//        message.setData(responseApprove);

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @Override
    public ResponseEntity<Message<ResponsePurchase>> purchase(RequestPurchase requestPurchase) {

        purchaseRepository.save(PurchaseRecord.builder()
                .uuid(requestPurchase.getUuid())
                .episodeId(requestPurchase.getEpisodeId())
            .build());

        Message message = new Message();
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    public String generatePartnerOrderId() {
        int targetStringLength = 12;
        Random random = new Random();
        return random.ints('0', 'z' + 1)
            .filter(i -> (i <= '0' || i >= 'A') && (i <= 'Z' || i >= 'a'))
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }
}
