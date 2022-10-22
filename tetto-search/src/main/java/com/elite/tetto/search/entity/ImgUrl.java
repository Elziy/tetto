package com.elite.tetto.search.entity;

import lombok.Data;

@Data
public class ImgUrl {
    private String url;
    
    /**
     * 宽度
     */
    private Integer width;
    
    /**
     * 高度
     */
    private Integer height;
}
