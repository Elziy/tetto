/*
    Author: 刘子阳.
    Date: 2022-07-30 14:36.
    Created by IntelliJ IDEA.
*/
package com.elite.tetto.common.exception;

public enum ExceptionCode {
    
    UNAUTHORIZED(401, "认证失败"),
    
    FORBIDDEN(403, "权限不足"),
    
    UNKNOWN_EXCEPTION(10000, "未知异常"),
    
    VALID_EXCEPTION(10001, "参数不合法"),
    
    USER_EXIST_EXCEPTION(150001, "该邮箱已被注册"),
    
    PHONE_EXIST_EXCEPTION(150002, "手机号已存在"),
    
    LOGIN_PASSWORD_INVALID_EXCEPTION(150003, "用户名或密码错误"),
    
    USER_NOT_EXIST_EXCEPTION(150004, "用户不存在"),
    
    ATLAS_NOT_EXIST(150005, "作品集不存在"),
    
    SEND_CODE_EXCEPTION(150006, "发送验证码失败"),
    
    EMAIL_FORMAT_EXCEPTION(150007, "邮箱格式错误"),
    
    CODE_ERROR(150008, "验证码错误");
    
    
    private final Integer code;
    
    private final String msg;
    
    ExceptionCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getMsg() {
        return msg;
    }
}
