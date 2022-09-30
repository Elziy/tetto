package com.elite.tetto.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elite.tetto.auth.dao.UserDao;
import com.elite.tetto.auth.entity.UserEntity;
import com.elite.tetto.auth.service.UserService;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {
    
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserEntity> page = this.page(
                new Query<UserEntity>().getPage(params),
                new QueryWrapper<UserEntity>()
        );
        
        return new PageUtils(page);
    }
    
}