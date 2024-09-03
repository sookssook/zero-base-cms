package com.zerobase.user.exception;

import lombok.Getter;

@Getter
public class CustomerException extends RuntimeException{
    private final Errorcode errorCode;

    public CustomerException(Errorcode errorCode){
        super(errorCode.getDetail());
        this.errorCode = errorCode;
    }
}
