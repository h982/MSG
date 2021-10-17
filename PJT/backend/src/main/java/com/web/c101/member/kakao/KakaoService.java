package com.web.c101.member.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.c101.error.CustomException;
import com.web.c101.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Slf4j
public class KakaoService {
    private @Value("${kakao.key}")
    String kakaoKey;

    public String kakaoLogin(String authorizeCode) {
        // http 요청을 간단하게 처리해주는 클래스
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoDto kakaoDto = null;

        // HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        // REST api 키 주의!!
        params.add("client_id", kakaoKey);
        params.add("redirect_url", "http://localhost:8080/member");
        params.add("code", authorizeCode);

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담는다.
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        String url = "https://kauth.kakao.com/oauth/token";
        try {
            // 실제로 요청하기
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            // 요청하여 반환된 값을 해당 오브젝트에 맞게 key, value값을 매칭
            kakaoDto = objectMapper.readValue(response.getBody(), KakaoDto.class);
        } catch (RestClientException | JsonProcessingException | NullPointerException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.KAKAO_LOGIN_EXCEPTION);
        }

        log.info(">>> Kakao Token : " + kakaoDto);
        return getKakaoUidByAccessToken(kakaoDto.getAccess_token());
    }

    // AccessToken을 사용하여 유저정보 받기
    private String getKakaoUidByAccessToken(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = null;

        // HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        String url = "https://kapi.kakao.com/v2/user/me";
        Optional<JSONObject> parser = Optional.empty();
        try {
            // 실제 요청
            response = restTemplate.postForEntity(url, request, String.class);
            parser = Optional.ofNullable(new JSONObject(response.getBody()));
        } catch (RestClientException e) {
            e.printStackTrace();
        }

        JSONObject jsonParser = parser.orElseThrow(() -> new CustomException(ErrorCode.KAKAO_LOGIN_EXCEPTION));
        log.info("KAKAO user info parser : " + parser);

        String uid = String.valueOf(jsonParser.getBigInteger("id"));

        log.info(">>> kakao uid : " + uid);

        return uid;
    }
}
