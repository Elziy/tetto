package com.elite.tetto.search.service.impl;

import com.elite.tetto.common.utils.PageUtils;

import java.util.Map;

public interface SearchService {
     /**
      * 分页检索图集
      *
      * @param params 检索参数
      * @return {@link PageUtils}
      */
     PageUtils searchAtlasPage(Map<String, Object> params);
}
