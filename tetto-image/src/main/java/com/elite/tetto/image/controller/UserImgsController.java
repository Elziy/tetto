package com.elite.tetto.image.controller;

import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.R;
import com.elite.tetto.image.entity.UserImgsEntity;
import com.elite.tetto.image.service.UserImgsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * 
 *
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-10-04 17:23:12
 */
@RestController
@RequestMapping("image/userimgs")
public class UserImgsController {
    @Autowired
    private UserImgsService userImgsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("image:userimgs:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = userImgsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("image:userimgs:info")
    public R info(@PathVariable("id") Long id){
		UserImgsEntity userImgs = userImgsService.getById(id);

        return R.ok().put("userImgs", userImgs);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("image:userimgs:save")
    public R save(@RequestBody UserImgsEntity userImgs){
		userImgsService.save(userImgs);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("image:userimgs:update")
    public R update(@RequestBody UserImgsEntity userImgs){
		userImgsService.updateById(userImgs);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("image:userimgs:delete")
    public R delete(@RequestBody Long[] ids){
		userImgsService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
