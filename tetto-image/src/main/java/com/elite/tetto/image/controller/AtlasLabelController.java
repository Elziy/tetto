package com.elite.tetto.image.controller;

import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.R;
import com.elite.tetto.image.entity.AtlasLabelEntity;
import com.elite.tetto.image.service.AtlasLabelService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
@RequestMapping("image/tags")
public class AtlasLabelController {
    @Resource
    private AtlasLabelService atlasLabelService;
    
    // 获取热门标签
    @GetMapping("/hotTags")
    public R getHotTags() {
        return R.ok().put("data", atlasLabelService.getHotTags());
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("image:atlaslabel:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = atlasLabelService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("image:atlaslabel:info")
    public R info(@PathVariable("id") Long id){
		AtlasLabelEntity atlasLabel = atlasLabelService.getById(id);

        return R.ok().put("atlasLabel", atlasLabel);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("image:atlaslabel:save")
    public R save(@RequestBody AtlasLabelEntity atlasLabel){
		atlasLabelService.save(atlasLabel);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("image:atlaslabel:update")
    public R update(@RequestBody AtlasLabelEntity atlasLabel){
		atlasLabelService.updateById(atlasLabel);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("image:atlaslabel:delete")
    public R delete(@RequestBody Long[] ids){
		atlasLabelService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
