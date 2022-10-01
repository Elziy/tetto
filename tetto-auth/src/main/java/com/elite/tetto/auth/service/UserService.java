package com.elite.tetto.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.elite.tetto.auth.entity.UserEntity;
import com.elite.tetto.common.entity.vo.LoginUserVo;
import com.elite.tetto.common.utils.PageUtils;

import java.util.Map;

/**
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-09-30 20:52:00
 */
public interface UserService extends IService<UserEntity> {
    
    PageUtils queryPage(Map<String, Object> params);
    
    /**
     * 用户登录
     *
     * @param loginUserVo 登录用户信息
     * @return {@link UserEntity}
     */
    UserEntity login(LoginUserVo loginUserVo);
}

