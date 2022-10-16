package com.elite.tetto.image.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.elite.tetto.image.entity.HistoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * 
 * 
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-10-15 21:19:41
 */
@Mapper
public interface HistoryDao extends BaseMapper<HistoryEntity> {
    
    /**
     * 添加浏览历史
     *
     * @param uid 用户id
     * @param aid 图集id
     * @return boolean
     */
    boolean addHistory(@Param("uid") Long uid, @Param("aid") Long aid, @Param("browseTime") Date browseTime);
}
