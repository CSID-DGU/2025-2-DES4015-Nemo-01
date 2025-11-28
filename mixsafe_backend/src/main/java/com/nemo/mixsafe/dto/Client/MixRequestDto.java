package com.nemo.mixsafe.dto.Client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@Schema(description = "검색 시 제품id 전달 DTO")
public class MixRequestDto {

    @Schema(description = "제품 1의 id")
    private Long product1Id;

    @Schema(description = "제품 2의 id")
    private Long product2Id;

    @Schema(description = "제품인지 기본물질인지 판단(제품 -> prd , 기본물질 -> defualt )")
    private String source1;

    @Schema(description = "제품인지 기본물질인지 판단(제품 -> prd , 기본물질 -> defualt )")
    private String source2;

}
