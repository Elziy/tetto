package com.elite.tetto.auth.controller;

import com.elite.tetto.auth.entity.UserEntity;
import com.elite.tetto.auth.service.UserService;
import com.elite.tetto.common.entity.vo.LoginUserVo;
import com.elite.tetto.common.entity.vo.ResUserVo;
import com.elite.tetto.common.exception.ExceptionCode;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-09-30 20:52:00
 */
@RestController
@RequestMapping("auth/user")
public class UserController {
    @Resource
    private UserService userService;
    
    /**
     * 用户登录
     *
     * @param loginUserVo 登录用户信息
     * @return {@link R}
     */
    @PostMapping("/login")
    public R login(@RequestBody LoginUserVo loginUserVo) {
        UserEntity user = userService.login(loginUserVo);
        if (user != null) {
            return R.ok().put("data", user);
        } else {
            return R.error(ExceptionCode.LOGIN_PASSWORD_INVALID_EXCEPTION.getCode(), "用户名或密码错误");
        }
    }
    
    @PostMapping("/register")
    public R register(@RequestBody ResUserVo resUserVo) {
        boolean b = userService.register(resUserVo);
        if (b) {
            return R.ok();
        } else {
            return R.error(ExceptionCode.USER_EXIST_EXCEPTION.getCode(), "该邮箱已被注册");
        }
    }
    
    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("auth:user:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = userService.queryPage(params);
        
        return R.ok().put("page", page);
    }
    
    
    /**
     * 信息
     */
    @RequestMapping("/info/{usrId}")
    // @RequiresPermissions("auth:user:info")
    public R info(@PathVariable("usrId") Long usrId) {
        UserEntity user = userService.getById(usrId);
        
        return R.ok().put("user", user);
    }
    
    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("auth:user:save")
    public R save(@RequestBody UserEntity user) {
        userService.save(user);
        
        return R.ok();
    }
    
    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("auth:user:update")
    public R update(@RequestBody UserEntity user) {
        userService.updateById(user);
        
        return R.ok();
    }
    
    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("auth:user:delete")
    public R delete(@RequestBody Long[] usrIds) {
        userService.removeByIds(Arrays.asList(usrIds));
        
        return R.ok();
    }
    
}
