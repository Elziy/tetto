package com.elite.tetto.image.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.elite.tetto.common.entity.vo.es.AtlasESModel;
import com.elite.tetto.common.entity.vo.es.ImgUrl;
import com.elite.tetto.image.dao.ESDao;
import com.elite.tetto.image.entity.AtlasLabelEntity;
import com.elite.tetto.image.entity.ImgsEntity;
import com.elite.tetto.image.service.AtlasLabelService;
import com.elite.tetto.image.service.ESService;
import com.elite.tetto.image.service.ImgsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ESServiceImpl implements ESService {
    
    @Resource
    private ESDao esDao;
    
    @Resource
    private AtlasLabelService atlasLabelService;
    
    @Resource
    private ImgsService imgsService;
    
    public List<AtlasESModel> getAtlasESModels() {
        List<AtlasESModel> atlasESModels = esDao.getAtlasESModel();
        atlasESModels.forEach(atlasESModel -> {
            LambdaQueryWrapper<AtlasLabelEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(AtlasLabelEntity::getAtlasId, atlasESModel.getId());
            List<AtlasLabelEntity> atlasLabelEntities = atlasLabelService.list(wrapper);
            List<String> tags = atlasLabelEntities.stream()
                    .map(AtlasLabelEntity::getLabelName)
                    .collect(Collectors.toList());
            atlasESModel.setTags(tags);
            
            LambdaQueryWrapper<ImgsEntity> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(ImgsEntity::getAtlasId, atlasESModel.getId());
            List<ImgsEntity> imgsEntities = imgsService.list(wrapper1);
            List<ImgUrl> imgUrls = imgsEntities.stream().map(imgsEntity -> {
                String imgUrl = imgsEntity.getImgUrl();
                Integer width = imgsEntity.getWidth();
                Integer height = imgsEntity.getHeight();
                ImgUrl url = new ImgUrl();
                url.setUrl(imgUrl);
                url.setWidth(width);
                url.setHeight(height);
                return url;
            }).collect(Collectors.toList());
            atlasESModel.setImgUrls(imgUrls);
        });
        return atlasESModels;
    }
}
