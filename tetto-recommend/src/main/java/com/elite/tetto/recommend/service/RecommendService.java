package com.elite.tetto.recommend.service;

import java.util.List;
import java.util.Set;

public interface RecommendService {
    
    /**
     * 获取推荐图集id
     *
     * @param uid   用户id
     * @param limit 限制
     * @param tags
     * @return {@link List}<{@link Long}>
     */
    List<Long> getRecommendAtlasIds(Long uid, Long limit, List<String> tags);
    
    
    /**
     * 获取喜欢的标签
     *
     * @param uid   用户id
     * @param limit 限制
     * @return {@link Set}<{@link String}>
     */
    Set<String> getLikeTags(Long uid, Long limit);
    
    
    /**
     * 获取推荐标签
     *
     * @param uid   用户id
     * @param limit 限制
     * @return {@link List}<{@link String}>
     */
    List<String> getRecommendTags(Long uid, Long limit);
    
    /**
     * 获取热门标签
     *
     * @return {@link List}<{@link String}>
     */
    Set<String> getHotTags(int limit);
}
