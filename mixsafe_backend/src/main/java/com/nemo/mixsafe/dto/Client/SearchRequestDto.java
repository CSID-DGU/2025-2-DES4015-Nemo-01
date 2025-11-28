package com.nemo.mixsafe.dto.Client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "제품명 검색 DTO")
public class SearchRequestDto {

    @Schema(description = "제품명 검색할 때 전달")
    private String productName;
}
