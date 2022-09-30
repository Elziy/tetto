package com.elite.tetto.auth.dao;

import com.elite.tetto.auth.entity.UserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-09-30 20:52:00
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {
	
}
