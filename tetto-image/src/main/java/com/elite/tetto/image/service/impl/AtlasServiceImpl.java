package com.elite.tetto.image.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elite.tetto.common.constant.ImageConstant;
import com.elite.tetto.common.entity.vo.LoginUserRes;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.Query;
import com.elite.tetto.common.utils.R;
import com.elite.tetto.image.dao.AtlasDao;
import com.elite.tetto.image.entity.AtlasEntity;
import com.elite.tetto.image.entity.AtlasLabelEntity;
import com.elite.tetto.image.entity.ImgsEntity;
import com.elite.tetto.image.entity.vo.AtlasRes;
import com.elite.tetto.image.entity.vo.UploadAtlasVo;
import com.elite.tetto.image.feign.RecommendClient;
import com.elite.tetto.image.service.AtlasLabelService;
import com.elite.tetto.image.service.AtlasService;
import com.elite.tetto.image.service.ImgsService;
import com.elite.tetto.image.service.LikeService;
import com.elite.tetto.image.util.SecurityUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("atlasService")
public class AtlasServiceImpl extends ServiceImpl<AtlasDao, AtlasEntity> implements AtlasService {
    
    @Resource
    private AtlasLabelService atlasLabelService;
    
    @Resource
    private ImgsService imgsService;
    
    @Resource(name = "atlasServiceCache")
    private AtlasService atlasService;
    
    @Resource
    private LikeService likeService;
    
    @Resource
    private RecommendClient recommendClient;
    
    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;
    
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AtlasEntity> page = this.page(
                new Query<AtlasEntity>().getPage(params),
                new QueryWrapper<>()
        );
        
