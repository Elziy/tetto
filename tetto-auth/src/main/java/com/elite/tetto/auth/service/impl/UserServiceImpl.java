package com.elite.tetto.auth.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elite.tetto.auth.dao.UserDao;
import com.elite.tetto.auth.entity.UserDetailsImpl;
import com.elite.tetto.auth.entity.UserEntity;
import com.elite.tetto.auth.service.UserService;
import com.elite.tetto.auth.util.JwtUtil;
import com.elite.tetto.common.constant.AuthConstant;
import com.elite.tetto.common.entity.vo.LoginUserRes;
import com.elite.tetto.common.entity.vo.LoginUserVo;
import com.elite.tetto.common.entity.vo.ResUserVo;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.Query;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {
    
    
    @Resource(name = "bCryptPasswordEncoder")
    private BCryptPasswordEncoder passwordEncoder;
    
    @Resource
    AuthenticationManager authenticationManager;
    
    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;
    
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
    public LoginUserRes loginByEmail(LoginUserVo loginUserVo) {
        // 将用户信息封装到UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserVo.getEmail(), loginUserVo.getPassword());
        // 执行认证
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // 认证没有通过
        if (Objects.isNull(authenticate)) {
            return null;
        }
        // 认证通过
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticate.getPrincipal();
        UserEntity userEntity = userDetails.getUser();
        String uid = userEntity.getId().toString();
        
        String jwt = JwtUtil.createJWT(uid);
        
        LoginUserRes loginUserRes = new LoginUserRes();
        loginUserRes.setUid(userEntity.getId());
        loginUserRes.setUsername(userEntity.getUsername());
        loginUserRes.setEmail(userEntity.getEmail());
        loginUserRes.setAvatar(userEntity.getHeader());
        loginUserRes.setSex(userEntity.getSex());
        loginUserRes.setIntroduce(userEntity.getIntroduce());
        loginUserRes.setBirthday(userEntity.getBirthday());
        loginUserRes.setToken(jwt);
        
        // 将用户信息存入redis
        redisTemplate.opsForValue().set(AuthConstant.LOGIN_USER_KEY + uid, JSON.toJSONString(loginUserRes));
        
        return loginUserRes;
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
    
    /**
     * 注销登录
     *
     * @return boolean
     */
    @Override
    public boolean logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUserRes loginUserRes = (LoginUserRes) authentication.getPrincipal();
        Long uid = loginUserRes.getUid();
        redisTemplate.delete(AuthConstant.LOGIN_USER_KEY + uid);
        return true;
    }
    
    @Override
    public LoginUserRes getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUserRes loginUserRes = (LoginUserRes) authentication.getPrincipal();
        return loginUserRes;
    }
    
}