package com.elite.tetto.search.entity;

import lombok.Data;

@Data
public class SearchParam {
    
    /**
     * 关键字
     */
    private String keyword;
    
    /**
     * 页码
     */
    private Integer page = 1;
    
    /**
     * 排序条件
     */
    private String sort;
}
