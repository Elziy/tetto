package com.elite.tetto.image.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.image.entity.AtlasEntity;
import com.elite.tetto.image.entity.vo.UploadAtlasVo;

import java.util.List;
import java.util.Map;

/**
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
    Long upload(UploadAtlasVo vo);
    
    /**
     * 通过用户id获取所有作品集信息
     *
     * @param uid 用户id
     * @return {@link List}<{@link AtlasEntity}>
     */
    List<AtlasEntity> getAtlasINfoByUid(Long uid);
    
    /**
     * 通过用户id获取限制数量的最新作品集信息
     *
     * @param uid   用户id
     * @param limit 限制数量
     * @return {@link List}<{@link AtlasEntity}>
     */
    List<AtlasEntity> getAtlasINfoByUid(Long uid, Long limit);
    
    /**
     * 获取所有点赞图集通过用户id
     *
     * @param uid 用户id
     * @return {@link List}<{@link AtlasEntity}>
     */
    List<AtlasEntity> getLikeAtlasByUid(Long uid);
    
    /**
     * 通过用户id获取所有(含非公开)作品集信息<br>
     *
     * 用于缓存
     *
     * @param uid   用户id
     * @return {@link List}<{@link AtlasEntity}>
     */
    List<AtlasEntity> getAllAtlasByUid(Long uid);
    
    /**
     * 获取作品集id获得作品集信息 <br>
     * 用于缓存
     *
     * @param aid 作品集id
     * @return {@link AtlasEntity}
     */
    AtlasEntity getAtlasInfoByAid(Long aid);
}

