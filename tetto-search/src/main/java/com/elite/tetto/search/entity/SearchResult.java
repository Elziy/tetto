package com.elite.tetto.search.entity;

import com.elite.tetto.search.entity.vo.AtlasRes;
import lombok.Data;

import java.util.List;

@Data
public class SearchResult {
    /**
     * 总数
     */
    private Long totalCount;
    
    /**
     * 每页记录数
     */
    private int pageSize;
    
    /**
     * 总页数
     */
    private int totalPage;
    
    /**
     * 当前页数
     */
    private int currPage;
    
    /**
     * 相关标签
     */
    private List<String> tags;
    
    /**
     * 相关图集
     */
    private List<AtlasRes> atlas;
}
