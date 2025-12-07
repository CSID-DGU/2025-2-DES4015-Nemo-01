package com.nemo.mixsafe.service.Client;

import com.nemo.mixsafe.domain.DefaultSubstance;
import com.nemo.mixsafe.domain.Ingredient;
import com.nemo.mixsafe.domain.Product;
import com.nemo.mixsafe.dto.Client.OcrImageRequestDto;
import com.nemo.mixsafe.dto.Ai.AiMultiRequestDto;
import com.nemo.mixsafe.dto.Ai.AiRequestDto;
import com.nemo.mixsafe.dto.Ai.AiResponseDto;
import com.nemo.mixsafe.dto.Client.AiResultResponseDto;
import com.nemo.mixsafe.dto.Client.MixRequestDto;
import com.nemo.mixsafe.dto.Client.SearchRequestDto;
import com.nemo.mixsafe.dto.Client.SearchResponseDto;
import com.nemo.mixsafe.repository.Client.SearchRepository;
import com.nemo.mixsafe.repository.Client.SubstanceRepository;
import com.nemo.mixsafe.repository.IngredientRepository;
import com.nemo.mixsafe.repository.ProductRepository;
import com.nemo.mixsafe.service.AiService;
import com.nemo.mixsafe.service.Green.GreenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

    private final SearchRepository searchRepository;
    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository;
    private final GreenService greenService;
    private final AiService aiService;
    private final ClovaOcrService clovaOcrService;
    private final SubstanceRepository substanceRepository;




    private static class IngredientInfo {
        String name;
        List<String> casNumbers;

        IngredientInfo(String name, List<String> casNumbers) {
            this.name = name;
            this.casNumbers = casNumbers;
        }
    }

    public AiResultResponseDto analyzeMix(MixRequestDto requestDto) {
        try {
            IngredientInfo info1 = getIngredientInfo(
                    requestDto.getProduct1Id(),
                    requestDto.getSource1()
            );

            // 2. 제품2 정보 가져오기
            IngredientInfo info2 = getIngredientInfo(
                    requestDto.getProduct2Id(),
                    requestDto.getSource2()
            );

            // 3. 성분이 없으면 에러 반환
            if (info1.casNumbers.isEmpty() || info2.casNumbers.isEmpty()) {
                return AiResultResponseDto.builder()
                        .productName(info1.name + " + " + info2.name)
                        .aiResult("둘 중 하나의 성분 정보를 찾을 수 없습니다.")
                        .status("FAILED")
                        .build();
            }


            AiRequestDto aiReq1 = AiRequestDto.builder()
                    .casNumbers(info1.casNumbers)
                    .productName(info1.name)
                    .build();

            AiRequestDto aiReq2 = AiRequestDto.builder()
                    .casNumbers(info2.casNumbers)
                    .productName(info2.name)
                    .build();

            List<AiRequestDto> productList = new ArrayList<>();
            productList.add(aiReq1);
            productList.add(aiReq2);

            AiMultiRequestDto aiMultiReq = new AiMultiRequestDto();
            aiMultiReq.setProducts(productList);
            aiMultiReq.setUseAi(true);

            log.info("AI 분석 시작: {} + {}", info1.name, info2.name);
            AiResponseDto aiResponse = aiService.analyze(aiMultiReq);


            return AiResultResponseDto.builder()
                    .productName(info1.name + " + " + info2.name)
                    .aiResult(aiResponse.getSimpleResponse().getMessage())
                    .status("SUCCESS")
                    .build();

        } catch (Exception e) {
            return AiResultResponseDto.builder()
                    .productName("분석 실패")
                    .aiResult("분석 중 오류가 발생했습니다: " + e.getMessage())
                    .status("FAILED")
                    .build();
        }
    }


    private IngredientInfo getIngredientInfo(Long id, String source) {
        if (Objects.equals(source, "default")) {
            // 기본 물질 - DB에서 바로 조회
            return getDefaultSubstanceInfo(id);
        } else if (Objects.equals(source, "prd")) {
            // 제품 - DB 확인 후 없으면 초록누리 API 호출
            return getProductInfo(id);
        } else {
            throw new IllegalArgumentException("잘못된 source 타입입니다: " + source);
        }
    }

    private IngredientInfo getDefaultSubstanceInfo(Long substanceId) {
        log.info("기본 물질 조회 시작: substanceId={}", substanceId);

        DefaultSubstance substance = substanceRepository.findById(substanceId)
                .orElseThrow(() -> new RuntimeException("기본 물질을 찾을 수 없습니다: " + substanceId));

        List<Ingredient> ingredients = ingredientRepository.findBySubstance(substance);

        List<String> casNumbers = ingredients.stream()
                .map(Ingredient::getCasNo)
                .toList();

        log.info("기본 물질 '{}' 성분 {}개 조회 완료", substance.getSubstanceName(), casNumbers.size());

        return new IngredientInfo(substance.getSubstanceName(), casNumbers);
    }

    private IngredientInfo getProductInfo(Long productId) {
        log.info("제품 조회 시작: productId={}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("제품을 찾을 수 없습니다: " + productId));

        List<Ingredient> ingredients = ingredientRepository.findByProduct(product);

        // 성분이 없으면 초록누리 API로 가져오기
        if (ingredients.isEmpty()) {
            log.info("제품 '{}' 성분 정보 없음 - 초록누리 API 호출", product.getProductName());

            // 초록누리 API 호출을 위한 임시 MixRequestDto 생성
            MixRequestDto tempRequest = MixRequestDto.builder()
                    .product1Id(productId)
                    .product2Id(productId)
                    .source1("prd")
                    .source2("prd")
                    .build();

            greenService.getGreenResult(tempRequest);

            // 다시 조회
            ingredients = ingredientRepository.findByProduct(product);
            log.info("초록누리 API 호출 후 성분 {}개 조회", ingredients.size());
        }

        List<String> casNumbers = ingredients.stream()
                .map(Ingredient::getCasNo)
                .toList();

        log.info("제품 '{}' 성분 {}개 조회 완료", product.getProductName(), casNumbers.size());

        return new IngredientInfo(product.getProductName(), casNumbers);
    }

    /** OCR 이미지 검색 */
    public SearchResponseDto searchProductByOcrImage(OcrImageRequestDto dto) throws IOException {

        // 1) OCR 수행
        String ocrText = clovaOcrService.extractTextFromImage(dto.getBase64Image());
        log.info("OCR TEXT: {}", ocrText);

        // 2) 전체 제품 중 가장 유사한 제품 찾기
        Product best = findBestMatchProduct(ocrText);

        if (best == null) {
            throw new IllegalArgumentException("OCR 결과와 일치하는 제품을 찾지 못했습니다.");
        }

        log.info("최종 선택된 제품: {} (id={})", best.getProductName(), best.getProductId());

        return SearchResponseDto.builder()
                .productId(best.getProductId())
                .productName(best.getProductName())
                .build();
    }

    private String preprocess(String text) {
        if (text == null) return "";
        return text.replaceAll("[^가-힣0-9 ]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }


    private double similarity(String a, String b) {

        if (a == null || b == null) return 0.0;

        a = a.replace(" ", "");
        b = b.replace(" ", "");

        int max = 0;

        for (int i = 0; i < a.length(); i++) {
            for (int j = i + 1; j <= a.length(); j++) {
                String sub = a.substring(i, j);
                if (b.contains(sub)) {
                    max = Math.max(max, sub.length());
                }
            }
        }

        return (double) max / Math.max(a.length(), b.length());
    }


    private Product findBestMatchProduct(String ocrText) {

        String clean = preprocess(ocrText);

        List<Product> allProducts = productRepository.findAll();

        Product best = null;
        double bestScore = 0.0;

        for (Product p : allProducts) {
            double score = similarity(clean, preprocess(p.getProductName()));

            log.info("유사도 비교: OCR='{}', 제품명='{}', score={}",
                    clean, p.getProductName(), score);

            if (score > bestScore) {
                bestScore = score;
                best = p;
            }
        }

        return best;
    }

//
//    /**
//     * OCR 텍스트에서 최적의 제품명 후보를 얻는다 (한글 제품명 우선)
//     */
//    private String extractKeyword(String text) {
//
//        if (text == null || text.isBlank()) return "";
//
//        // 1) 한글만 추출
//        StringBuilder onlyKorean = new StringBuilder();
//        for (char c : text.toCharArray()) {
//            if (c >= '가' && c <= '힣') {
//                onlyKorean.append(c);
//            } else {
//                onlyKorean.append(" ");
//            }
//        }
//
//        String[] words = onlyKorean.toString().trim().split("\\s+");
//
//        // 2) 제품명 우선 키워드 목록
//        String[] productKeywords = {
//                "구연산","베이킹소다","식초","세제","세탁세제","세정제",
//                "클리너","탈취제","유연제","천연세제"
//        };
//
//        // 2-1) OCR 결과에서 먼저 등장한 제품명 키워드 있으면 즉시 반환
//        for (String w : words) {
//            for (String pk : productKeywords) {
//                if (w.contains(pk) || pk.contains(w)) {
//                    return pk;
//                }
//            }
//        }
//
//        // 3) 없으면 가장 긴 단어 반환
//        String best = "";
//        for (String w : words) {
//            if (w.length() > best.length()) {
//                best = w;
//            }
//        }
//
//        return best;
//    }
}
