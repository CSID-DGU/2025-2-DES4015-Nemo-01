package com.nemo.mixsafe.dto.Client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "OCR 이미지 기반 제품 검색 요청 DTO")
public class OcrImageRequestDto {
    @Schema(
            description = "Base64 로 인코딩된 이미지 문자열",
            example = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA..."
    )
    private String base64Image;
}
