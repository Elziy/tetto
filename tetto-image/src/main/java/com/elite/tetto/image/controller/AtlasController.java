package com.elite.tetto.image.controller;

import com.elite.tetto.common.exception.ExceptionCode;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.R;
import com.elite.tetto.image.entity.AtlasEntity;
import com.elite.tetto.image.entity.vo.UploadAtlasVo;
import com.elite.tetto.image.service.AtlasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-10-07 14:39:01
 */
@RestController
@RequestMapping("image/atlas")
public class AtlasController {
    @Autowired
    private AtlasService atlasService;
    
    // 上传作品集
    @PostMapping("/upload")
    public R upload(@RequestBody UploadAtlasVo vo) {
        boolean b = atlasService.upload(vo);
        if (b) {
            return R.ok();
        } else {
            return R.error(ExceptionCode.UNKNOWN_EXCEPTION.getCode(), ExceptionCode.UNKNOWN_EXCEPTION.getMsg());
        }
    }
    
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