        return new PageUtils(page);
    }
    
    /**
     * 上传作品集
     *
     * @param vo 上传作品集的信息
     * @return boolean
     */
    @Override
    @Transactional
    @CacheEvict(value = {ImageConstant.USER_ALL_ATLAS}, key = "#result")
    public Long upload(UploadAtlasVo vo) {
        // 1. 获取登录用户的id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUserRes loginUserRes = (LoginUserRes) authentication.getPrincipal();
        Long uid = loginUserRes.getUid();
        // 2. 保存作品集
        AtlasEntity atlasEntity = new AtlasEntity();
        atlasEntity.setUId(uid);
        atlasEntity.setTitle(vo.getTitle());
        atlasEntity.setIntroduce(vo.getIntroduce());
        atlasEntity.setIsPublic(vo.getIsPublic());
        atlasEntity.setThumbnailUrl(vo.getThumbnailUrl());
        Date date = new Date();
        System.out.println(date);
        atlasEntity.setDate(date);
        boolean saveAtlas = this.save(atlasEntity);
        if (!saveAtlas) {
            throw new RuntimeException("保存作品集失败");
        }
        // 3. 保存作品集标签
        List<AtlasLabelEntity> atlasLabelEntities = vo.getTags()
                .stream()
                .map(tag -> {
                    AtlasLabelEntity atlasLabelEntity = new AtlasLabelEntity();
                    atlasLabelEntity.setAtlasId(atlasEntity.getId());
                    atlasLabelEntity.setLabelName(tag);
                    return atlasLabelEntity;
                }).collect(Collectors.toList());
        boolean saveAtlasLabel = atlasLabelService.saveBatch(atlasLabelEntities);
        if (!saveAtlasLabel) {
            throw new RuntimeException("保存作品集标签失败");
        }
        // 4. 保存作品集图片
        List<ImgsEntity> imgsEntities = vo.getImgUrls().stream().map(imgUrl -> {
            ImgsEntity imgsEntity = new ImgsEntity();
            imgsEntity.setAtlasId(atlasEntity.getId());
            imgsEntity.setImgUrl(imgUrl.getUrl());
            imgsEntity.setWidth(imgUrl.getWidth());
            imgsEntity.setHeight(imgUrl.getHeight());
            return imgsEntity;
        }).collect(Collectors.toList());
        boolean saveImgs = imgsService.saveBatch(imgsEntities);
        if (!saveImgs) {
            throw new RuntimeException("保存作品集图片失败");
        }
        // 删除防止缓存穿透的key
        redisTemplate.delete(ImageConstant.ATLAS_CACHE_PREFIX + "::" + atlasEntity.getId());
        return uid;
    }
    
    /**
     * 通过用户id获取所有作品集信息
     *
     * @param uid 用户id
     * @return {@link List}<{@link AtlasEntity}>
     */
    @Override
    public List<AtlasEntity> getAtlasINfoByUid(Long uid) {
        return this.getAtlasINfoByUid(uid, null);
    }
    
    /**
     * 通过用户id获取拥有限制数量的作品集信息
     *
     * @param uid   用户id
     * @param limit 限制数量
     * @return {@link List}<{@link AtlasEntity}>
     */
    @Override
    public List<AtlasEntity> getAtlasINfoByUid(Long uid, Long limit) {
        // 1. 获取登录用户的id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUserRes loginUserRes = (LoginUserRes) authentication.getPrincipal();
        Long loginUserId = loginUserRes.getUid();
        // 2. 获取用户所有作品集信息
        List<AtlasEntity> atlasEntities = atlasService.getAllAtlasByUid(uid);
        // 3. 不是自己的作品集，需要过滤掉私有作品集
        if (!uid.equals(loginUserId)) {
            atlasEntities = atlasEntities.stream()
                    .filter(atlasEntity -> atlasEntity.getIsPublic() == 1)
                    .collect(Collectors.toList());
        }
        // 4. 限制数量
        if (limit != null) {
            atlasEntities = atlasEntities.stream()
                    .limit(limit)
                    .collect(Collectors.toList());
        }
        return atlasEntities;
    }
    
    /**
     * 通过用户id获取点赞图集(分页)
     *
     * @param params 分页参数
     * @param uid    用户id
     * @return {@link List}<{@link AtlasEntity}>
     */
    @Override
    public PageUtils getLikeAtlasByUid(Map<String, Object> params, Long uid) {
        // params.put(PageConstant.LIMIT, PageConstant.DEFAULT_LIMIT);
        IPage<AtlasEntity> page = new Query<AtlasEntity>().getPage(params);
        Page<AtlasEntity> entityPage = this.baseMapper.getLikeAtlasPageByUid(page, uid);
        return new PageUtils(entityPage);
    }
    
    /**
     * 获取最新的10个作品集
     *
     * @return {@link List}<{@link AtlasRes}>
     */
    @Override
    public List<AtlasRes> getNewAtlas() {
        Long loginUserId = SecurityUtil.getLoginUserId();
        List<AtlasRes> atlasRes = this.baseMapper.getNewAtlas(10, loginUserId);
        return atlasRes;
    }
    
    /**
     * @return
     */
    @Override
    public List<AtlasRes> getRecommendAtlas() {
        Long loginUserId = SecurityUtil.getLoginUserId();
        try {
            R r = recommendClient.getRecommendAtlasIds();
            if (r.getCode() == 0) {
                List<Long> atlasIds = r.getData(new TypeReference<List<Long>>() {
                });
                List<AtlasRes> atlasRes = this.baseMapper.getRecommendAtlas(atlasIds, loginUserId, 20);
                return atlasRes;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 通过用户id获取所有(含非公开)作品集信息<br>
     * <p>
     * 用于缓存
     *
     * @param uid 用户id
     * @return {@link List}<{@link AtlasEntity}>
     */
    @Override
    public List<AtlasEntity> getAllAtlasByUid(Long uid) {
        LambdaQueryWrapper<AtlasEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AtlasEntity::getUId, uid);
        wrapper.orderByDesc(AtlasEntity::getDate);
        return this.list(wrapper);
    }
    
    /**
     * 获取作品集id获得作品集信息 <br>
     * 用于缓存
     *
     * @param aid 作品集id
     * @return {@link AtlasEntity}
     */
    @Override
    @Cacheable(value = ImageConstant.ATLAS_CACHE_PREFIX, key = "#aid")
    public AtlasEntity getAtlasInfoByAid(Long aid) {
        return this.getById(aid);
    }
    
    /**
     * 通过id删除图集<br>
     * 用于删除缓存
     *
     * @param aid         图集id
     * @param loginUserId 登录用户id
     * @return boolean
     */
    @Override
    @Caching(evict = {
            // 图集缓存
            @CacheEvict(value = ImageConstant.ATLAS_CACHE_PREFIX, key = "#aid"),
            // 用户所有图集缓存
            @CacheEvict(value = ImageConstant.USER_ALL_ATLAS, key = "#loginUserId"),
            // 图集对应的图片缓存
            @CacheEvict(value = ImageConstant.ATLAS_IMGS_CACHE_PREFIX, key = "#aid"),
            // 图集对应的标签缓存
            @CacheEvict(value = ImageConstant.ATLAS_LABEL_CACHE_PREFIX, key = "#aid")
    })
    @Transactional
    public boolean removeAtlasById(Long aid, Long loginUserId) {
        // 查看是否是自己的图集
        AtlasEntity atlasEntity = this.getAtlasInfoByAid(aid);
        if (!atlasEntity.getUId().equals(loginUserId)) {
            throw new RuntimeException("无法删除他人的作品集");
        }
        // 删除图集
        boolean removeById = this.removeById(aid);
        if (!removeById) {
            throw new RuntimeException("删除作品集失败");
        }
        // 删除图集下的图片
        boolean removeImageByAid = imgsService.removeImageByAid(aid);
        if (!removeImageByAid) {
            throw new RuntimeException("删除作品集下的作品失败");
        }
        // 删除图集的标签
        boolean removeAtlasTagByAid = atlasLabelService.removeAtlasTagByAid(aid);
        if (!removeAtlasTagByAid) {
            throw new RuntimeException("删除作品集下的标签失败");
        }
        // 删除图集的点赞
        boolean removeLikeByAid = likeService.removeLikeByAid(aid);
        if (!removeLikeByAid) {
            throw new RuntimeException("删除作品集下的点赞失败");
        }
        return true;
    }
    
    /**
     * 更新通过id <br>
     * <p>
     * 更新后删除缓存
     *
     * @param entity 实体
     * @return boolean
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = ImageConstant.ATLAS_CACHE_PREFIX, key = "#entity.id"),
            @CacheEvict(value = ImageConstant.USER_ALL_ATLAS, key = "#entity.uId"),
    })
    public boolean updateById(AtlasEntity entity) {
        return super.updateById(entity);
    }
}