package com.readme.payments.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.readme.payments.requestObject.RequestPurchase;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentsServiceImpl implements PaymentsService {

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

    @Override
    public String chargePoint(RequestPurchase requestPurchase) throws JsonProcessingException {

        Map<String, String> ready = ready(requestPurchase);

        return null;
    }

    public Map<String, String> ready(RequestPurchase requestPurchase)
        throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + APP_ADMIN_KEY);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("cid", CID);
        body.add("partner_order_id", "1");
        body.add("partner_user_id", requestPurchase.getUuid());

        Long novelId = requestPurchase.getNovelId();
        body.add("item_name", novelId == null ? "point" : novelId.toString());

        body.add("quantity", "1");
        body.add("total_amount", requestPurchase.getPoint().toString());
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
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        Map<String, String> result = new HashMap<>();
        result.put("tid", jsonNode.get("tid").asText());
        result.put("next_redirect_app_url", jsonNode.get("next_redirect_app_url").asText());
        result.put("next_redirect_mobile_url", jsonNode.get("next_redirect_mobile_url").asText());
        result.put("next_redirect_pc_url", jsonNode.get("next_redirect_pc_url").asText());
        result.put("android_app_scheme", jsonNode.get("android_app_scheme").asText());
        result.put("ios_app_scheme", jsonNode.get("ios_app_scheme").asText());

        return result;
    }
}
