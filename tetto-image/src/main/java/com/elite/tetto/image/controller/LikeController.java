package com.elite.tetto.image.controller;

import com.elite.tetto.common.exception.ExceptionCode;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.R;
import com.elite.tetto.image.entity.LikeEntity;
import com.elite.tetto.image.service.LikeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-10-11 20:25:39
 */
@RestController
@RequestMapping("image/like")
public class LikeController {
    @Resource
    private LikeService likeService;
    
    
    /**
     * 新增点赞
     *
     * @param aid 图集id
     * @return {@link R}
     */
    @PostMapping("/{aid}")
    public R addLike(@PathVariable("aid") Long aid) {
        boolean b = likeService.addLike(aid);
        return b ? R.ok() :
                R.error(ExceptionCode.UNKNOWN_EXCEPTION.getCode(), ExceptionCode.UNKNOWN_EXCEPTION.getMsg());
    }
    
    /**
     * 取消点赞
     *
     * @param aid 图集id
     * @return {@link R}
     */
    @DeleteMapping("/{aid}")
    public R deleteLike(@PathVariable("aid") Long aid) {
        boolean b = likeService.deleteLike(aid);
        return b ? R.ok() :
                R.error(ExceptionCode.UNKNOWN_EXCEPTION.getCode(), ExceptionCode.UNKNOWN_EXCEPTION.getMsg());
    }
    
    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("image:like:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = likeService.queryPage(params);
        
        return R.ok().put("page", page);
    }
    
    
    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("image:like:info")
    public R info(@PathVariable("id") Long id) {
        LikeEntity like = likeService.getById(id);
        
        return R.ok().put("like", like);
    }
    
    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("image:like:save")
    public R save(@RequestBody LikeEntity like) {
        boolean save = likeService.save(like);
        return save ? R.ok() :
                R.error(ExceptionCode.UNKNOWN_EXCEPTION.getCode(), ExceptionCode.UNKNOWN_EXCEPTION.getMsg());
    }
    
    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("image:like:update")
    public R update(@RequestBody LikeEntity like) {
        likeService.updateById(like);
        
        return R.ok();
    }
    
    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("image:like:delete")
    public R delete(@RequestBody Long[] ids) {
        likeService.removeByIds(Arrays.asList(ids));
        
        return R.ok();
    }
    
}
