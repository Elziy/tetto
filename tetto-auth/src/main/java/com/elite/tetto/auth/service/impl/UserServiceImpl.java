package com.elite.tetto.auth.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elite.tetto.auth.dao.UserDao;
import com.elite.tetto.auth.entity.UserDetailsImpl;
import com.elite.tetto.auth.entity.UserEntity;
import com.elite.tetto.auth.entity.vo.UserInfoRes;
import com.elite.tetto.auth.service.UserService;
import com.elite.tetto.auth.util.JwtUtil;
import com.elite.tetto.common.constant.AuthConstant;
import com.elite.tetto.common.entity.vo.LoginUserRes;
import com.elite.tetto.common.entity.vo.LoginUserVo;
import com.elite.tetto.common.entity.vo.ResUserVo;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.Query;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {
    public static final String DEFAULT_AVATAR = "http://static.tetto.com/default.jpg";
    
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
    @CacheEvict(value = AuthConstant.USER_INFO_KEY, key = "#result") // 清除解决缓存穿透的缓存
    @Transactional
    public Long register(ResUserVo resUserVo) {
        String email = resUserVo.getEmail();
        String password = resUserVo.getPassword();
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserEntity::getEmail, email);
        UserEntity userEntity = this.getOne(wrapper);
        if (userEntity != null) {
            return 0L;
        } else {
            UserEntity entity = new UserEntity();
            entity.setSex("未知");
            entity.setHeader(DEFAULT_AVATAR);
            entity.setUsername(email.substring(0, email.indexOf("@")));
            entity.setEmail(email);
            entity.setPassword(passwordEncoder.encode(password));
            boolean save = this.save(entity);
            return save ? entity.getId() : 0L;
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
    
    /**
     * 获取登录用户的信息
     *
     * @return {@link LoginUserRes}
     */
    @Override
    public LoginUserRes getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUserRes loginUserRes = (LoginUserRes) authentication.getPrincipal();
        LoginUserRes userRes = new LoginUserRes();
        // 为了数据一致性，重新从数据库中查询
        UserEntity userEntity = getEntityByUid(loginUserRes.getUid());
        userRes.setUid(loginUserRes.getUid());
        userRes.setUsername(userEntity.getUsername());
        userRes.setEmail(userEntity.getUsername());
        userRes.setAvatar(userEntity.getHeader());
        userRes.setSex(userEntity.getSex());
        userRes.setIntroduce(userEntity.getIntroduce());
        userRes.setBirthday(userEntity.getBirthday());
        userRes.setToken(loginUserRes.getToken());
        
        return userRes;
    }
    
    /**
     * 通过uid获取用户信息<br>
     * 根据登录用户的是否是自己返回不同的信息
     *
     * @param uid uid
     * @return {@link UserInfoRes}
     */
    @Override
    @Cacheable(value = AuthConstant.USER_INFO_KEY, key = "#uid") // 设置缓存
    public UserInfoRes getUserInfoByUid(Long uid) {
        UserEntity userEntity = this.getEntityByUid(uid);
        if (userEntity == null) {
            return null;
        }
        UserInfoRes userInfoRes = new UserInfoRes();
        setBaseInfo(userInfoRes, userEntity);
        // todo 设置可以访问的其他信息
        
        // 获取登录的用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        
        if (Objects.nonNull(principal) && principal instanceof LoginUserRes) {
            // 访问的是自己的信息
            // todo 设置自己的信息
            return userInfoRes;
        } else {
            // 访问的是别人的信息
            return userInfoRes;
        }
    }
    
    /**
     * 更新用户信息
     *
     * @param user 更新后的用户信息
     */
    @Override
    @CacheEvict(value = AuthConstant.USER_INFO_KEY, key = "#user.id") // 清除缓存
    public void updateUserInfo(UserEntity user) {
        Long id = user.getId();
        if (id == null) {
            throw new RuntimeException("用户id不能为空");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUserRes loginUserRes = (LoginUserRes) authentication.getPrincipal();
        Long uid = loginUserRes.getUid();
        if (!id.equals(uid)) {
            throw new RuntimeException("无权限修改");
        }
        this.updateById(user);
    }
    
    /**
     * 设置基本信息(不需要权限的信息)
     *
     * @param userEntity 用户实体
     */
    private void setBaseInfo(UserInfoRes userInfoRes, UserEntity userEntity) {
        userInfoRes.setUid(userEntity.getId());
        userInfoRes.setUsername(userEntity.getUsername());
        userInfoRes.setEmail(userEntity.getEmail());
        userInfoRes.setAvatar(userEntity.getHeader());
        userInfoRes.setSex(userEntity.getSex());
        userInfoRes.setIntroduce(userEntity.getIntroduce());
        userInfoRes.setBirthday(userEntity.getBirthday());
    }
    
    /**
     * 获取用户基本信息通过uid
     *
     * @param uid uid
     * @return {@link UserEntity}
     */
    public UserEntity getEntityByUid(Long uid) {
        UserEntity userEntity = this.getById(uid);
        return userEntity;
    }
    
}