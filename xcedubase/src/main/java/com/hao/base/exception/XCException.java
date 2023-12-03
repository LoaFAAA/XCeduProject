package com.hao.base.exception;

/**
 * 自定义错误
 */

public class XCException extends RuntimeException{
    private String ErrMessage;

    public XCException() {
        super();
    }

    public String getErrMessage() {
        return ErrMessage;
    }

    public void setErrMessage(String errMessage) {
        ErrMessage = errMessage;
    }

    public XCException(String errMessage) {
        super(errMessage);
        this.ErrMessage = errMessage;
    }


}
