package com.elite.tetto.image.entity.vo;

import com.elite.tetto.image.entity.AtlasEntity;
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
