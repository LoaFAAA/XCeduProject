package com.hao.base.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    //响应码。1代表成功，0代表失败
    private Integer code;
    //错误信息
    private String message;
    //响应对象
    private T data;

    public static <T> Result<T> success(T t){
        Result<T> result = new Result<T>();
        result.code = 1;
        return result;
    }
}
