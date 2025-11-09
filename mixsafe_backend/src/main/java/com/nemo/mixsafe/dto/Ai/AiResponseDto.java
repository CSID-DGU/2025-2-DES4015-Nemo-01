package com.nemo.mixsafe.dto.Ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Schema(description = "ai 응답 DTO")

public class AiResponseDto {

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("simple_response")
    private SimpleResponse simpleResponse;

    @Data
    public static class SimpleResponse {
        @JsonProperty("risk_level")
        private String riskLevel;

        @JsonProperty("message")
        private String message;
    }

}
