package com.elite.tetto.image.service;

import com.elite.tetto.common.entity.vo.es.AtlasESModel;

import java.util.List;

public interface ESService {
    /**
     * 获取图集的所有信息
     *
     * @return {@link List}<{@link AtlasESModel}>
     */
    List<AtlasESModel> getAtlasESModels();
}
