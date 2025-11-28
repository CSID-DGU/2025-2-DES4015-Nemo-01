package com.nemo.mixsafe.controller.Client;

import com.nemo.mixsafe.dto.Client.*;
import com.nemo.mixsafe.service.Client.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @PostMapping("/search")
    public SearchResponseDto searchProduct(@RequestBody SearchRequestDto requestDto) {
        return searchService.searchProductByName(requestDto);
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
