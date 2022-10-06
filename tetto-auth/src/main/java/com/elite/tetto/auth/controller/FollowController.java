package com.elite.tetto.auth.controller;

import java.util.Arrays;
import java.util.Map;

import com.elite.tetto.common.exception.ExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elite.tetto.auth.entity.FollowEntity;
import com.elite.tetto.auth.service.FollowService;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.R;



/**
 *
 *
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-10-04 14:32:08
 */
@RestController
@RequestMapping("auth/follow")
public class FollowController {
    @Autowired
    private FollowService followService;

    /**
     * 关注用户
     *
     * @param uid 关注者ID
     * @param fid 被关注者ID
     * @return {@link R}
     */
    public R follow(@RequestBody long uid,long fid){
        boolean b=followService.follow(uid,fid);
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
    // @RequiresPermissions("auth:follow:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = followService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("auth:follow:info")
    public R info(@PathVariable("id") Long id){
		FollowEntity follow = followService.getById(id);

        return R.ok().put("follow", follow);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("auth:follow:save")
    public R save(@RequestBody FollowEntity follow){
		followService.save(follow);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("auth:follow:update")
    public R update(@RequestBody FollowEntity follow){
		followService.updateById(follow);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("auth:follow:delete")
    public R delete(@RequestBody Long[] ids){
		followService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
