package com.elite.tetto.recommend.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.Query;

import com.elite.tetto.recommend.dao.UserTagsDao;
import com.elite.tetto.recommend.entity.UserTagsEntity;
import com.elite.tetto.recommend.service.UserTagsService;


@Service("userTagsService")
public class UserTagsServiceImpl extends ServiceImpl<UserTagsDao, UserTagsEntity> implements UserTagsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserTagsEntity> page = this.page(
                new Query<UserTagsEntity>().getPage(params),
                new QueryWrapper<UserTagsEntity>()
        );

        return new PageUtils(page);
    }

}