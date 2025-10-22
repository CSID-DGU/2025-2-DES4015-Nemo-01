package com.nemo.mixsafe.dto.Ai;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "ai 응답 DTO")

public class AiResponseDto {

    @Schema(description = "ai 분석 결과")
    private String Text;

}
