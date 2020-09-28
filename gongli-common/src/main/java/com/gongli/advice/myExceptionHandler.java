package com.gongli.advice;


import com.gongli.enums.ExceptionEnum;
import com.gongli.exception.myException;
import com.gongli.vo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class myExceptionHandler {


    @ExceptionHandler(myException.class)
    public ResponseEntity<ExceptionResult> handlerException(myException myException){

        ExceptionEnum exceptionEnum = myException.getExceptionEnum();

        return ResponseEntity.status(exceptionEnum.getCode()).body(new ExceptionResult(myException));

    }

}
