package com.nemo.mixsafe.dto.Ai;

import com.nemo.mixsafe.domain.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "ai 요청 DTO")
public class AiRequestDto {

    @Schema(description = "성분 casNo")
    private String casNo;

    @Schema(description = "성분이 들어있는 제품명")
    private String productName;
}
