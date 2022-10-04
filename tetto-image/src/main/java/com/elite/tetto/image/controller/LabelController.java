package com.elite.tetto.image.controller;

import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.R;
import com.elite.tetto.image.entity.LabelEntity;
import com.elite.tetto.image.service.LabelService;
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
@RequestMapping("image/label")
public class LabelController {
    @Autowired
    private LabelService labelService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("image:label:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = labelService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("image:label:info")
    public R info(@PathVariable("id") Long id){
		LabelEntity label = labelService.getById(id);

        return R.ok().put("label", label);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("image:label:save")
    public R save(@RequestBody LabelEntity label){
		labelService.save(label);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("image:label:update")
    public R update(@RequestBody LabelEntity label){
		labelService.updateById(label);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("image:label:delete")
    public R delete(@RequestBody Long[] ids){
		labelService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
