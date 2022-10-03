package com.elite.tetto.upload.entity;

public class R {
    private int code;
    
    private String msg;
    
    private String url;
    
    public R(int code, String msg, String url) {
        this.code = code;
        this.msg = msg;
        this.url = url;
    }
    
    public R() {
    }
    
    public int getCode() {
        return code;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
    
    public String getMsg() {
        return msg;
    }
    
    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
}
