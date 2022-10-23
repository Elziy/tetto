package com.elite.tetto.search.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.elite.tetto.search.entity.vo.AtlasRes;
import com.elite.tetto.search.entity.vo.SuggestTags;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
    
    /**
     * 获取联想标签
     *
     * @param tag 标签
     * @return {@link List}<{@link String}>
     */
    List<SuggestTags> getSuggestTags(@Param("tag") String tag);
}
