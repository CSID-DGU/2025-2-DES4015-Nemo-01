package com.nemo.mixsafe.service;

import com.nemo.mixsafe.dto.Ai.AiMultiRequestDto;
import com.nemo.mixsafe.dto.Ai.AiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiService {

   /* - HTTP 400: CAS Number가 2개 미만
    - HTTP 404: CAMEO에서 화학물질을 찾을 수 없음
    - HTTP 500: 서버 오류
    - Timeout: 5분 초과
    */


    private final RestTemplate restTemplate;

    @Value("${ai.api.url:https://chemical-analyzer-v2.onrender.com}")
    private String aiApiUrl;

    public AiResponseDto analyze(AiMultiRequestDto requestDto) {
        try{

            AiMultiRequestDto request = requestDto;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<AiMultiRequestDto> entity = new HttpEntity<>(request, headers);

            ResponseEntity<AiResponseDto> response = restTemplate.postForEntity(aiApiUrl, entity, AiResponseDto.class);

            AiResponseDto aiResponse = response.getBody();

            if (aiResponse == null || !aiResponse.getSuccess()) {
                throw new RuntimeException("AI 분석에 실패했습니다.");
            }

            return aiResponse;

        }
        catch (Exception e) {
            log.error("AI API 호출 중 오류 발생", e);
            throw new RuntimeException("AI 분석 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

}
