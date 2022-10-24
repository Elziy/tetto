package com.elite.tetto.recommend.service.impl;

import com.elite.tetto.common.constant.RecommendConstant;
import com.elite.tetto.recommend.dao.RecommendDao;
import com.elite.tetto.recommend.entity.UserTagsEntity;
import com.elite.tetto.recommend.service.RecommendService;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecommendServiceImpl implements RecommendService {
    
    @Resource
    private RecommendDao recommendDao;
    
    /**
     * 获取推荐图集id
     *
     * @param uid   用户id
     * @param limit 限制
     * @param tags
     * @return {@link List}<{@link Long}>
     */
    @Override
    public List<Long> getRecommendAtlasIds(Long uid, Long limit, List<String> tags) {
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
        Set<UserTagsEntity> likeAtlasTags = recommendDao.getLikeAtlasTags(uid, limit);
        // 获取三天内浏览记录的标签
        Date time = DateUtils.addDays(new Date(), -3);
        Set<UserTagsEntity> historyAtlasTags = recommendDao.getHistoryAtlasTags(uid, 2 * limit, time);
        // 将标签按名字为key，权重为value存入map
        Map<String, Long> likeMap = likeAtlasTags.stream()
                .collect(Collectors.toMap(UserTagsEntity::getTag,
                        (userTagsEntity) -> userTagsEntity.getCount() * RecommendConstant.LIKE_TAG_WEIGHT));
        Map<String, Long> historyMap = historyAtlasTags.stream()
                .collect(Collectors.toMap(UserTagsEntity::getTag,
                        (userTagsEntity) -> userTagsEntity.getCount() * RecommendConstant.HISTORY_TAG_WEIGHT));
        
        // 合并标签，相同标签权重相加
        likeMap.forEach((k, v) -> {
            if (historyMap.containsKey(k)) {
                historyMap.put(k, historyMap.get(k) + v);
            } else {
                historyMap.put(k, v);
            }
        });
        
        // 根据权重排序
        List<String> tags = historyMap.entrySet().stream()
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                .map(Map.Entry::getKey)
                .limit(limit)
                .collect(Collectors.toList());
        System.out.println(tags);
        return tags;
    }
    
    /**
     * 获取热门标签
     *
     * @param uid   用户id
     * @param limit 限制
     * @return {@link List}<{@link String}>
     */
    @Override
    public List<String> getHotTags(Long uid, Long limit) {
        return null;
    }
}
