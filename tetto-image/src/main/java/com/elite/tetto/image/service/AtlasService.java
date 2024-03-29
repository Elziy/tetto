package com.elite.tetto.image.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.image.entity.AtlasEntity;
import com.elite.tetto.image.entity.vo.AtlasRes;
import com.elite.tetto.image.entity.vo.RecommendRes;
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
     * 通过用户id获取所有点赞图集(分页)
     *
     * @param params 分页参数
     * @param uid    用户id
     * @return {@link List}<{@link AtlasEntity}>
     */
    PageUtils getLikeAtlasByUid(Map<String, Object> params, Long uid);
    
    /**
     * 获取最新的10个作品集
     *
     * @return {@link List}<{@link AtlasRes}>
     */
    List<AtlasRes> getNewAtlas();
    
    /**
     * 获取推荐图集
     *
     * @return {@link RecommendRes}
     */
    RecommendRes getRecommendAtlas();
    
    /**
     * 添加热榜图集
     *
     * @param aid 图集id
     */
    void addHotAtlas(Long aid);
    
    /**
     * 获取热榜的图集id
     *
     * @param key 获取热榜日期的key 2022-01-01
     * @return {@link List}<{@link Long}>
     */
    List<Long> getHotTopAtlasId(String key);
    
    /**
     * 获取昨天热榜图集
     *
     * @return {@link List}<{@link AtlasRes}>
     */
    List<AtlasRes> getYesterdayHotTopAtlas();
    
    /**
     * 通过用户id获取所有(含非公开)作品集信息<br>
     * <p>
     * 用于缓存
     *
     * @param uid 用户id
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
    
    /**
     * 通过id删除图集<br>
     * 用于删除缓存
     *
     * @param aid         图集id
     * @param loginUserId 登录用户id
     * @return boolean
     */
    boolean removeAtlasById(Long aid, Long loginUserId);
}

