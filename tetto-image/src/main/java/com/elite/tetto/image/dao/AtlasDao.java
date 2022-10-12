package com.elite.tetto.image.dao;

import com.elite.tetto.image.entity.AtlasEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.elite.tetto.image.entity.vo.AtlasRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-10-07 14:39:01
 */
@Mapper
public interface AtlasDao extends BaseMapper<AtlasEntity> {
    
    /**
     * 获取用户点赞图集通过用户id
     *
     * @param uid 用户id
     * @return {@link List}<{@link AtlasEntity}>
     */
    List<AtlasEntity> getLikeAtlasByUid(@Param("uid") Long uid);
    
    /**
     * 获取新图集
     * 获取最新图集
     *
     * @param limit 限制
     * @param uid   用户id
     * @return {@link List}<{@link AtlasRes}>
     */
    List<AtlasRes> getNewAtlas(@Param("limit") int limit, @Param("uid") Long uid);
}
