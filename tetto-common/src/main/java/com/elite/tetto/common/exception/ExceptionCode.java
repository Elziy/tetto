/*
    Author: 刘子阳.
    Date: 2022-07-30 14:36.
    Created by IntelliJ IDEA.
*/
package com.elite.tetto.common.exception;

public enum ExceptionCode {
    
    UNKNOWN_EXCEPTION(10000, "未知异常"),
    
    VALID_EXCEPTION(10001, "参数不合法"),
    
    PRODUCT_UP_EXCEPTION(11000, "商品上架异常"),
    
    USER_EXIST_EXCEPTION(150001, "用户名已存在"),
    
    PHONE_EXIST_EXCEPTION(150002, "手机号已存在"),
    
    LOGIN_PASSWORD_INVALID_EXCEPTION(150003, "账号或密码错误"),;
    
    
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
