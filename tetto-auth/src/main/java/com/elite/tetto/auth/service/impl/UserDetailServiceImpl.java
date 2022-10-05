package com.elite.tetto.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.elite.tetto.auth.entity.UserDetailsImpl;
import com.elite.tetto.auth.entity.UserEntity;
import com.elite.tetto.auth.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 自定义的密码校验
 *
 * @author Elziy
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    
    @Resource
    private UserService userService;
    
    /**
     * 得到数据库中登录用户的信息
     *
     * @param email 邮箱
     * @return {@link UserDetails}
     * @throws UsernameNotFoundException 用户名没有发现异常
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserEntity::getEmail, email);
        UserEntity user = userService.getOne(wrapper);
        if (user == null) {
            throw new RuntimeException("邮箱或密码错误");
        }
    
        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setUser(user);
        return userDetails;
    }
}
