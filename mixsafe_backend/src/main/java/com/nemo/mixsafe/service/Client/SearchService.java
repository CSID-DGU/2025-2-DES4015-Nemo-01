package com.nemo.mixsafe.service.Client;

import com.nemo.mixsafe.domain.Product;
import com.nemo.mixsafe.dto.Client.OcrImageRequestDto;
import com.nemo.mixsafe.dto.Client.SearchRequestDto;
import com.nemo.mixsafe.dto.Client.SearchResponseDto;
import com.nemo.mixsafe.repository.Client.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;
    private final ClovaOcrService clovaOcrService;

    /** 일반 이름 검색 */
    public SearchResponseDto searchProductByName(SearchRequestDto requestDto) {
        Product product = searchRepository.findFirstByProductName(requestDto.getProductName())
                .orElseThrow(() -> new IllegalArgumentException("해당 제품이 존재하지 않습니다."));

        return SearchResponseDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .build();
    }

    /** OCR 이미지 검색 */
    public SearchResponseDto searchProductByOcrImage(OcrImageRequestDto dto) throws IOException {

        String ocrText = clovaOcrService.extractTextFromImage(dto.getBase64Image());
        System.out.println("OCR TEXT: " + ocrText);

        String keyword = extractKeyword(ocrText);
        System.out.println("최종 검색 키워드: " + keyword);

        if (keyword.isBlank()) {
            throw new IllegalArgumentException("OCR 결과에서 검색 가능한 키워드를 찾지 못했습니다.");
        }

        List<Product> results = searchRepository.findByProductNameContaining(keyword);

        if (results.isEmpty()) {
            throw new IllegalArgumentException("OCR 인식 결과에 해당하는 제품이 없습니다.");
        }

        Product product = results.get(0);

        return SearchResponseDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .build();
    }

    /**
     * OCR 텍스트에서 최적의 제품명 후보를 얻는다 (한글 제품명 우선)
     */
    private String extractKeyword(String text) {

        if (text == null || text.isBlank()) return "";

        // 1) 한글만 추출
        StringBuilder onlyKorean = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (c >= '가' && c <= '힣') {
                onlyKorean.append(c);
            } else {
                onlyKorean.append(" ");
            }
        }

        String[] words = onlyKorean.toString().trim().split("\\s+");

        // 2) 제품명 우선 키워드 목록
        String[] productKeywords = {
                "구연산","베이킹소다","식초","세제","세탁세제","세정제",
                "클리너","탈취제","유연제","천연세제"
        };

        // 2-1) OCR 결과에서 먼저 등장한 제품명 키워드 있으면 즉시 반환
        for (String w : words) {
            for (String pk : productKeywords) {
                if (w.contains(pk) || pk.contains(w)) {
                    return pk;
                }
            }
        }

        // 3) 없으면 가장 긴 단어 반환
        String best = "";
        for (String w : words) {
            if (w.length() > best.length()) {
                best = w;
            }
        }

        return best;
    }
}
