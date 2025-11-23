package com.nemo.mixsafe.service.Client;

import com.nemo.mixsafe.domain.Product;
import com.nemo.mixsafe.dto.Client.SearchRequestDto;
import com.nemo.mixsafe.dto.Client.SearchResponseDto;
import com.nemo.mixsafe.repository.Client.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;

    public SearchResponseDto searchProductByName(SearchRequestDto requestDto) {
        Product product = searchRepository.findFirstByProductName(requestDto.getProductName())
                .orElseThrow(() -> new IllegalArgumentException("해당 제품이 존재하지 않습니다."));

        return SearchResponseDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .build();
    }

    public
}
