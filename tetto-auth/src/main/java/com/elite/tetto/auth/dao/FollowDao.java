package com.elite.tetto.auth.dao;

import com.elite.tetto.auth.entity.FollowEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.elite.tetto.auth.entity.vo.FollowerRes;
import com.elite.tetto.auth.entity.vo.FollowingRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-10-04 14:32:08
 */
@Mapper
public interface FollowDao extends BaseMapper<FollowEntity> {
    
    /**
     * 获取用户的粉丝
     *
     * @param uid 用户id
     * @return {@link List}<{@link FollowerRes}>
     */
    List<FollowerRes> getFollowers(@Param("uid") long uid);
    
    /**
     * 获取用户的关注
     *
     * @param uid 用户id
     * @return {@link List}<{@link FollowerRes}>
     */
    List<FollowingRes> getFollowings(@Param("uid") Long uid);
}
