package com.elite.tetto.image.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import com.elite.tetto.image.entity.vo.OnlyImgRes;
import com.elite.tetto.image.feign.AuthClient;
import com.elite.tetto.image.service.*;
import com.elite.tetto.image.util.SecurityUtil;
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
    
    @Resource
    private LikeService likeService;
    
    @Resource
    private HistoryService historyService;
    
    @Resource(name = "imgServiceCache")
    private ImgsService imgServiceCache;
    
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ImgsEntity> page = this.page(
                new Query<ImgsEntity>().getPage(params),
                new QueryWrapper<>()
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
        // 获取作品集信息 缓存
        AtlasEntity atlas = atlasService.getAtlasInfoByAid(aid);
        if (atlas == null) {
            return null;
        }
        Long loginUserId = SecurityUtil.getLoginUserId();
        // 不是自己的非公开作品集
        Long uid = atlas.getUId();
        if (!loginUserId.equals(uid) && atlas.getIsPublic() == 0) {
            return null;
        }
        ImgRes imgRes = new ImgRes();
        imgRes.setAId(atlas.getId());
        imgRes.setAtlas(atlas);
        try {
            R r = authClient.getUserInfoByUid(uid);
            UserInfoRes userInfoRes = r.getData(new TypeReference<UserInfoRes>() {
            });
            // 设置作者信息
            imgRes.setUserInfoRes(userInfoRes);
        } catch (Exception e) {
            return null;
        }
        // 获取作品集图片 缓存
        List<ImgsEntity> imgEntities = imgServiceCache.getImgsByAid(aid);
        imgRes.setImgEntities(imgEntities);
        // 获取作品集标签 缓存
        List<String> tags = atlasLabelService.getAtlasLabelsByAid(aid);
        imgRes.setTags(tags);
        // 获取作者最新作品集
        List<AtlasEntity> latestAtlas = atlasService.getAtlasINfoByUid(uid, ImageConstant.LATEST_ATLAS_NUM);
        imgRes.setLatestAtlas(latestAtlas);
        // 获取登录用户是否点赞
        boolean like = likeService.isLike(loginUserId, aid);
        imgRes.setLike(like);
        // 添加浏览历史
        historyService.addHistory(loginUserId, aid);
        return imgRes;
    }
    
    /**
     * 通过图集id获取只有单个图集信息的返回体
     *
     * @param aid 图集id
     * @return {@link OnlyImgRes}
     */
    @Override
    public OnlyImgRes getOnlyImgResByAid(Long aid) {
        // 获取作品集信息 缓存
        AtlasEntity atlas = atlasService.getAtlasInfoByAid(aid);
        if (atlas == null) {
            return null;
        }
        Long loginUserId = SecurityUtil.getLoginUserId();
        // 不是自己的非公开作品集
        if (!loginUserId.equals(atlas.getUId()) && atlas.getIsPublic() == 0) {
            return null;
        }
        OnlyImgRes onlyImgRes = new OnlyImgRes();
        onlyImgRes.setAId(atlas.getId());
        onlyImgRes.setAtlas(atlas);
        // 获取作品集图片 缓存
        List<ImgsEntity> imgEntities = imgServiceCache.getImgsByAid(aid);
        onlyImgRes.setImgEntities(imgEntities);
        // 获取作品集标签 缓存
        List<String> tags = atlasLabelService.getAtlasLabelsByAid(aid);
        onlyImgRes.setTags(tags);
        // 获取登录用户是否点赞
        boolean like = likeService.isLike(loginUserId, aid);
        onlyImgRes.setLike(like);
        return onlyImgRes;
    }
    
    
    /**
     * 通过图集id删除图集的所有图片
     *
     * @param aid 图集id
     * @return boolean
     */
    @Override
    public boolean removeImageByAid(Long aid) {
        LambdaQueryWrapper<ImgsEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ImgsEntity::getAtlasId, aid);
        return this.remove(wrapper);
    }
    
    
    /**
     * 用于缓存，在子类中实现
     *
     * @param aid 图集id
     * @return {@link List}<{@link ImgsEntity}>
     */
    @Override
    public List<ImgsEntity> getImgsByAid(Long aid) {
        return null;
    }
}