package com.elite.tetto.image.entity.vo;

import lombok.Data;

/**
 * img url
 *
 * @author Elziy
 */
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
