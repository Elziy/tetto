package com.elite.tetto.recommend.handler;

import com.elite.tetto.common.exception.ExceptionCode;
import com.elite.tetto.common.utils.R;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.elite.tetto.image.controller")
public class ExceptionHandler {
    
    /**
     * 配置了全局异常处理后需要单独处理验证失败的异常
     *
     * @return {@link R}
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(InternalAuthenticationServiceException.class)
    public R passwordInvalidExceptionHandle() {
        return R.error(ExceptionCode.UNAUTHORIZED.getCode(),
                ExceptionCode.UNAUTHORIZED.getMsg());
    }
    
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public R exceptionHandle(Throwable e) {
        e.printStackTrace();
        return R.error(ExceptionCode.UNKNOWN_EXCEPTION.getCode(), ExceptionCode.UNKNOWN_EXCEPTION.getMsg());
    }
}
