package com.elite.tetto.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elite.tetto.auth.dao.FollowDao;
import com.elite.tetto.auth.entity.FollowEntity;
import com.elite.tetto.auth.entity.vo.FollowerRes;
import com.elite.tetto.auth.entity.vo.FollowingRes;
import com.elite.tetto.auth.service.FollowService;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("followService")
public class FollowServiceImpl extends ServiceImpl<FollowDao, FollowEntity> implements FollowService {
    
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<FollowEntity> page = this.page(
                new Query<FollowEntity>().getPage(params),
                new QueryWrapper<FollowEntity>()
        );
        
        return new PageUtils(page);
    }
    
    @Override
    public boolean follow(long uid, long fid) {
        FollowEntity follow = new FollowEntity();
        follow.setFid(fid);
        follow.setUid(uid);
        return this.save(follow);
    }
    
    /**
     * 用户取消关注
     *
     * @param uid 用户id
     * @param fid 被关注者id
     * @return boolean
     */
    @Override
    public boolean unfollow(long uid, long fid) {
        LambdaQueryWrapper<FollowEntity> wrapper = new QueryWrapper<FollowEntity>().lambda();
        wrapper.eq(FollowEntity::getUid, uid);
        wrapper.eq(FollowEntity::getFid, fid);
        return this.remove(wrapper);
    }
    
    /**
     * 获取用户粉丝数量
     *
     * @param uid 用户id
     * @return {@link Integer}
     */
    @Override
    public Integer getFollowersNum(long uid) {
        LambdaQueryWrapper<FollowEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FollowEntity::getFid, uid);
        return Math.toIntExact(this.count(wrapper));
    }
    
    /**
     * 获取用户的关注数量
     *
     * @param uid 用户id
     * @return {@link Integer}
     */
    @Override
    public Integer getFollowingNum(long uid) {
        LambdaQueryWrapper<FollowEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FollowEntity::getUid, uid);
        return Math.toIntExact(this.count(wrapper));
    }
    
    /**
     * 获取用户是否关注另一个用户
     *
     * @param uid 用户id
     * @param fid 另一个用户id
     * @return boolean
     */
    @Override
    public boolean isFollow(long uid, long fid) {
        LambdaQueryWrapper<FollowEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FollowEntity::getUid, uid);
        wrapper.eq(FollowEntity::getFid, fid);
        return this.count(wrapper) > 0;
    }
    
    /**
     * 获取用户是否被另一个用户关注
     *
     * @param uid uid
     * @param fid 另一个用户id
     * @return boolean
     */
    @Override
    public boolean isFollowed(long uid, long fid) {
        LambdaQueryWrapper<FollowEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FollowEntity::getUid, fid);
        wrapper.eq(FollowEntity::getFid, uid);
        return this.count(wrapper) > 0;
    }
    
    /**
     * 获取用户的粉丝列表
     *
     * @param uid 用户id
     * @return {@link List}<{@link FollowEntity}>
     */
    @Override
    public List<FollowerRes> getFollowers(long uid) {
        List<FollowerRes> followers = this.baseMapper.getFollowers(uid);
        for (FollowerRes follower : followers) {
            follower.setFollowing(this.isFollow(uid, follower.getUid()));
        }
        return followers;
    }
    
    /**
     * 获取用户的关注列表
     *
     * @param userId 用户id
     * @return {@link List}<{@link FollowerRes}>
     */
    @Override
    public List<FollowingRes> getFollowings(Long userId) {
        List<FollowingRes> followings = this.baseMapper.getFollowings(userId);
        for (FollowingRes following : followings) {
            following.setFollowed(this.isFollowed(userId, following.getUid()));
        }
        return followings;
    }
    
}