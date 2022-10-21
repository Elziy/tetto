package com.elite.tetto.recommend.service.impl;

import com.elite.tetto.recommend.dao.RecommendDao;
import com.elite.tetto.recommend.service.RecommendService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RecommendServiceImpl implements RecommendService {
    
    @Resource
    private RecommendDao recommendDao;
    
    /**
     * 获取推荐图集id
     *
     * @param uid   用户id
     * @param limit 限制
     * @return {@link List}<{@link Long}>
     */
    @Override
    public List<Long> getRecommendAtlasIds(Long uid, Long limit) {
        List<String> tags = this.getRecommendTags(uid, 8L);
        // 没有标签则获取随机图集
        if (tags == null || tags.size() == 0) {
            return recommendDao.getRandomAtlasIds(uid, limit);
        }
        List<Long> recommendAtlasIds = recommendDao.getRecommendAtlasIds(uid, tags, limit);
        // 没有推荐图集则获取随机图集
        if (recommendAtlasIds == null || recommendAtlasIds.size() == 0) {
            return recommendDao.getRandomAtlasIds(uid, limit);
        } else {
            // 推荐图集不足则获取随机图集
            if (recommendAtlasIds.size() < limit) {
                List<Long> randomAtlasIds = recommendDao.getRandomAtlasIds(uid, limit - recommendAtlasIds.size());
                recommendAtlasIds.addAll(randomAtlasIds);
            } else if (recommendAtlasIds.size() > limit) {
                // 推荐图集过多则截取
                recommendAtlasIds = recommendAtlasIds.subList(0, limit.intValue());
            }
        }
        return recommendAtlasIds;
    }
    
    /**
     * 获取推荐标签
     *
     * @param uid   用户id
     * @param limit 限制
     * @return {@link List}<{@link String}>
     */
    @Override
    public List<String> getRecommendTags(Long uid, Long limit) {
        return recommendDao.getRecommendAtlasTags(uid, limit);
    }
}
