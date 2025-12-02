package com.nemo.mixsafe.service.Green;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.nemo.mixsafe.domain.Ingredient;
import com.nemo.mixsafe.domain.Product;
import com.nemo.mixsafe.dto.Client.MixRequestDto;
import com.nemo.mixsafe.dto.Green.*;
import com.nemo.mixsafe.exception.GreenApiErrorCode;
import com.nemo.mixsafe.exception.GreenApiException;
import com.nemo.mixsafe.repository.IngredientRepository;
import com.nemo.mixsafe.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GreenService {

    private final RestTemplate restTemplate;
    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository;
    private final XmlMapper xmlMapper = new XmlMapper();

    @Value("${green.api.base-url}")
    private String baseUrl;

    @Value("${green.api.auth-key}")
    private String authKey;

    @Transactional
    public void getGreenResult(MixRequestDto requestDto) {

        Long id1 = requestDto.getProduct1Id();
        Long id2 = requestDto.getProduct2Id();

        // 제품 조회
        Product product1 = productRepository.findById(id1)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id1));
        Product product2 = productRepository.findById(id2)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id2));

        // 각각 처리
        fetchAndSaveProductIngredients(product1);
        fetchAndSaveProductIngredients(product2);
    }

    @Transactional
    public void fetchAndSaveProductIngredients(Product product) {
        try {
            log.info("제품 '{}' 초록누리 API 조회 시작", product.getProductName());

            // 1. 제품 마스터 번호가 없으면 먼저 조회
            if (product.getPrdMstrNo() == null || product.getPrdMstrNo().isEmpty()) {
                String prdMstrNo = fetchProductMasterNumber(product);
                product.setPrdMstrNo(prdMstrNo);
                productRepository.save(product);
                log.info("제품 마스터 번호 저장 완료: {}", prdMstrNo);
            }

            // 2. 성분 정보 조회 및 저장
            fetchProductIngredients(product);

            log.info("제품 '{}' 성분 저장 완료", product.getProductName());

        } catch (GreenApiException e) {
            log.error("초록누리 API 오류: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("제품 성분 조회 중 오류 발생", e);
            throw new RuntimeException("제품 성분 조회 실패: " + e.getMessage(), e);
        }
    }

    // 3번: 생활화학제품 목록 조회
    private String fetchProductMasterNumber(Product product) {

        try{
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .queryParam("ServiceName", "chmstryProductList")
                    .queryParam("AuthKey", authKey)
                    .queryParam("prdtarmCd", "01") // 01: 위해우려제품
                    .queryParam("prdtnmKor", product.getProductName())
                    .build()
                    .toUriString();
            log.info("[GREEN-3] 제품 마스터 번호 조회 요청 시작. productName={}, prdtarmCd={}",
                    product.getProductName(), product.getPrdtarmCd());
            log.debug("[GREEN-3] 요청 URL = {}", url);

            String xmlResponse = restTemplate.getForObject(url, String.class);


            log.debug("[GREEN-3] 원본 XML 응답 일부 = {}",
                    xmlResponse != null && xmlResponse.length() > 500
                            ? xmlResponse.substring(0, 500) + "..."
                            : xmlResponse);


            Green3ResponseDto response = xmlMapper.readValue(xmlResponse, Green3ResponseDto.class);

            log.debug("[GREEN-3] 파싱 결과 resultcode={}, rowCount={}",
                    response.getResultcode(),
                    response.getRows() != null ? response.getRows().size() : 0);

            GreenApiErrorCode errorCode = GreenApiErrorCode.fromCode(response.getResultcode());
            if (!errorCode.isSuccess()) {
                log.error("[GREEN-3] API resultcode 오류. code={} message={}",
                        errorCode.getCode(), errorCode.getMessage());
                throw new GreenApiException(errorCode);
            }

            if (response.getRows() == null || response.getRows().isEmpty()) {
                log.warn("[GREEN-3] 제품 정보를 찾을 수 없음. productName={}", product.getProductName());
                throw new RuntimeException("제품 정보를 찾을 수 없습니다: " + product.getProductName());
            }

            return response.getRows().get(0).getPrdtMstrNo();


        } catch (GreenApiException e) {
            throw e;
        } catch (Exception e) {
            log.error("[GREEN-3] 제품 마스터 번호 조회 실패 - 상세 에러: {}", e.getMessage(), e);
            throw new GreenApiException(GreenApiErrorCode.ERROR99999);
        }


    }

    // 5번: 제품 성분 목록 조회
    private void fetchProductIngredients(Product product) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .queryParam("ServiceName", "chmstryProductCntnIrdntList")
                    .queryParam("AuthKey", authKey)
                    .queryParam("prdtMstrNo", product.getPrdMstrNo())
                    .build()
                    .toUriString();


            String xmlResponse = restTemplate.getForObject(url, String.class);
            Green5ResponseDto response = xmlMapper.readValue(xmlResponse, Green5ResponseDto.class);

            GreenApiErrorCode errorCode = GreenApiErrorCode.fromCode(response.getResultcode());
            if (!errorCode.isSuccess()) {
                throw new GreenApiException(errorCode);
            }


            List<Ingredient> ingredients1 = new ArrayList<>();
            if (response.getRows() != null) {
                for (Green5Row row : response.getRows()) {
                    String casNo = row.getCasNo();

                    if (casNo != null && !casNo.trim().isEmpty()) {
                        // 중복 체크
                        if (!ingredientRepository.existsByProductAndCasNo(product, casNo)) {
                            Ingredient ingredient = new Ingredient();
                            ingredient.setCasNo(casNo);
                            ingredient.setProduct(product);

                            Ingredient saved = ingredientRepository.save(ingredient);
                            ingredients1.add(saved);
                            log.debug("성분 저장: CAS={}", casNo);
                        }
                    }
                }
            }

        } catch (GreenApiException e) {
            throw e;
        } catch (Exception e) {
            log.error("제품 성분 조회 실패(5번)", e);
            throw new GreenApiException(GreenApiErrorCode.ERROR99999);
        }
    }
}