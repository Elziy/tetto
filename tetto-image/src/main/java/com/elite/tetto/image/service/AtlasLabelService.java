package com.elite.tetto.image.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.image.entity.AtlasLabelEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-10-07 14:39:01
 */
public interface AtlasLabelService extends IService<AtlasLabelEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    /**
     * 通过图集id获取所有标签
     *
     * @param aid 图集id
     * @return {@link List}<{@link String}>
     */
    List<String> getAtlasLabelsByAid(Long aid);
}

