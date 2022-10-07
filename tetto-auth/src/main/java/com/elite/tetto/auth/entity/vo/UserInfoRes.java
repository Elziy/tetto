package com.elite.tetto.auth.entity.vo;

import lombok.Data;

import java.util.Date;

/**
 * 返回的用户信息
 *
 * @author Elziy
 */
@Data
public class UserInfoRes {
    private Long uid;
    
    private String username;
    
    private String email;
    
    /**
     * 头像url
     */
    private String avatar;
    
    private String sex;
    
    /**
     * 个人介绍
     */
    private String introduce;
    
    private Date birthday;
    
    /**
     * 粉丝数
     */
    private int followers;
    
    /**
     * 关注数
     */
    private int following;
    
    // 是否关注
    private Boolean isFollow;
    
    // 是否被关注
    private Boolean isFollowed;
}
