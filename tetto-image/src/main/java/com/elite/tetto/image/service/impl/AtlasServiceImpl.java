package com.elite.tetto.image.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
    public boolean upload(UploadAtlasVo vo) {
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
            imgsEntity.setImgUrl(imgUrl);
            return imgsEntity;
        }).collect(Collectors.toList());
        boolean saveImgs = imgsService.saveBatch(imgsEntities);
        if (!saveImgs) {
            throw new RuntimeException("保存作品集图片失败");
        }
        return true;
    }
    
}