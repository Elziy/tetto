package com.elite.tetto.search.service;

import com.elite.tetto.common.entity.vo.es.AtlasESModel;

public interface SaveService {
    /**
     * 保存图集到es
     *
     * @param atlasESModel 图集
     * @return boolean
     */
    boolean saveAtlas(AtlasESModel atlasESModel);
    
    /**
     * 删除图集
     *
     * @param aid 图集id
     * @return boolean
     */
    boolean deleteAtlas(Long aid);
}
