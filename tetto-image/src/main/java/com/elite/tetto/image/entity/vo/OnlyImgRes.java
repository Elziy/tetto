package com.elite.tetto.image.entity.vo;

import com.elite.tetto.image.entity.AtlasEntity;
import com.elite.tetto.image.entity.ImgsEntity;
import lombok.Data;

import java.util.List;

@Data
public class OnlyImgRes {
    /**
     * 作品集id
     */
    private Long aId;
    
    /**
     * 作品集信息
     */
    private AtlasEntity atlas;
    
    /**
     * 图集图片信息
     */
    List<ImgsEntity> imgEntities;
    
    /**
     * 图集标签
     */
    List<String> tags;
    
    /**
     * 是否点赞
     */
    Boolean like;
}
