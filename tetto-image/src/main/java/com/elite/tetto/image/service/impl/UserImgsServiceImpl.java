package com.elite.tetto.image.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.Query;

import com.elite.tetto.image.dao.UserImgsDao;
import com.elite.tetto.image.entity.UserImgsEntity;
import com.elite.tetto.image.service.UserImgsService;


@Service("userImgsService")
public class UserImgsServiceImpl extends ServiceImpl<UserImgsDao, UserImgsEntity> implements UserImgsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserImgsEntity> page = this.page(
                new Query<UserImgsEntity>().getPage(params),
                new QueryWrapper<UserImgsEntity>()
        );

        return new PageUtils(page);
    }

}