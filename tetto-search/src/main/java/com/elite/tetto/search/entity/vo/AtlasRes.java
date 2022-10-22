package com.elite.tetto.search.entity.vo;

import com.elite.tetto.search.entity.AtlasEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AtlasRes extends AtlasEntity {
    /**
     * 作者用户名
     */
    private String username;
    
    /**
     * 作者头像
     */
    private String avatar;
}
