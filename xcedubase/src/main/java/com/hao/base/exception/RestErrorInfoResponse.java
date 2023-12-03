package com.hao.base.exception;

import java.io.Serializable;

/**
 * 错误响应参数包装
 */

public class RestErrorInfoResponse implements Serializable {
    private String ErrMessage;

    public RestErrorInfoResponse(String errMessage) {
        this.ErrMessage = errMessage;
    }

    public String getErrMessage() {
        return ErrMessage;
    }

    public void setErrMessage(String errMessage) {
        ErrMessage = errMessage;
    }
}
