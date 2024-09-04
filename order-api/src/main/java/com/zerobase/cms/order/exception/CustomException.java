package com.zerobase.cms.order.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private final Errorcode errorcode;
    private final int status;
    private static final ObjectMapper mapper = new ObjectMapper();

    public CustomException(Errorcode errorcode){
        super(errorcode.getDetail());
        this.errorcode = errorcode;
        this.status = errorcode.getHttpStatus().value();
    }
}
