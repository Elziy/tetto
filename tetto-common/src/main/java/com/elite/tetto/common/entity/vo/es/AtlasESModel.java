package com.elite.tetto.common.entity.vo.es;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AtlasESModel {
    private Long id;
    
    private Long uid;
    
    private String title;
    
    private String introduce;
    
    private List<String> tags;
    
    private List<ImgUrl> imgUrls;
    
    private String thumbnailUrl;
    
    private int isPublic;
    
    /**
     * 上传日期
     */
    private Date date;
    
    /**
     * 作者用户名
     */
    private String username;
    
    /**
     * 作者头像
     */
    private String avatar;
}
