package com.nemo.mixsafe.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum GreenApiErrorCode {

    ERROR00001("ERROR00001", "서비스명이 설정되지 않음"),
    ERROR00002("ERROR00002", "인증키가 설정되지 않음"),
    ERROR00010("ERROR00010", "등록되지 않은 서비스명 호출"),
    ERROR00020("ERROR00020", "서비스가 중지된 서비스 호출"),
    ERROR00030("ERROR00030", "호출된 서비스에 필수 Parameter 누락"),
    ERROR00040("ERROR00040", "서비스 승인이전"),
    ERROR00050("ERROR00050", "과도한 PageCount 값", "1 ~ 20 사이 정수"),
    ERROR99999("ERROR99999", "정의되지 않은 기타오류"),
    SUCCESS("0000", "정상 응답");

    private final String code;
    private final String message;
    private String detail;

    GreenApiErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
        this.detail = "";
    }

    GreenApiErrorCode(String code, String message, String detail) {
        this.code = code;
        this.message = message;
        this.detail = detail;
    }

    public static GreenApiErrorCode fromCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return ERROR99999;
        }

        for (GreenApiErrorCode errorCode : values()) {
            if (errorCode.getCode().equals(code)) {
                return errorCode;
            }
        }
        return ERROR99999;
    }

    public boolean isSuccess() {
        return this == SUCCESS;
    }

    public String getFullMessage() {
        if (detail != null && !detail.isEmpty()) {
            return String.format("%s (%s) - %s", code, message, detail);
        }
        return String.format("%s (%s)", code, message);
    }
}