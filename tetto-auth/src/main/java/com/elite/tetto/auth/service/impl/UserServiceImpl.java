package com.elite.tetto.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elite.tetto.auth.dao.UserDao;
import com.elite.tetto.auth.entity.UserEntity;
import com.elite.tetto.auth.service.UserService;
import com.elite.tetto.common.entity.vo.LoginUserVo;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {
    
    @Resource(name = "bCryptPasswordEncoder")
    private BCryptPasswordEncoder passwordEncoder;
    
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserEntity> page = this.page(
                new Query<UserEntity>().getPage(params),
                new QueryWrapper<UserEntity>()
        );
        
        return new PageUtils(page);
    }
    
    @Override
    public UserEntity login(LoginUserVo loginUserVo) {
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserEntity::getUsrUsername, loginUserVo.getUsername())
                .or()
                .eq(UserEntity::getUsrEmail, loginUserVo.getUsername());
        
        UserEntity userEntity = this.getOne(wrapper);
        if (userEntity == null) {
            return null;
        } else {
            String password = loginUserVo.getPassword();
            String usrPassword = userEntity.getUsrPassword();
            boolean matches = passwordEncoder.matches(password, usrPassword);
            return matches ? userEntity : null;
    
        }
    }
    
}