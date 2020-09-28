package com.gongli.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public enum ExceptionEnum {

    price_cannot_null(400,"价格不能为空"),
    brand_cannot_null(400,"商标不能为空"),
    category_cannot_null(400,"种类不能为空"),
    ;

    ExceptionEnum(int code,String msg){
        this.code=code;
        this.msg=msg;
    };


    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    private int code;
    private String msg;
}
