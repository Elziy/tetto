package com.elite.tetto.auth.controller;

import java.util.Arrays;
import java.util.Map;

import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elite.tetto.auth.entity.UserEntity;
import com.elite.tetto.auth.service.UserService;

/**
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-09-30 20:52:00
 */
@RestController
@RequestMapping("auth/user")
public class UserController {
    @Autowired
    private UserService userService;
    
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
