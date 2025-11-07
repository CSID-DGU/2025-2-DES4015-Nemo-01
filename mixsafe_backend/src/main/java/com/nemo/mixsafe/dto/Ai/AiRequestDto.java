package com.nemo.mixsafe.dto.Ai;

import com.nemo.mixsafe.domain.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "AI 요청 DTO")
public class AiRequestDto {

    @Schema(description = "성분 CAS 번호 리스트", example = "[\"103-95-7\", \"64-17-5\"]")
    private List<String> casNumbers;

    @Schema(description = "성분이 들어있는 제품명", example = "Bleach Cleaner")
    private String productName;
}
