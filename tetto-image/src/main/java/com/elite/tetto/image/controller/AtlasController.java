package com.elite.tetto.image.controller;

import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.R;
import com.elite.tetto.image.entity.AtlasEntity;
import com.elite.tetto.image.service.AtlasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-10-04 17:23:12
 */
@RestController
@RequestMapping("image/atlas")
public class AtlasController {
    @Autowired
    private AtlasService atlasService;
    
    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("image:atlas:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = atlasService.queryPage(params);
        
        return R.ok().put("page", page);
    }
    
    
    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("image:atlas:info")
    public R info(@PathVariable("id") Long id) {
        AtlasEntity atlas = atlasService.getById(id);
        
        return R.ok().put("atlas", atlas);
    }
    
    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("image:atlas:save")
    public R save(@RequestBody AtlasEntity atlas) {
        atlasService.save(atlas);
        
        return R.ok();
    }
    
    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("image:atlas:update")
    public R update(@RequestBody AtlasEntity atlas) {
        atlasService.updateById(atlas);
        
        return R.ok();
    }
    
    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("image:atlas:delete")
    public R delete(@RequestBody Long[] ids) {
        atlasService.removeByIds(Arrays.asList(ids));
        
        return R.ok();
    }
    
}
