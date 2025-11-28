package com.nemo.mixsafe.dto.Client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "클라이언트에게 전달되는 AI 분석 결과 DTO")
public class AiResultResponseDto {

    @Schema(description = "분석 대상 제품명", example = "곰팡이 제거제")
    private String productName;

    @Schema(description = "AI 분석 결과 텍스트")
    private String aiResult;

    @Schema(description = "분석 상태 (SUCCESS / FAILED)")
    private String status;
}
