package com.elite.tetto.image.controller;

import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.R;
import com.elite.tetto.image.entity.ImgsEntity;
import com.elite.tetto.image.service.ImgsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * 
 *
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-10-07 14:39:01
 */
@RestController
@RequestMapping("image/imgs")
public class ImgsController {
    @Autowired
    private ImgsService imgsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("image:imgs:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = imgsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("image:imgs:info")
    public R info(@PathVariable("id") Long id){
		ImgsEntity imgs = imgsService.getById(id);

        return R.ok().put("imgs", imgs);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("image:imgs:save")
    public R save(@RequestBody ImgsEntity imgs){
		imgsService.save(imgs);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("image:imgs:update")
    public R update(@RequestBody ImgsEntity imgs){
		imgsService.updateById(imgs);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("image:imgs:delete")
    public R delete(@RequestBody Long[] ids){
		imgsService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
