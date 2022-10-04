package com.elite.tetto.image.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.Query;

import com.elite.tetto.image.dao.ImgsDao;
import com.elite.tetto.image.entity.ImgsEntity;
import com.elite.tetto.image.service.ImgsService;


@Service("imgsService")
public class ImgsServiceImpl extends ServiceImpl<ImgsDao, ImgsEntity> implements ImgsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ImgsEntity> page = this.page(
                new Query<ImgsEntity>().getPage(params),
                new QueryWrapper<ImgsEntity>()
        );

        return new PageUtils(page);
    }

}