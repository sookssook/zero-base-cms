package com.zerobase.user.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private final Errorcode errorCode;

    public CustomException(Errorcode errorCode){
        super(errorCode.getDetail());
        this.errorCode = errorCode;
    }
}
