package com.elite.tetto.auth.handler;

import com.elite.tetto.common.exception.ExceptionCode;
import com.elite.tetto.common.utils.R;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.elite.tetto.auth.controller")
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(Throwable.class)
    public R exceptionHandle(Throwable e) {
        e.printStackTrace();
        return R.error(ExceptionCode.UNKNOWN_EXCEPTION.getCode(), ExceptionCode.UNKNOWN_EXCEPTION.getMsg());
    }
}
