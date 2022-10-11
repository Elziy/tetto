package com.elite.tetto.image.service.impl.cache;

import com.elite.tetto.common.constant.ImageConstant;
import com.elite.tetto.image.entity.AtlasEntity;
import com.elite.tetto.image.service.impl.AtlasServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("atlasServiceCache")
public class AtlasServiceCache extends AtlasServiceImpl {
    @Override
    @Cacheable(value = ImageConstant.USER_ALL_ATLAS, key = "#uid")
    public List<AtlasEntity> getAllAtlasByUid(Long uid) {
        return super.getAllAtlasByUid(uid);
    }
}
