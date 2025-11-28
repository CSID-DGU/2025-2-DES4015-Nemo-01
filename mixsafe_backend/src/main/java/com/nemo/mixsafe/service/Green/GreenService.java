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
        MstrNoDTO mstrNoDTO = fetchProductList(requestDto);
        fetchProductIngredients(mstrNoDTO);
    }

    // 3번: 생활화학제품 목록 조회
    private MstrNoDTO fetchProductList(MixRequestDto requestDto) {

        Long id1 = requestDto.getProduct1Id();
        Product product1 = productRepository.findById(id1)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id1));
        Long id2 = requestDto.getProduct2Id();
        Product product2 = productRepository.findById(id2)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id2));

        try {
            String url1 = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .queryParam("ServiceName", "chmstryProductList")
                    .queryParam("AuthKey", authKey)
                    .queryParam("prdtarmCd", product1.getPrdtarmCd()) // 01: 위해우려제품
                    .queryParam("prdtnmKor", product1.getProductName())
                    .build()
                    .toUriString();

            String url2 = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .queryParam("ServiceName", "chmstryProductList")
                    .queryParam("AuthKey", authKey)
                    .queryParam("prdtarmCd", product2.getPrdtarmCd()) // 01: 위해우려제품
                    .queryParam("prdtnmKor", product2.getProductName())
                    .build()
                    .toUriString();

            String xmlResponse1 = restTemplate.getForObject(url1, String.class);
            String xmlResponse2 = restTemplate.getForObject(url2, String.class);
            Green3ResponseDto response1 = xmlMapper.readValue(xmlResponse1, Green3ResponseDto.class);
            Green3ResponseDto response2 = xmlMapper.readValue(xmlResponse2, Green3ResponseDto.class);


            GreenApiErrorCode errorCode1 = GreenApiErrorCode.fromCode(response1.getResultcode());
            GreenApiErrorCode errorCode2 = GreenApiErrorCode.fromCode(response2.getResultcode());

            if (!errorCode1.isSuccess()) {
                throw new GreenApiException(errorCode1);
            }
            if (!errorCode2.isSuccess()) {
                throw new GreenApiException(errorCode2);
            }

            String mstrNo1 = response1.getRows().get(0).getPrdtMstrNo();
            String mstrNo2 = response1.getRows().get(0).getPrdtMstrNo();

            product1.setPrdtMstrNo(mstrNo1);
            product2.setPrdtMstrNo(mstrNo2);
            productRepository.save(product1);
            productRepository.save(product2);


            return MstrNoDTO.builder()
                    .productId1(id1)
                    .productId2(id2)
                    .prdtMstrNo1(mstrNo1)
                    .prdtMstrNo2(mstrNo2)
                    .build();

        } catch (GreenApiException e) {
            throw e;
        } catch (Exception e) {
            log.error("API 3번 호출 실패", e);
            throw new GreenApiException(GreenApiErrorCode.ERROR99999);
        }
    }

    // 5번: 제품 성분 목록 조회
    private void fetchProductIngredients(MstrNoDTO mstrNoDTO) {
        Long id1 = mstrNoDTO.getProductId1();
        Long id2 = mstrNoDTO.getProductId2();
        String prdtMstrNo1 = mstrNoDTO.getPrdtMstrNo1();
        String prdtMstrNo2 = mstrNoDTO.getPrdtMstrNo2();

        try {
            Product product1 = productRepository.findById(id1)
                    .orElseThrow(() -> new RuntimeException("Product1을 찾을 수 없습니다: " + id1));
            Product product2 = productRepository.findById(id2)
                    .orElseThrow(() -> new RuntimeException("Product2를 찾을 수 없습니다: " + id2));

            String url1 = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .queryParam("ServiceName", "chmstryProductCntnrIndtList")
                    .queryParam("AuthKey", authKey)
                    .queryParam("prdtMstrNo", prdtMstrNo1)
                    .build()
                    .toUriString();

            String url2 = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .queryParam("ServiceName", "chmstryProductCntnrIndtList")
                    .queryParam("AuthKey", authKey)
                    .queryParam("prdtMstrNo", prdtMstrNo2)
                    .build()
                    .toUriString();

            String xmlResponse1 = restTemplate.getForObject(url1, String.class);
            Green5ResponseDto response1 = xmlMapper.readValue(xmlResponse1, Green5ResponseDto.class);
            String xmlResponse2 = restTemplate.getForObject(url2, String.class);
            Green5ResponseDto response2 = xmlMapper.readValue(xmlResponse2, Green5ResponseDto.class);

            GreenApiErrorCode errorCode1 = GreenApiErrorCode.fromCode(response1.getResultcode());
            GreenApiErrorCode errorCode2 = GreenApiErrorCode.fromCode(response2.getResultcode());

            if (!errorCode1.isSuccess()) {
                throw new GreenApiException(errorCode1);
            }
            if (!errorCode2.isSuccess()) {
                throw new GreenApiException(errorCode2);
            }

            List<Ingredient> ingredients1 = new ArrayList<>();
            if (response1.getRows() != null) {
                for (Green5Row row : response1.getRows()) {
                    String casNo = row.getCasNo();

                    if (casNo != null && !casNo.trim().isEmpty()) {
                        // 중복 체크
                        if (!ingredientRepository.existsByProductAndCasNo(product1, casNo)) {
                            Ingredient ingredient = new Ingredient();
                            ingredient.setCasNo(casNo);
                            ingredient.setProduct(product1);

                            Ingredient saved = ingredientRepository.save(ingredient);
                            ingredients1.add(saved);
                        }
                    }
                }
            }

            // Product2의 성분 저장
            List<Ingredient> ingredients2 = new ArrayList<>();
            if (response2.getRows() != null) {
                for (Green5Row row : response2.getRows()) {
                    String casNo = row.getCasNo();

                    if (casNo != null && !casNo.trim().isEmpty()) {
                        // 중복 체크
                        if (!ingredientRepository.existsByProductAndCasNo(product2, casNo)) {
                            Ingredient ingredient = new Ingredient();
                            ingredient.setCasNo(casNo);
                            ingredient.setProduct(product2);

                            Ingredient saved = ingredientRepository.save(ingredient);
                            ingredients2.add(saved);
                        }
                    }
                }
            }

        } catch (GreenApiException e) {
            throw e;
        } catch (Exception e) {
            log.error("API 5번 호출 실패", e);
            throw new GreenApiException(GreenApiErrorCode.ERROR99999);
        }
    }
}