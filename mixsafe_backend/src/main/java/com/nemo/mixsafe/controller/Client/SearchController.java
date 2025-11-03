package com.nemo.mixsafe.controller.Client;

import com.nemo.mixsafe.dto.Client.SearchRequestDto;
import com.nemo.mixsafe.dto.Client.SearchResponseDto;
import com.nemo.mixsafe.service.Client.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @PostMapping
    public SearchResponseDto searchProduct(@RequestBody SearchRequestDto requestDto) {
        return searchService.searchProductByName(requestDto);
    }
}
