package com.uliga.uliga_backend.domain.Member.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.Member.dto.OAuthDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static com.uliga.uliga_backend.domain.Member.dto.OAuthDTO.*;

@Component
@RequiredArgsConstructor
public class GoogleAuth {


    @Value("${spring.OAuth2.google.userinfo_request}")
    private String GOOGLE_USERINFO_REQUEST_URL;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    public ResponseEntity<String> requestUserInfo(String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + token);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(GOOGLE_USERINFO_REQUEST_URL, HttpMethod.GET, request, String.class);
    }

    public GoogleUser getUserInfo(ResponseEntity<String> userInfo) throws JsonProcessingException {
        return objectMapper.readValue(userInfo.getBody(), GoogleUser.class);
    }

}
