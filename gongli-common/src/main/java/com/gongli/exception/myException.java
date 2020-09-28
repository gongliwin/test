package com.gongli.exception;

import com.gongli.enums.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;



public class myException extends RuntimeException {

    private ExceptionEnum exceptionEnum;

    public myException(ExceptionEnum exceptionEnum){
        this.exceptionEnum=exceptionEnum;
    }

    public ExceptionEnum getExceptionEnum() {
        return exceptionEnum;
    }
}
