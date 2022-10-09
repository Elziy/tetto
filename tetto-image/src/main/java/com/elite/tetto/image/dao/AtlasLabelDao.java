package com.elite.tetto.image.dao;

import com.elite.tetto.image.entity.AtlasLabelEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-10-07 14:39:01
 */
@Mapper
public interface AtlasLabelDao extends BaseMapper<AtlasLabelEntity> {
    
    List<String> getAtlasLabelsByAid(@Param("aid") Long aid);
}
