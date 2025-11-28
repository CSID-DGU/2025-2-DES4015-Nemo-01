package com.nemo.mixsafe.service.Client;

import com.nemo.mixsafe.domain.Ingredient;
import com.nemo.mixsafe.domain.Product;
import com.nemo.mixsafe.dto.Ai.AiMultiRequestDto;
import com.nemo.mixsafe.dto.Ai.AiRequestDto;
import com.nemo.mixsafe.dto.Ai.AiResponseDto;
import com.nemo.mixsafe.dto.Client.AiResultResponseDto;
import com.nemo.mixsafe.dto.Client.MixRequestDto;
import com.nemo.mixsafe.dto.Client.SearchRequestDto;
import com.nemo.mixsafe.dto.Client.SearchResponseDto;
import com.nemo.mixsafe.repository.Client.SearchRepository;
import com.nemo.mixsafe.repository.IngredientRepository;
import com.nemo.mixsafe.repository.ProductRepository;
import com.nemo.mixsafe.service.AiService;
import com.nemo.mixsafe.service.Green.GreenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;
    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository;
    private final GreenService greenService;
    private final AiService aiService;

    public SearchResponseDto searchProductByName(SearchRequestDto requestDto) {
        Product product = searchRepository.findFirstByProductName(requestDto.getProductName())
                .orElseThrow(() -> new IllegalArgumentException("해당 제품이 존재하지 않습니다."));

        return SearchResponseDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .build();
    }

    public AiResultResponseDto analyzeMix(MixRequestDto requestDto) {
        try {
            Long productId1 = requestDto.getProduct1Id();
            Long productId2 = requestDto.getProduct2Id();

            Product product1 = productRepository.findById(productId1)
                    .orElseThrow(() -> new RuntimeException("제품을 찾을 수 없습니다: " + productId1));
            Product product2 = productRepository.findById(productId2)
                    .orElseThrow(() -> new RuntimeException("제품을 찾을 수 없습니다: " + productId2));

            List<Ingredient> ingredients1 = ingredientRepository.findByProduct(product1);
            List<Ingredient> ingredients2 = ingredientRepository.findByProduct(product2);

            // 3. 성분이 없으면 초록누리 API로 조회 및 저장
            if (ingredients1.isEmpty() || ingredients2.isEmpty()) {
                greenService.getGreenResult(requestDto);

                ingredients1 = ingredientRepository.findByProduct(product1);
                ingredients2 = ingredientRepository.findByProduct(product2);
            }

            List<String> product1CasNumbers = ingredients1.stream()
                    .map(Ingredient::getCasNo)
                    .toList();


            List<String> product2CasNumbers = ingredients2.stream()
                    .map(Ingredient::getCasNo)
                    .toList();

            if (product1CasNumbers.isEmpty() || product2CasNumbers.isEmpty()) {
                return AiResultResponseDto.builder()
                        .productName(product1.getProductName() + " + " + product2.getProductName())
                        .aiResult("둘 중 하나의 성분 정보를 찾을 수 없습니다.")
                        .status("FAILED")
                        .build();
            }

            AiRequestDto aiReq1 = AiRequestDto.builder()
                    .casNumbers(product1CasNumbers)
                    .productName(product1.getProductName())
                    .build();

            AiRequestDto aiReq2 = AiRequestDto.builder()
                    .casNumbers(product2CasNumbers)
                    .productName(product2.getProductName())
                    .build();

            List<AiRequestDto> productList = new ArrayList<>();
            productList.add(aiReq1);
            productList.add(aiReq2);

            AiMultiRequestDto aiMultiReq = new AiMultiRequestDto();
            aiMultiReq.setProducts(productList);
            aiMultiReq.setUseAi(true);


            AiResponseDto aiResponse = aiService.analyze(aiMultiReq);


            return AiResultResponseDto.builder()
                    .productName(product1.getProductName() + " + " + product2.getProductName())
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

}
