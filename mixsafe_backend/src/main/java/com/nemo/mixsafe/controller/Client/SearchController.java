package com.nemo.mixsafe.controller.Client;

import com.nemo.mixsafe.dto.Client.*;
import com.nemo.mixsafe.service.Client.ProductService;
import com.nemo.mixsafe.service.Client.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;
    private final ProductService productService;

    @GetMapping("/substance")
    public SearchResponseDto searchSubstance(@RequestParam String substanceName){
        return productService.searchDefaultSubstance(substanceName);
    }

    @GetMapping("/product")
    public SearchResponseDto searchProduct(@RequestParam String productName) {
        return productService.searchProductByName(productName);
    }

    @PostMapping("/mix")
    public AiResultResponseDto mixProductResult(@RequestBody MixRequestDto requestDto) {

        return searchService.analyzeMix(requestDto);

    }

    @PostMapping("/search/ocr-image")
    public SearchResponseDto searchProductByOcrImage(@RequestBody OcrImageRequestDto dto) throws IOException {
        return searchService.searchProductByOcrImage(dto);
    }

}
