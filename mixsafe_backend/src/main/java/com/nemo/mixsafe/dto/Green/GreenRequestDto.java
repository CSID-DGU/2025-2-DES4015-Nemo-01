package com.nemo.mixsafe.dto.Green;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "초록누리 ")
public class GreenRequestDto {

    @Schema(description = "어떤 api 호출할지 결정(3.chmstryProductList/4.chmstryProductDetail/5.chmstryProductCntnrIndtList")
    private String ServiceName;

    @Schema(description = "api 권한 인증키")
    private String AuthKey;

    @Schema(description = "제품군 코드(3에서 필수)")
    private String prdtarmCd;

    @Schema(description = "제품 마스터 번호(4,5에서만 사용)")
    private String prdtMstrNo;

}
