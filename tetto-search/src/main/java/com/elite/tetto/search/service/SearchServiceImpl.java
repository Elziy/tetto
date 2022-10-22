package com.elite.tetto.search.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.Query;
import com.elite.tetto.search.dao.SearchDao;
import com.elite.tetto.search.entity.vo.AtlasRes;
import com.elite.tetto.search.service.impl.SearchService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

@Service
public class SearchServiceImpl implements SearchService {
    
    @Resource
    private SearchDao searchDao;
    
    /**
     * 分页检索图集
     *
     * @param params 检索参数
     * @return {@link PageUtils}
     */
    @Override
    public PageUtils searchAtlasPage(Map<String, Object> params) {
        IPage<AtlasRes> page = new Query<AtlasRes>().getPage(params);
        String keyword = (String) params.get("keyword");
        if (Objects.isNull(keyword) || keyword.isEmpty()) {
            throw new RuntimeException("检索关键字不能为空");
        }
        Page<AtlasRes> atlasResPage = searchDao.searchAtlas(page, keyword);
        return new PageUtils(atlasResPage);
    }
}
