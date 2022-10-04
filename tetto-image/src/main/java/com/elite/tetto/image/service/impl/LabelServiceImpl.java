package com.elite.tetto.image.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.Query;

import com.elite.tetto.image.dao.LabelDao;
import com.elite.tetto.image.entity.LabelEntity;
import com.elite.tetto.image.service.LabelService;


@Service("labelService")
public class LabelServiceImpl extends ServiceImpl<LabelDao, LabelEntity> implements LabelService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<LabelEntity> page = this.page(
                new Query<LabelEntity>().getPage(params),
                new QueryWrapper<LabelEntity>()
        );

        return new PageUtils(page);
    }

}