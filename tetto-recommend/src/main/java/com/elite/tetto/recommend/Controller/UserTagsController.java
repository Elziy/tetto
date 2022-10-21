package com.elite.tetto.recommend.Controller;

import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.R;
import com.elite.tetto.recommend.entity.UserTagsEntity;
import com.elite.tetto.recommend.service.UserTagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-10-21 19:57:54
 */
@RestController
@RequestMapping("recommend/usertags")
public class UserTagsController {
    @Autowired
    private UserTagsService userTagsService;
    
    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("recommend:usertags:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = userTagsService.queryPage(params);
        
        return R.ok().put("page", page);
    }
    
    
    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("recommend:usertags:info")
    public R info(@PathVariable("id") Long id) {
        UserTagsEntity userTags = userTagsService.getById(id);
        
        return R.ok().put("userTags", userTags);
    }
    
    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("recommend:usertags:save")
    public R save(@RequestBody UserTagsEntity userTags) {
        userTagsService.save(userTags);
        
        return R.ok();
    }
    
    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("recommend:usertags:update")
    public R update(@RequestBody UserTagsEntity userTags) {
        userTagsService.updateById(userTags);
        
        return R.ok();
    }
    
    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("recommend:usertags:delete")
    public R delete(@RequestBody Long[] ids) {
        userTagsService.removeByIds(Arrays.asList(ids));
        
        return R.ok();
    }
    
}
