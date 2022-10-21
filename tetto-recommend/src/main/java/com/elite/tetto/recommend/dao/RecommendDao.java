package com.elite.tetto.recommend.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 推荐
 *
 * @author Elziy
 */
public interface RecommendDao {
    
    /**
     * 获取随机图集id
     *
     * @param uid   用户id
     * @param limit 限制
     * @return {@link List}<{@link Long}>
     */
    List<Long> getRandomAtlasIds(@Param("uid") Long uid, @Param("limit") Long limit);
    
    /**
     * 获取推荐图集id
     *
     * @param tags  标签
     * @param limit 限制
     * @return {@link List}<{@link Long}>
     */
    List<Long> getRecommendAtlasIds(@Param("uid") Long uid, @Param("tags") List<String> tags, @Param("limit") Long limit);
    
    
    /**
     * 获取推荐图集标签
     *
     * @param uid   用户id
     * @param limit 限制
     * @return {@link List}<{@link String}>
     */
    List<String> getRecommendAtlasTags(@Param("uid") Long uid, @Param("limit") Long limit);
    
    
}
