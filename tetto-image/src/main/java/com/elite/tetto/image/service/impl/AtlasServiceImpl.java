package com.elite.tetto.image.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elite.tetto.common.constant.ImageConstant;
import com.elite.tetto.common.entity.vo.LoginUserRes;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.Query;
import com.elite.tetto.image.dao.AtlasDao;
import com.elite.tetto.image.entity.AtlasEntity;
import com.elite.tetto.image.entity.AtlasLabelEntity;
import com.elite.tetto.image.entity.ImgsEntity;
import com.elite.tetto.image.entity.vo.UploadAtlasVo;
import com.elite.tetto.image.service.AtlasLabelService;
import com.elite.tetto.image.service.AtlasService;
import com.elite.tetto.image.service.ImgsService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AtlasEntity> page = this.page(
                new Query<AtlasEntity>().getPage(params),
                new QueryWrapper<AtlasEntity>()
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
    @CacheEvict(value = {ImageConstant.USER_ALL_ATLAS, ImageConstant.USER_LATEST_ATLAS}, key = "#result")
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
        atlasEntity.setDate(new Date());
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
        return uid;
    }
    
    /**
     * 通过用户id获取所有作品集信息
     *
     * @param uid 用户id
     * @return {@link List}<{@link AtlasEntity}>
     */
    @Override
    @Cacheable(value = ImageConstant.USER_ALL_ATLAS, key = "#uid")
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
    @Cacheable(value = ImageConstant.USER_LATEST_ATLAS, key = "#uid")
    public List<AtlasEntity> getAtlasINfoByUid(Long uid, Long limit) {
        // 1. 获取登录用户的id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUserRes loginUserRes = (LoginUserRes) authentication.getPrincipal();
        Long loginUserId = loginUserRes.getUid();
        LambdaQueryWrapper<AtlasEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AtlasEntity::getUId, uid);
        // 2. 如果是自己的作品集，就查询所有的作品集，如果不是自己的作品集，就只查询公开的作品集
        if (!loginUserId.equals(uid)) {
            queryWrapper.eq(AtlasEntity::getIsPublic, 1);
        }
        // 3. 设置查询数量
        queryWrapper.last(limit != null, "limit " + limit);
        // 4. 设置时间倒叙排序
        queryWrapper.orderByDesc(AtlasEntity::getDate);
        return this.list(queryWrapper);
    }
    
    /**
     * 通过作品集id获得作品集信息
     *
     * @param aid 援助
     * @return {@link AtlasEntity}
     */
    @Override
    @Cacheable(value = ImageConstant.ATLAS_CACHE_PREFIX, key = "#aid")
    public AtlasEntity getAtlasInfoByAid(Long aid) {
        return this.getById(aid);
    }
    
}