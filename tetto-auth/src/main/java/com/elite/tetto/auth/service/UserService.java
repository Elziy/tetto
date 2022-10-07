package com.elite.tetto.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.elite.tetto.auth.entity.UserEntity;
import com.elite.tetto.auth.entity.vo.UserInfoRes;
import com.elite.tetto.common.entity.vo.LoginUserRes;
import com.elite.tetto.common.entity.vo.LoginUserVo;
import com.elite.tetto.common.entity.vo.ResUserVo;
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
    LoginUserRes login(LoginUserVo loginUserVo);
    
    /**
     * 优化用户登录
     *
     * @param loginUserVo 登录用户信息
     * @return {@link LoginUserRes}
     */
    LoginUserRes loginByEmail(LoginUserVo loginUserVo);
    
    /**
     * 用户注册
     *
     * @param resUserVo 用户注册信息
     * @return boolean true:注册成功 false:注册失败
     */
    Long register(ResUserVo resUserVo);
    
    /**
     * 注销登录
     *
     * @return
     */
    boolean logout();
    
    /**
     * 获取登录用户
     *
     * @return {@link LoginUserRes}
     */
    LoginUserRes getLoginUser();
    
    /**
     * 通过uid获取用户信息<br>
     * 根据登录用户的是否是自己返回不同的信息
     *
     * @param uid uid
     * @return {@link UserInfoRes}
     */
    UserInfoRes getUserInfoByUid(Long uid);
    
    /**
     * 更新用户信息
     *
     * @param user 更新后用户信息
     */
    void updateUserInfo(UserEntity user);
    
    /**
     * 检查邮箱是否存在
     *
     * @param email 电子邮件
     * @return boolean
     */
    boolean checkEmail(String email);
}

