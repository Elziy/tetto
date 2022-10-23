package com.elite.tetto.search.entity.vo;

import lombok.Data;

@Data
public class SuggestTags {
    
    /**
     * 标签
     */
    private String tag;
    
    /**
     * 建议
     */
    private String suggest;
}
