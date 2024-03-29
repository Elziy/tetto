package com.elite.tetto.image.handler;

import com.elite.tetto.common.exception.ExceptionCode;
import com.elite.tetto.common.utils.R;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

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
    
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public R validExceptionHandle(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        Map<String, String> map = new HashMap<>();
        result.getFieldErrors()
                .forEach(item -> {
                    String field = item.getField();
                    String message = item.getDefaultMessage();
                    map.put(field, message);
                });
        return R.error(ExceptionCode.VALID_EXCEPTION.getCode(), ExceptionCode.VALID_EXCEPTION.getMsg())
                .put("data", map);
    }
    
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public R exceptionHandle(Throwable e) {
        e.printStackTrace();
        return R.error(ExceptionCode.UNKNOWN_EXCEPTION.getCode(), ExceptionCode.UNKNOWN_EXCEPTION.getMsg());
    }
}
