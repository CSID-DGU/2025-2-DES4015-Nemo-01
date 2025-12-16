package com.nemo.mixsafe.controller.Client;

import com.nemo.mixsafe.dto.Client.*;
import com.nemo.mixsafe.service.Client.ProductService;
import com.nemo.mixsafe.service.Client.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Search API", description = "제품/물질 검색 및 혼합 분석 API")
public class SearchController {

    private final SearchService searchService;
    private final ProductService productService;

    @Operation(
            summary = "기본 물질 검색",
            description = "물, 식초, 알코올 등 내부에 저장된 기본 물질을 검색합니다.",
            parameters = {
                    @Parameter(name = "substanceName", description = "검색할 기본 물질명", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색 성공",
                            content = @Content(schema = @Schema(implementation = SearchResponseDto.class))),
            }
    )
    @GetMapping("/substance")
    public SearchResponseDto searchSubstance(@RequestParam String substanceName){
        return productService.searchDefaultSubstance(substanceName);
    }

    @Operation(
            summary = "제품 검색",
            description = "제품명으로 초록누리 기반 제품을 검색합니다.",
            parameters = {
                    @Parameter(name = "productName", description = "검색할 제품명", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색 성공",
                            content = @Content(schema = @Schema(implementation = SearchResponseDto.class))),
            }
    )
    @GetMapping("/product")
    public SearchResponseDto searchProduct(@RequestParam String productName) {
        return productService.searchProductByName(productName);
    }

    @Operation(
            summary = "혼합 위험 분석",
            description = "선택된 두 제품/물질의 혼합 위험성을 AI 기반으로 분석합니다.\n 제품은 prd, 기본물질은 default로 요청해야합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "분석 성공",
                            content = @Content(schema = @Schema(implementation = AiResultResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청",
                            content = @Content(schema = @Schema()))
            }
    )
    @PostMapping("/mix")
    public AiResultResponseDto mixProductResult(@RequestBody MixRequestDto requestDto) {

        return searchService.analyzeMix(requestDto);

    }

    @Operation(
            summary = "OCR 이미지 기반 제품 검색",
            description = "이미지를 OCR 분석하여 자동으로 제품명을 인식하고 검색합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색 성공",
                            content = @Content(schema = @Schema(implementation = SearchResponseDto.class))),
                    @ApiResponse(responseCode = "500", description = "OCR 처리 실패",
                            content = @Content(schema = @Schema()))
            }
    )
    @PostMapping("/search/ocr-image")
    public SearchResponseDto searchProductByOcrImage(@RequestBody OcrImageRequestDto dto) throws IOException {
        return searchService.searchProductByOcrImage(dto);
    }



}
