package com.gongli.item.controller;


import com.gongli.enums.ExceptionEnum;
import com.gongli.exception.myException;
import com.gongli.item.controller.pojo.item;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class itemController {
    int i=0;

    @GetMapping("/item")
    public ResponseEntity<item> item(){

        if (i==0){
            i++;
            throw new myException(ExceptionEnum.price_cannot_null);
        }else {
            i--;
            return ResponseEntity.status(HttpStatus.CREATED).body(new item());
        }
    }
}
