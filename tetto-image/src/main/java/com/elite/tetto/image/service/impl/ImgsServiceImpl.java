package com.elite.tetto.image.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elite.tetto.common.constant.ImageConstant;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.Query;
import com.elite.tetto.common.utils.R;
import com.elite.tetto.image.dao.ImgsDao;
import com.elite.tetto.image.entity.AtlasEntity;
import com.elite.tetto.image.entity.ImgsEntity;
import com.elite.tetto.image.entity.to.UserInfoRes;
import com.elite.tetto.image.entity.vo.ImgRes;
import com.elite.tetto.image.feign.AuthClient;
import com.elite.tetto.image.service.AtlasLabelService;
import com.elite.tetto.image.service.AtlasService;
import com.elite.tetto.image.service.ImgsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Service("imgsService")
public class ImgsServiceImpl extends ServiceImpl<ImgsDao, ImgsEntity> implements ImgsService {
    
    @Resource
    private AuthClient authClient;
    
    @Resource
    private AtlasService atlasService;
    
    @Resource
    private AtlasLabelService atlasLabelService;
    
    @Resource(name = "imgServiceCache")
    private ImgsService imgServiceCache;
    
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ImgsEntity> page = this.page(
                new Query<ImgsEntity>().getPage(params),
                new QueryWrapper<ImgsEntity>()
        );
        
        return new PageUtils(page);
    }
    
    /**
     * 获取作品集id获得作品集信息
     *
     * @param aid 作品集id
     * @return {@link ImgRes}
     */
    @Override
    public ImgRes getImgResByAid(Long aid) {
        // 获取作品集信息
        AtlasEntity atlas = atlasService.getAtlasInfoByAid(aid);
        if (atlas == null) {
            return null;
        }
        ImgRes imgRes = new ImgRes();
        imgRes.setAId(atlas.getId());
        imgRes.setAtlas(atlas);
        // 作者id
        Long uId = atlas.getUId();
        try {
            R r = authClient.getUserInfoByUid(uId);
            UserInfoRes userInfoRes = r.getData(new TypeReference<UserInfoRes>() {
            });
            imgRes.setUserInfoRes(userInfoRes);
        } catch (Exception e) {
            return null;
        }
        // 获取作品集图片
        List<ImgsEntity> imgEntities = imgServiceCache.getImgsByAid(aid);
        imgRes.setImgEntities(imgEntities);
        List<String> tags = atlasLabelService.getAtlasLabelsByAid(aid);
        imgRes.setTags(tags);
        List<AtlasEntity> latestAtlas = atlasService.getAtlasINfoByUid(uId, ImageConstant.LATEST_ATLAS_NUM);
        imgRes.setLatestAtlas(latestAtlas);
        return imgRes;
    }
    
    /**
     * 用于缓存，在子类中实现
     *
     * @param aid 援助
     * @return {@link List}<{@link ImgsEntity}>
     */
    @Override
    public List<ImgsEntity> getImgsByAid(Long aid) {
        return null;
    }
}