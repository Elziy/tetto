package com.elite.tetto.image.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elite.tetto.common.constant.ImageConstant;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.Query;
import com.elite.tetto.image.dao.AtlasLabelDao;
import com.elite.tetto.image.entity.AtlasLabelEntity;
import com.elite.tetto.image.service.AtlasLabelService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("atlasLabelService")
public class AtlasLabelServiceImpl extends ServiceImpl<AtlasLabelDao, AtlasLabelEntity> implements AtlasLabelService {
    
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AtlasLabelEntity> page = this.page(
                new Query<AtlasLabelEntity>().getPage(params),
                new QueryWrapper<AtlasLabelEntity>()
        );
        
        return new PageUtils(page);
    }
    
    /**
     * 通过图集id获取所有标签
     *
     * @param aid 图集id
     * @return {@link List}<{@link String}>
     */
    @Override
    @Cacheable(value = ImageConstant.ATLAS_LABEL_CACHE_PREFIX, key = "#aid")
    public List<String> getAtlasLabelsByAid(Long aid) {
        return this.baseMapper.getAtlasLabelsByAid(aid);
    }
    
    /**
     * 通过图集id删除图集标签
     *
     * @param aid 图集id
     * @return boolean
     */
    @Override
    public boolean removeAtlasTagByAid(Long aid) {
        LambdaQueryWrapper<AtlasLabelEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AtlasLabelEntity::getAtlasId, aid);
        return this.remove(wrapper);
    }
    
}