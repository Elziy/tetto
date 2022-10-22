package com.elite.tetto.search.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.elite.tetto.search.entity.vo.AtlasRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SearchDao {
    
    /**
     * 检索图集
     *
     * @param page    页面参数
     * @param keyword
     * @return {@link Page}<{@link AtlasRes}>
     */
    Page<AtlasRes> searchAtlas(@Param("page") IPage<AtlasRes> page, @Param("keyword") String keyword);
}
