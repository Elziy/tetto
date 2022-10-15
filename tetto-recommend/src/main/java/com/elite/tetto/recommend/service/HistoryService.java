package com.elite.tetto.recommend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.recommend.entity.HistoryEntity;

import java.util.Map;

/**
 * 
 *
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-10-15 21:19:41
 */
public interface HistoryService extends IService<HistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    /**
     * 添加浏览历史
     *
     * @param uid 用户id
     * @param aid 图集id
     * @return boolean
     */
    boolean addHistory(Long uid, Long aid);
}

