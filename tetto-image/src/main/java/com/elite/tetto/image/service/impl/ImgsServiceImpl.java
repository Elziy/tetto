package com.elite.tetto.image.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elite.tetto.common.constant.ImageConstant;
import com.elite.tetto.common.entity.vo.LoginUserRes;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;


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
    
    @Resource(name = "threadPoolExecutor")
    private ThreadPoolExecutor executor;
    
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
    public ImgRes getImgResByAid(Long aid) throws ExecutionException, InterruptedException {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        LoginUserRes loginUserRes = (LoginUserRes) authentication.getPrincipal();
        Long loginUserId = loginUserRes.getUid();
    
        ImgRes imgRes = new ImgRes();
        
        CompletableFuture<AtlasEntity> atlasEntityCompletableFuture = CompletableFuture.supplyAsync(() -> {
            // 获取作品集信息 缓存
            AtlasEntity atlas = atlasService.getAtlasInfoByAid(aid);
            if (atlas == null) {
                throw new RuntimeException("作品集不存在");
            } else {
                Long uid = atlas.getUId();
                if (!loginUserId.equals(uid) && atlas.getIsPublic() == 0) {
                    throw new RuntimeException("作品集不存在");
                }
                return atlas;
            }
        }, executor);
        
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        CompletableFuture<Void> user = atlasEntityCompletableFuture.thenAcceptAsync(atlas -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            try {
                R r = authClient.getUserInfoByUid(atlas.getUId());
                UserInfoRes userInfoRes = r.getData(new TypeReference<UserInfoRes>() {
                });
                // 设置作者信息
                imgRes.setUserInfoRes(userInfoRes);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("获取作者信息失败");
            }
        }, executor);
        
        CompletableFuture<Void> img = atlasEntityCompletableFuture.thenAcceptAsync(atlas -> {
            imgRes.setAId(atlas.getId());
            imgRes.setAtlas(atlas);
            // 获取作品集图片 缓存
            List<ImgsEntity> imgEntities = imgServiceCache.getImgsByAid(aid);
            imgRes.setImgEntities(imgEntities);
        }, executor);
        
        CompletableFuture<Void> tag = atlasEntityCompletableFuture.thenAcceptAsync(atlas -> {
            // 获取作品集标签 缓存
            List<String> tags = atlasLabelService.getAtlasLabelsByAid(aid);
            imgRes.setTags(tags);
        }, executor);
        
        CompletableFuture<Void> latest = atlasEntityCompletableFuture.thenAcceptAsync(atlas -> {
            SecurityContextHolder.setContext(context);
            // 获取作者最新作品集
            List<AtlasEntity> latestAtlas = atlasService.getAtlasINfoByUid(atlas.getUId(), ImageConstant.LATEST_ATLAS_NUM);
            imgRes.setLatestAtlas(latestAtlas);
        }, executor);
        
        CompletableFuture<Void> likes = atlasEntityCompletableFuture.thenAcceptAsync(atlas -> {
            // 获取登录用户是否点赞
            boolean like = likeService.isLike(loginUserId, aid);
            imgRes.setLike(like);
        }, executor);
        
        atlasEntityCompletableFuture.thenAcceptAsync(atlas -> {
            // 添加浏览历史
            historyService.addHistory(loginUserId, aid);
        }, executor);
        
        CompletableFuture.allOf(user, img, tag, latest, likes).get();
        
        return imgRes;
    }
    
    /**
     * 通过图集id获取只有单个图集信息的返回体
     *
     * @param aid 图集id
     * @return {@link OnlyImgRes}
     */
    @Override
    public OnlyImgRes getOnlyImgResByAid(Long aid) throws ExecutionException, InterruptedException {
        Long loginUserId = SecurityUtil.getLoginUserId();
        CompletableFuture<AtlasEntity> atlasEntityCompletableFuture = CompletableFuture.supplyAsync(() -> {
            // 获取作品集信息 缓存
            AtlasEntity atlas = atlasService.getAtlasInfoByAid(aid);
            if (atlas == null) {
                throw new RuntimeException("作品集不存在");
            } else {
                // 不是自己的非公开作品集
                Long uid = atlas.getUId();
                if (!loginUserId.equals(uid) && atlas.getIsPublic() == 0) {
                    throw new RuntimeException("作品集不存在");
                }
                return atlas;
            }
        }, executor);
        
        OnlyImgRes onlyImgRes = new OnlyImgRes();
        
        CompletableFuture<Void> likes = atlasEntityCompletableFuture.thenAcceptAsync(atlas -> {
            // 获取登录用户是否点赞
            boolean like = likeService.isLike(loginUserId, aid);
            onlyImgRes.setLike(like);
        }, executor);
        
        
        CompletableFuture<Void> imgs = atlasEntityCompletableFuture.thenAcceptAsync(atlas -> {
            onlyImgRes.setAId(atlas.getId());
            onlyImgRes.setAtlas(atlas);
            // 获取作品集图片 缓存
            List<ImgsEntity> imgEntities = imgServiceCache.getImgsByAid(aid);
            onlyImgRes.setImgEntities(imgEntities);
        }, executor);
        
        CompletableFuture<Void> tag = atlasEntityCompletableFuture.thenAcceptAsync(atlas -> {
            // 获取作品集标签 缓存
            List<String> tags = atlasLabelService.getAtlasLabelsByAid(aid);
            onlyImgRes.setTags(tags);
        }, executor);
        
        CompletableFuture.allOf(likes, imgs, tag).get();
        
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