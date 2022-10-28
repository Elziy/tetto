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
import com.elite.tetto.auth.service.EmailService;
import com.elite.tetto.auth.service.FollowService;
import com.elite.tetto.auth.service.UserService;
import com.elite.tetto.auth.util.JwtUtil;
import com.elite.tetto.auth.util.NumberUtils;
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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {
    public static final String DEFAULT_AVATAR = "http://static.tetto.com/default.jpg";
    
    @Resource(name = "bCryptPasswordEncoder")
    private BCryptPasswordEncoder passwordEncoder;
    
    @Resource
    AuthenticationManager authenticationManager;
    
    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;
    
    @Resource
    private FollowService followService;
    
    @Resource
    private EmailService emailService;
    
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserEntity> page = this.page(
                new Query<UserEntity>().getPage(params),
                new QueryWrapper<>()
        );
        
        return new PageUtils(page);
    }
    
    @Override
    public LoginUserRes loginByEmail(LoginUserVo loginUserVo) {
        // 将用户信息封装到UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserVo.getEmail(), loginUserVo.getPassword());
        // 执行认证
        Authentication authenticate;
        try {
            authenticate = authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            return null;
        }
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
        String code = resUserVo.getCode();
        // 验证码校验
        String redisCode = redisTemplate.opsForValue().get(AuthConstant.VERIFICATION_CODE_KEY + email);
        if (Objects.isNull(redisCode) || !redisCode.split("_")[0].equals(code)) {
            throw new RuntimeException("验证码错误");
        }
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
     * 发送邮箱验证码
     *
     * @param email 电子邮件
     * @return boolean
     */
    @Override
    public boolean sendCode(String email) {
        String code = NumberUtils.getCode();
        String redisCode = redisTemplate.opsForValue().get(AuthConstant.VERIFICATION_CODE_KEY + email);
        if (redisCode != null) {
            long l = Long.parseLong(redisCode.split("_")[1]);
            if (System.currentTimeMillis() - l < 1000 * 60) {
                throw new RuntimeException("频繁发送验证码");
            }
            code = redisCode.split("_")[0];
        }
        boolean mail;
        try {
            mail = emailService.sendMail(email, code,
                    AuthConstant.REGISTER,
                    AuthConstant.REGISTER_SUBJECT);
        } catch (Exception e) {
            throw new RuntimeException("请检查邮箱是否正确");
        }
        if (mail) {
            redisTemplate.opsForValue().set(AuthConstant.VERIFICATION_CODE_KEY + email,
                    code + "_" + System.currentTimeMillis(),
                    5, TimeUnit.MINUTES);
        }
        return mail;
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
    // 只缓存空值，解决缓存穿透
    @Cacheable(value = AuthConstant.USER_INFO_KEY, key = "#uid", unless = "#result != null ") // 设置缓存
    public UserInfoRes getUserInfoByUid(Long uid) {
        UserEntity userEntity = this.getEntityByUid(uid);
        if (userEntity == null) {
            return null;
        }
        UserInfoRes userInfoRes = new UserInfoRes();
        setBaseInfo(userInfoRes, userEntity);
        // todo 设置可以访问的其他信息
        // 设置请求用户的关注信息
        Integer followers = followService.getFollowersNum(uid);
        Integer following = followService.getFollowingNum(uid);
        userInfoRes.setFollowers(followers);
        userInfoRes.setFollowing(following);
        
        // 获取登录的用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUserRes loginUserRes = (LoginUserRes) authentication.getPrincipal();
        Long loginUid = loginUserRes.getUid();
        
        if (loginUid.equals(uid)) {
            // 访问的是自己的信息
            // todo 设置自己的信息
            return userInfoRes;
        } else {
            // 访问的是别人的信息
            // 查出登录用户与该用户的关注关系
            boolean follow = followService.isFollow(loginUid, uid);
            boolean followed = followService.isFollowed(loginUid, uid);
            userInfoRes.setIsFollow(follow);
            userInfoRes.setIsFollowed(followed);
            return userInfoRes;
        }
    }
    
    /**
     * 更新用户信息
     *
     * @param user 更新后的用户信息
     */
    @Override
    // @CacheEvict(value = AuthConstant.USER_INFO_KEY, key = "#user.id") // 清除缓存
    public void updateUserInfo(UserEntity user) {
        Long id = user.getId();
        if (id == null) {
            throw new RuntimeException("用户id不能为空");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUserRes loginUserRes = (LoginUserRes) authentication.getPrincipal();
        Long uid = loginUserRes.getUid();
        // 修改的不是自己的信息或者邮箱已经被占用
        if (Objects.nonNull(user.getEmail()) && (!id.equals(uid) || !checkEmail(user.getEmail()))) {
            throw new RuntimeException("无权限修改");
        }
        boolean b = this.updateById(user);
        if (!b) {
            throw new RuntimeException("更新失败");
        } else {
            // 如果修改了邮箱，需要注销登录
            if (Objects.nonNull(user.getEmail())) {
                logout();
            }
        }
    }
    
    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱
     * @return boolean true 可用 false 不可用
     */
    @Override
    public boolean checkEmail(String email) {
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserEntity::getEmail, email);
        UserEntity userEntity = this.getOne(wrapper);
        if (userEntity == null) {
            return true;
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();
            // 如果是登录状态，且登录的用户的邮箱和传入的邮箱一致，则返回true
            if (Objects.nonNull(principal) && principal instanceof LoginUserRes) {
                LoginUserRes loginUserRes = (LoginUserRes) principal;
                return loginUserRes.getEmail().equals(email);
            } else {
                // 如果不是登录状态，返回false
                return false;
            }
        }
    }
    
    /**
     * 关注用户
     *
     * @param fid 被关注的用户id
     * @return boolean
     */
    @Override
    public boolean follow(long fid) {
        // 得到登录用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUserRes loginUserRes = (LoginUserRes) authentication.getPrincipal();
        Long uid = loginUserRes.getUid();
        // 关注自己
        if (uid.equals(fid)) {
            return false;
        }
        return followService.follow(uid, fid);
    }
    
    /**
     * 取消关注
     *
     * @param fid 被取消关注的用户id
     * @return boolean
     */
    @Override
    public boolean unfollow(long fid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUserRes loginUserRes = (LoginUserRes) authentication.getPrincipal();
        Long uid = loginUserRes.getUid();
        return followService.unfollow(uid, fid);
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