package com.gongli.vo;

import com.gongli.exception.myException;
import lombok.Data;


public class ExceptionResult {

    private int status;
    private String msg;
    private Long timeStamp;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ExceptionResult(myException myException) {
        status = myException.getExceptionEnum().getCode();
        msg = myException.getExceptionEnum().getMsg();
        timeStamp = System.currentTimeMillis();

    }

    @Override
    public String toString() {
        return "ExceptionResult{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
