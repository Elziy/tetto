package com.elite.tetto.image.service.impl.cache;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.elite.tetto.common.constant.ImageConstant;
import com.elite.tetto.image.entity.ImgsEntity;
import com.elite.tetto.image.service.ImgsService;
import com.elite.tetto.image.service.impl.ImgsServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("imgServiceCache")
public class ImgServiceCache extends ImgsServiceImpl implements ImgsService {
    @Override
    @Cacheable(value = ImageConstant.ATLAS_IMGS_CACHE_PREFIX, key = "#aid")
    public List<ImgsEntity> getImgsByAid(Long aid) {
        LambdaQueryWrapper<ImgsEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ImgsEntity::getAtlasId, aid);
        return this.list(wrapper);
    }
}
