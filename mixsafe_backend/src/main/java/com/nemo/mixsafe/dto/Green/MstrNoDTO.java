package com.nemo.mixsafe.dto.Green;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Schema(description = "두 제품 마스터 번호 반환 DTO")
public class MstrNoDTO {
    private Long productId1;
    private Long productId2;
    private String prdtMstrNo1;
    private String prdtMstrNo2;
}
