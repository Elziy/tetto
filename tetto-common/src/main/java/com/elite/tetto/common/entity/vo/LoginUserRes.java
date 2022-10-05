package com.elite.tetto.common.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class LoginUserRes {
    
    private Long uid;
    
    private String username;
    
    private String email;
    
    /**
     * 头像url
     */
    private String avatar;
    
    private String sex;
    
    private String introduce;
    
    private Date birthday;
    
    private String token;
    
}
