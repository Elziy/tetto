package com.elite.tetto.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elite.tetto.auth.dao.FollowDao;
import com.elite.tetto.auth.entity.FollowEntity;
import com.elite.tetto.auth.service.FollowService;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.Query;
import org.springframework.stereotype.Service;

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
        FollowEntity follow=new FollowEntity();
        follow.setFid(fid);
        follow.setUid(uid);
        return this.save(follow);
    }
    
    /**
     * 获取用户关注数量
     *
     * @param uid 用户id
     * @return {@link Integer}
     */
    @Override
    public Integer getFollowers(long uid) {
        return null;
    }
    
    /**
     * 获取用户的粉丝数量
     *
     * @param uid 用户id
     * @return {@link Integer}
     */
    @Override
    public Integer getFollowing(long uid) {
        return null;
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
        return false;
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
        return false;
    }
    
}