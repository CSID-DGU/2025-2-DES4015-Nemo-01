package com.nemo.mixsafe.service.Client;

import com.nemo.mixsafe.domain.DefaultSubstance;
import com.nemo.mixsafe.domain.Product;
import com.nemo.mixsafe.dto.Client.SearchRequestDto;
import com.nemo.mixsafe.dto.Client.SearchResponseDto;
import com.nemo.mixsafe.repository.Client.SearchRepository;
import com.nemo.mixsafe.repository.Client.SubstanceRepository;
import com.nemo.mixsafe.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final SearchRepository searchRepository;
    private final SubstanceRepository substanceRepository;

    //제품명을 검색
    public SearchResponseDto searchProductByName(String productName) {
        Product product = searchRepository.findFirstByProductName(productName)
                .orElseThrow(() -> new IllegalArgumentException("해당 제품이 존재하지 않습니다."));

        return SearchResponseDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .build();
    }

    //미리 저장해둔 물질을 선택
    public SearchResponseDto searchDefaultSubstance(String substanceName){
        DefaultSubstance defaultSubstance = substanceRepository.findBySubstanceName(substanceName)
                .orElseThrow(()-> new IllegalArgumentException("데이터베이스에 없는 제품입니다."));

        return SearchResponseDto.builder()
                .productId(defaultSubstance.getSubstanceId())
                .productName(defaultSubstance.getSubstanceName())
                .build();
    }

}
