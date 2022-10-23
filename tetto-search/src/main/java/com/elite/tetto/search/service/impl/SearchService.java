package com.elite.tetto.search.service.impl;

import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.search.entity.SearchParam;
import com.elite.tetto.search.entity.SearchResult;
import com.elite.tetto.search.entity.vo.SuggestTags;

import java.util.List;

public interface SearchService {
    /**
     * 从es中检索图集
     *
     * @param searchParam 检索参数
     * @return {@link PageUtils}
     */
    SearchResult searchAtlasPage(SearchParam searchParam);
    
    /**
     * 从es中检索相关标签
     *
     * @param tag 标签
     * @return {@link PageUtils}
     */
    List<String> searchTags(String tag);
    
    /**
     * 获取联想标签
     *
     * @param tag 标签
     * @return {@link List}<{@link String}>
     */
    List<SuggestTags> getSuggestTags(String tag);
}
