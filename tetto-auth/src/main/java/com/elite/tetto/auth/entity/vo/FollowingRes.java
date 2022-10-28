package com.elite.tetto.auth.entity.vo;

import lombok.Data;

@Data
public class FollowingRes {
    private Long uid;
    
    private String username;
    
    /**
     * 头像url
     */
    private String avatar;
    
    /**
     * 介绍
     */
    private String introduce;
    
    /**
     * 是否被关注
     */
    private boolean isFollowed;
}
