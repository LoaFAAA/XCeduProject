package com.hao.base.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RestResponse<T> {
    /**
     * 响应编码,0为正常,-1错误
     */
    private int code;

    /**
     * 响应提示信息
     */
    private String msg;

    /**
     * 响应内容
     */
    private T result;

    public RestResponse() {
        this.code = 0;
        this.msg = "success";
        this.result = null;
    }

    public RestResponse(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    /**
     * 错误信息的封装
     *
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> RestResponse<T> error(String msg){
        RestResponse<T> restRespnse = new RestResponse<>();
        restRespnse.setCode(-1);
        restRespnse.setMsg(msg);

        return restRespnse;
    }

    public static <T> RestResponse<T> error(String msg, T result){
        RestResponse<T> restRespnse = new RestResponse<>();
        restRespnse.setCode(-1);
        restRespnse.setMsg(msg);
        restRespnse.setResult(result);

        return restRespnse;
    }

    /**
     * 添加正常响应数据（包含响应内容）
     *
     * @return RestResponse Rest服务封装相应数据
     */
    public static <T> RestResponse<T> success(T result) {
        RestResponse<T> response = new RestResponse<T>();
        response.setResult(result);

        return response;
    }
    public static <T> RestResponse<T> success(T result,String msg) {
        RestResponse<T> response = new RestResponse<T>();
        response.setResult(result);
        response.setMsg(msg);

        return response;
    }

    public static <T> RestResponse<T> success(){

        return new RestResponse<>();
    }

    public boolean isSuccessful(){
        return this.code == 0;
    }
}
