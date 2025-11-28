package com.nemo.mixsafe.dto.Client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Schema(description = "검색 후 제품 반환 DTO")
public class SearchResponseDto {

    @Schema(description = "제품명")
    private String productName;

    @Schema(description = "제품 아이디")
    private Long productId;

}
