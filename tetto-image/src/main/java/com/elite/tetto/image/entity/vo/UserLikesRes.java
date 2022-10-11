package com.elite.tetto.image.entity.vo;

import com.elite.tetto.image.entity.AtlasEntity;
import lombok.Data;

@Data
public class UserLikesRes extends AtlasEntity {
    
    private String username;
    
    private String avatar;
}
