package com.elite.tetto.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elite.tetto.auth.dao.UserDao;
import com.elite.tetto.auth.entity.UserEntity;
import com.elite.tetto.auth.service.UserService;
import com.elite.tetto.common.entity.vo.LoginUserRes;
import com.elite.tetto.common.entity.vo.LoginUserVo;
import com.elite.tetto.common.entity.vo.ResUserVo;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.Query;
import org.springframework.beans.factory.annotation.Value;
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
                new QueryWrapper<>()
        );
        
        return new PageUtils(page);
    }
    
    @Override
    public LoginUserRes login(LoginUserVo loginUserVo) {
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserEntity::getEmail, loginUserVo.getEmail());
        UserEntity userEntity = this.getOne(wrapper);
        
        if (userEntity == null) {
            return null;
        } else {
            String password = loginUserVo.getPassword();
            String usrPassword = userEntity.getPassword();
            boolean matches = passwordEncoder.matches(password, usrPassword);
            if (matches) {
                LoginUserRes loginUserRes = new LoginUserRes();
                loginUserRes.setUid(userEntity.getId());
                loginUserRes.setUsername(userEntity.getUsername());
                loginUserRes.setEmail(userEntity.getEmail());
                loginUserRes.setAvatar(userEntity.getHeader());
                loginUserRes.setSex(userEntity.getSex());
                loginUserRes.setIntroduce(userEntity.getIntroduce());
                loginUserRes.setBirthday(userEntity.getBirthday());
                return loginUserRes;
            } else {
                return null;
            }
        }
    }
    
    @Override
    public boolean register(ResUserVo resUserVo) {
        String email = resUserVo.getEmail();
        String password = resUserVo.getPassword();
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserEntity::getEmail, email);
        UserEntity userEntity = this.getOne(wrapper);
        if (userEntity != null) {
            return false;
        } else {
            UserEntity entity = new UserEntity();
            entity.setSex("未知");
            entity.setUsername(email.substring(0, email.indexOf("@")));
            entity.setEmail(email);
            entity.setPassword(passwordEncoder.encode(password));
            return this.save(entity);
        }
    }
    
}