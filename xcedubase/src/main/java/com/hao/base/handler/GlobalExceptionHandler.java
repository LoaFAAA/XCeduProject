package com.hao.base.handler;

import com.hao.base.Constant.ErrMessageConstant;
import com.hao.base.exception.RestErrorInfoResponse;
import com.hao.base.exception.XCException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    //定义捕获自定义XCException时的处理
    @ResponseBody
    @ExceptionHandler(XCException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorInfoResponse globalException(XCException xce){
        log.info("系统存在错误："+xce.getErrMessage()+xce);
        String errMessage = xce.getErrMessage();

        return new RestErrorInfoResponse(errMessage);
    }

    //定义捕获系统的Exception时的处理
    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorInfoResponse globalException(Exception e){
        log.info("系统存在错误："+e.getMessage()+e);
        String errMessage = e.getMessage();

        return new RestErrorInfoResponse(e.getMessage());
    }
}
