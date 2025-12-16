package com.nemo.mixsafe.controller.Client;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.nemo.mixsafe.dto.Green.Green3ResponseDto;
import com.nemo.mixsafe.dto.Green.Green5ResponseDto;
import com.nemo.mixsafe.exception.GreenApiErrorCode;
import com.nemo.mixsafe.exception.GreenApiException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/debug/green")
@RequiredArgsConstructor
@Slf4j
public class DebugController {

    private final RestTemplate restTemplate;
    private final XmlMapper xmlMapper = new XmlMapper();

    @Value("${green.api.base-url}")
    private String baseUrl;

    @Value("${green.api.auth-key}")
    private String authKey;

    /**
     * 초록누리 5번 API (제품 성분 목록) – 상위 5개만 조회
     *
     * Postman 테스트용
     */

    @GetMapping(value = "/products/top5", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTop5Products(
            @RequestParam String prdtnmKor,
            @RequestParam(defaultValue = "01") String prdtarmCd
    ) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .queryParam("ServiceName", "chmstryProductList")
                    .queryParam("AuthKey", authKey)
                    .queryParam("prdtarmCd", prdtarmCd)
                    .queryParam("prdtnmKor", prdtnmKor)
                    // ⭐ 핵심: 5개까지만
                    .queryParam("PageNum", 1)
                    .queryParam("PageCount", 5)
                    .build(true)
                    .toUriString();

            log.info("[GREEN-3-DEBUG] 요청 URL = {}", url);

            String xmlResponse = restTemplate.getForObject(url, String.class);

            log.debug("[GREEN-3-DEBUG] 원본 XML (500자) = {}",
                    xmlResponse != null && xmlResponse.length() > 500
                            ? xmlResponse.substring(0, 500) + "..."
                            : xmlResponse);

            Green3ResponseDto response = xmlMapper.readValue(xmlResponse, Green3ResponseDto.class);

            GreenApiErrorCode errorCode = GreenApiErrorCode.fromCode(response.getResultcode());
            if (!errorCode.isSuccess()) {
                throw new GreenApiException(errorCode);
            }

            // 👉 그대로 반환 (rows 최대 5개)
            return ResponseEntity.ok(response);

        } catch (GreenApiException e) {
            log.error("[GREEN-3-DEBUG] 초록누리 API 오류", e);
            throw e;
        } catch (Exception e) {
            log.error("[GREEN-3-DEBUG] 처리 중 오류", e);
            throw new GreenApiException(GreenApiErrorCode.ERROR99999);
        }
    }

    /**
     * (옵션) 3번 응답 중 "첫 번째" prdtMstrNo만 바로 뽑아주는 엔드포인트
     */
    @GetMapping(value = "/products/first-mstrno", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getFirstPrdtMstrNo(
            @RequestParam String prdtnmKor,
            @RequestParam(defaultValue = "01") String prdtarmCd
    ) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .queryParam("ServiceName", "chmstryProductList")
                    .queryParam("AuthKey", authKey)
                    .queryParam("prdtarmCd", prdtarmCd)
                    .queryParam("prdtnmKor", prdtnmKor)
                    .queryParam("PageNum", 1)
                    .queryParam("PageCount", 5)
                    .build(true)
                    .toUriString();

            String xmlResponse = restTemplate.getForObject(url, String.class);
            Green3ResponseDto response = xmlMapper.readValue(xmlResponse, Green3ResponseDto.class);

            GreenApiErrorCode errorCode = GreenApiErrorCode.fromCode(response.getResultcode());
            if (!errorCode.isSuccess()) throw new GreenApiException(errorCode);

            if (response.getRows() == null || response.getRows().isEmpty()) {
                return ResponseEntity.ok("");
            }

            String prdtMstrNo = response.getRows().get(0).getPrdtMstrNo();
            return ResponseEntity.ok(prdtMstrNo);

        } catch (GreenApiException e) {
            throw e;
        } catch (Exception e) {
            throw new GreenApiException(GreenApiErrorCode.ERROR99999);
        }
    }


    @GetMapping(value = "/ingredients/top5", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTop5Ingredients(
            @RequestParam String prdtMstrNo
    ) {

        try {
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .queryParam("ServiceName", "chmstryProductCntnIrdntList")
                    .queryParam("AuthKey", authKey)
                    .queryParam("prdtMstrNo", prdtMstrNo)
                    .queryParam("pagenum", 1)
                    .queryParam("pagesize", 5)   // ⭐ 핵심: 5개만
                    .build(true)
                    .toUriString();

            log.info("[GREEN-5-DEBUG] 요청 URL = {}", url);

            String xmlResponse = restTemplate.getForObject(url, String.class);

            log.debug("[GREEN-5-DEBUG] 원본 XML (500자) = {}",
                    xmlResponse != null && xmlResponse.length() > 500
                            ? xmlResponse.substring(0, 500) + "..."
                            : xmlResponse);


            Green5ResponseDto response =
                    xmlMapper.readValue(xmlResponse, Green5ResponseDto.class);

            GreenApiErrorCode errorCode =
                    GreenApiErrorCode.fromCode(response.getResultcode());

            if (!errorCode.isSuccess()) {
                throw new GreenApiException(errorCode);
            }

            // 👉 그대로 반환 (rows 최대 5개)
            return ResponseEntity.ok(response);

        } catch (GreenApiException e) {
            log.error("[GREEN-5-DEBUG] 초록누리 API 오류", e);
            throw e;
        } catch (Exception e) {
            log.error("[GREEN-5-DEBUG] 처리 중 오류", e);
            throw new GreenApiException(GreenApiErrorCode.ERROR99999);
        }
    }
}
