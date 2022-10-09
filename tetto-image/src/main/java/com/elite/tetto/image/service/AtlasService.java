package com.elite.tetto.image.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.image.entity.AtlasEntity;
import com.elite.tetto.image.entity.vo.UploadAtlasVo;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-10-07 14:39:01
 */
public interface AtlasService extends IService<AtlasEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    /**
     * 上传作品集
     *
     * @param vo 上传作品集的信息
     * @return boolean
     */
    boolean upload(UploadAtlasVo vo);
    
    /**
     * 通过用户id获取作品集信息
     *
     * @param uid 用户id
     * @return {@link List}<{@link AtlasEntity}>
     */
    List<AtlasEntity> getAtlasINfoByUid(Long uid);
}

