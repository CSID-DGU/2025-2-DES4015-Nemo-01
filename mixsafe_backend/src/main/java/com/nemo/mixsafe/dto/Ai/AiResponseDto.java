package com.nemo.mixsafe.dto.Ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Schema(description = "ai 응답 DTO")

public class AiResponseDto {

    @Schema(description = "AI 요청 처리 성공 여부", example = "true")
    @JsonProperty("success")
    private Boolean success;

    @Schema(description = "단순화된 위험도/메시지 응답")
    @JsonProperty("simple_response")
    private SimpleResponse simpleResponse;

    @Data
    @Schema(description = "간단한 위험도 및 메시지를 담는 내부 DTO")
    public static class SimpleResponse {

        @Schema(description = "위험도 레벨", example = "위험")
        @JsonProperty("risk_level")
        private String riskLevel;

        @Schema(
                description = "사용자에게 보여줄 안내 메시지",
                example = "이 두 제품을 함께 사용할 경우 유해 가스가 발생할 수 있습니다."
        )
        @JsonProperty("message")
        private String message;
    }

}
