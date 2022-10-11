package com.elite.tetto.image.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.image.entity.LikeEntity;

import java.util.Map;

/**
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-10-11 20:25:39
 */
public interface LikeService extends IService<LikeEntity> {
    
    PageUtils queryPage(Map<String, Object> params);
    
    /**
     * 用户是否点赞
     *
     * @param uid 用户id
     * @param aid 图集id
     * @return boolean
     */
    boolean isLike(Long uid, Long aid);
    
    /**
     * 添加点赞
     *
     * @param aid 图集id
     * @return boolean
     */
    boolean addLike(Long aid);
    
    /**
     * 取消点赞
     *
     * @param aid 图集id
     * @return boolean
     */
    boolean deleteLike(Long aid);
}

