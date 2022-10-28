package com.elite.tetto.auth.controller;

import com.elite.tetto.auth.entity.vo.FollowerRes;
import com.elite.tetto.auth.entity.vo.FollowingRes;
import com.elite.tetto.auth.service.FollowService;
import com.elite.tetto.auth.service.UserService;
import com.elite.tetto.auth.util.SecurityUtil;
import com.elite.tetto.common.exception.ExceptionCode;
import com.elite.tetto.common.utils.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-10-04 14:32:08
 */
@RestController
@RequestMapping("auth/user")
public class FollowController {
    
    @Resource
    private UserService userService;
    
    @Resource
    private FollowService followService;
    
    /**
     * 关注用户
     *
     * @param fid 被关注者ID
     * @return {@link R}
     */
    @GetMapping("follow/{fid}")
    public R follow(@PathVariable long fid) {
        boolean b = userService.follow(fid);
        if (b) {
            return R.ok();
        } else {
            return R.error(ExceptionCode.UNKNOWN_EXCEPTION.getCode(), ExceptionCode.UNKNOWN_EXCEPTION.getMsg());
        }
    }
    
    /**
     * 取消关注
     *
     * @param fid 被关注者ID
     * @return {@link R}
     */
    @GetMapping("unfollow/{fid}")
    public R unfollow(@PathVariable long fid) {
        boolean b = userService.unfollow(fid);
        if (b) {
            return R.ok();
        } else {
            return R.error(ExceptionCode.UNKNOWN_EXCEPTION.getCode(), ExceptionCode.UNKNOWN_EXCEPTION.getMsg());
        }
    }
    
    /**
     * 获取用户粉丝列表
     *
     * @return {@link R}
     */
    @GetMapping("followers")
    public R getFollowers() {
        Long userId = SecurityUtil.getLoginUserId();
        List<FollowerRes> followers = followService.getFollowers(userId);
        return R.ok().put("data", followers);
    }
    
    /**
     * 获取用户关注列表
     *
     * @return {@link R}
     */
    @GetMapping("followings")
    public R getFollowing() {
        Long userId = SecurityUtil.getLoginUserId();
        List<FollowingRes> following = followService.getFollowings(userId);
        return R.ok().put("data", following);
    }
}
