package com.elite.tetto.image.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.Query;

import com.elite.tetto.image.dao.ImgsLabelDao;
import com.elite.tetto.image.entity.ImgsLabelEntity;
import com.elite.tetto.image.service.ImgsLabelService;


@Service("imgsLabelService")
public class ImgsLabelServiceImpl extends ServiceImpl<ImgsLabelDao, ImgsLabelEntity> implements ImgsLabelService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ImgsLabelEntity> page = this.page(
                new Query<ImgsLabelEntity>().getPage(params),
                new QueryWrapper<ImgsLabelEntity>()
        );

        return new PageUtils(page);
    }

}