package com.elite.tetto.image.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.Query;
import com.elite.tetto.image.dao.LikeDao;
import com.elite.tetto.image.entity.LikeEntity;
import com.elite.tetto.image.service.LikeService;
import com.elite.tetto.image.util.SecurityUtil;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;


@Service("likeService")
public class LikeServiceImpl extends ServiceImpl<LikeDao, LikeEntity> implements LikeService {
    
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<LikeEntity> page = this.page(
                new Query<LikeEntity>().getPage(params),
                new QueryWrapper<>()
        );
        
        return new PageUtils(page);
    }
    
    /**
     * 用户是否点赞
     *
     * @param uid 用户id
     * @param aid 图集id
     * @return boolean
     */
    @Override
    public boolean isLike(Long uid, Long aid) {
        LambdaQueryWrapper<LikeEntity> wrapper = new QueryWrapper<LikeEntity>().lambda();
        wrapper.eq(LikeEntity::getUid, uid);
        wrapper.eq(LikeEntity::getAid, aid);
        LikeEntity one = this.getOne(wrapper);
        return one != null;
    }
    
    /**
     * 添加点赞
     *
     * @param aid 图集id
     * @return boolean
     */
    @Override
    public boolean addLike(Long aid) {
        // 1. 获取登录用户的id
        Long loginUserId = SecurityUtil.getLoginUserId();
        // 2. 查询是否已经点赞
        boolean isLike = isLike(loginUserId, aid);
        if (isLike) {
            return false;
        } else {
            // 3. 保存点赞
            LikeEntity like = new LikeEntity();
            like.setAid(aid);
            like.setUid(loginUserId);
            Date date = new Date();
            System.out.println(date);
            like.setDate(date);
            return this.save(like);
        }
    }
    
    /**
     * 取消点赞
     *
     * @param aid 图集id
     * @return boolean
     */
    @Override
    public boolean deleteLike(Long aid) {
        // 1. 获取登录用户的id
        Long loginUserId = SecurityUtil.getLoginUserId();
        LambdaQueryWrapper<LikeEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LikeEntity::getAid, aid)
                .eq(LikeEntity::getUid, loginUserId);
        return this.remove(wrapper);
    }
    
    /**
     * 通过图集id删除所有点赞
     *
     * @param aid 图集id
     * @return boolean
     */
    @Override
    public boolean removeLikeByAid(Long aid) {
        LambdaQueryWrapper<LikeEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LikeEntity::getAid, aid);
        return this.remove(wrapper);
    }
}