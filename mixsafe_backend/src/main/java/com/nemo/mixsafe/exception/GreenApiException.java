package com.nemo.mixsafe.exception;

import lombok.Getter;

@Getter
public class GreenApiException extends RuntimeException {

    private final GreenApiErrorCode errorCode;

    public GreenApiException(GreenApiErrorCode errorCode) {
        super(errorCode.getFullMessage());
        this.errorCode = errorCode;
    }

    public GreenApiException(String resultCode) {
        this(GreenApiErrorCode.fromCode(resultCode));
    }
}