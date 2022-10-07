package com.elite.tetto.image.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.Query;

import com.elite.tetto.image.dao.AtlasLabelDao;
import com.elite.tetto.image.entity.AtlasLabelEntity;
import com.elite.tetto.image.service.AtlasLabelService;


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

}