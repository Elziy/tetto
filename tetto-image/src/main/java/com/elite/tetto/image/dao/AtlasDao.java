package com.elite.tetto.image.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.elite.tetto.image.entity.AtlasEntity;
import com.elite.tetto.image.entity.vo.AtlasRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-10-07 14:39:01
 */
@Mapper
public interface AtlasDao extends BaseMapper<AtlasEntity> {
    /**
     * 通过用户id获取用户点赞图集(分页)
     *
     * @param page 页面
     * @param uid  用户id
     * @return {@link Page}<{@link AtlasEntity}>
     */
    Page<AtlasEntity> getLikeAtlasPageByUid(@Param("page") IPage<AtlasEntity> page, @Param("uid") Long uid);
    
    /**
     * 获取最新图集
     *
     * @param limit 限制
     * @param uid   用户id
     * @return {@link List}<{@link AtlasRes}>
     */
    List<AtlasRes> getNewAtlas(@Param("limit") int limit, @Param("uid") Long uid);
    
    /**
     * 获取推荐图集
     *
     * @param ids 图集id
     * @param uid 用户id
     * @return {@link List}<{@link AtlasRes}>
     */
    List<AtlasRes> getRecommendAtlas(@Param("ids") List<Long> ids, @Param("uid") Long uid, @Param("limit") int limit);
    
    /**
     * 通过图集id列表获取图集res
     *
     * @param atlasIdList 图集id列表
     * @return {@link List}<{@link AtlasRes}>
     */
    List<AtlasRes> getAtlasResByAidList(@Param("ids") List<Long> atlasIdList);
}
