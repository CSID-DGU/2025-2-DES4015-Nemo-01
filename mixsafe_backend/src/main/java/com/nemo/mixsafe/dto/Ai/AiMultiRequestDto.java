package com.nemo.mixsafe.dto.Ai;

import com.nemo.mixsafe.domain.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "AI 다중 제품 요청 DTO")
public class AiMultiRequestDto {

    @Schema(description = "분석할 제품 리스트", example = "[{...}, {...}]")
    private List<AiRequestDto> products;

    @Schema(description = "AI 분석 기능 사용 여부", example = "true")
    private boolean useAi;
}
