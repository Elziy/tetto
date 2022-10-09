package com.elite.tetto.image.entity.vo;

import com.elite.tetto.image.entity.AtlasEntity;
import com.elite.tetto.image.entity.ImgsEntity;
import com.elite.tetto.image.entity.to.UserInfoRes;
import lombok.Data;

import java.util.List;

/**
 * 作品集返回体
 *
 * @author Elziy
 */
@Data
public class ImgRes {
    /**
     * 作品集id
     */
    private Long aId;
    
    /**
     * 作者信息
     */
    UserInfoRes userInfoRes;
    
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
     * 最新作品集
     */
    List<AtlasEntity> latestAtlas;
}
