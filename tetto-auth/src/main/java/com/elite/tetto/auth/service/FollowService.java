package com.elite.tetto.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.elite.tetto.auth.entity.FollowEntity;
import com.elite.tetto.auth.entity.vo.FollowerRes;
import com.elite.tetto.auth.entity.vo.FollowingRes;
import com.elite.tetto.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-10-04 14:32:08
 */
public interface FollowService extends IService<FollowEntity> {
    
    PageUtils queryPage(Map<String, Object> params);
    
    /**
     * 用户关注被关注者
     *
     * @param uid 用户id
     * @param fid 被关注者id
     * @return boolean
     */
    boolean follow(long uid, long fid);
    
    /**
     * 用户取消关注
     *
     * @param uid uid
     * @param fid 被关注者id
     * @return boolean
     */
    boolean unfollow(long uid, long fid);
    
    /**
     * 获取用户粉丝数量
     *
     * @param uid 用户id
     * @return {@link Integer}
     */
    Integer getFollowersNum(long uid);
    
    /**
     * 获取用户的关注数量
     *
     * @param uid 用户id
     * @return {@link Integer}
     */
    Integer getFollowingNum(long uid);
    
    /**
     * 获取用户是否关注另一个用户
     *
     * @param uid 用户id
     * @param fid 另一个用户id
     * @return boolean
     */
    boolean isFollow(long uid, long fid);
    
    /**
     * 获取用户是否被另一个用户关注
     *
     * @param uid uid
     * @param fid 另一个用户id
     * @return boolean
     */
    boolean isFollowed(long uid, long fid);
    
    /**
     * 获取用户的粉丝列表
     *
     * @param uid 用户id
     * @return {@link List}<{@link FollowEntity}>
     */
    List<FollowerRes> getFollowers(long uid);
    
    /**
     * 获取用户的关注列表
     *
     * @param userId 用户id
     * @return {@link List}<{@link FollowerRes}>
     */
    List<FollowingRes> getFollowings(Long userId);
}

