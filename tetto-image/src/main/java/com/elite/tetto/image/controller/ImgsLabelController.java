package com.elite.tetto.image.controller;

import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.R;
import com.elite.tetto.image.entity.ImgsLabelEntity;
import com.elite.tetto.image.service.ImgsLabelService;
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
@RequestMapping("image/imgslabel")
public class ImgsLabelController {
    @Autowired
    private ImgsLabelService imgsLabelService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("image:imgslabel:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = imgsLabelService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("image:imgslabel:info")
    public R info(@PathVariable("id") Long id){
		ImgsLabelEntity imgsLabel = imgsLabelService.getById(id);

        return R.ok().put("imgsLabel", imgsLabel);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("image:imgslabel:save")
    public R save(@RequestBody ImgsLabelEntity imgsLabel){
		imgsLabelService.save(imgsLabel);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("image:imgslabel:update")
    public R update(@RequestBody ImgsLabelEntity imgsLabel){
		imgsLabelService.updateById(imgsLabel);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("image:imgslabel:delete")
    public R delete(@RequestBody Long[] ids){
		imgsLabelService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
