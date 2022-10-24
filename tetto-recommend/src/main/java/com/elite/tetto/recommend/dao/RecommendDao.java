package com.elite.tetto.recommend.dao;

import com.elite.tetto.recommend.entity.UserTagsEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

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
     * 获取用户喜欢的标签
     *
     * @param uid   用户id
     * @param limit 限制
     * @return {@link List}<{@link String}>
     */
    Set<UserTagsEntity> getLikeAtlasTags(@Param("uid") Long uid, @Param("limit") Long limit);
    
    /**
     * 获取最近用户浏览记录的标签
     *
     * @param uid   用户id
     * @param limit 限制
     * @param time  时间限制
     * @return {@link List}<{@link UserTagsEntity}>
     */
    Set<UserTagsEntity> getHistoryAtlasTags(@Param("uid") Long uid, @Param("limit") Long limit, @Param("time") Date time);
}
