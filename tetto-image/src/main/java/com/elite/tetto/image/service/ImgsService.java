package com.elite.tetto.image.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.image.entity.ImgsEntity;
import com.elite.tetto.image.entity.vo.ImgRes;
import com.elite.tetto.image.entity.vo.OnlyImgRes;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-10-07 14:39:01
 */
public interface ImgsService extends IService<ImgsEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    /**
     * 获取作品集id获得作品集返回体
     *
     * @param aid 作品集id
     * @return {@link ImgRes}
     */
    ImgRes getImgResByAid(Long aid);
    
    /**
     * 通过图集id获取只有单个图集信息的返回体
     *
     * @param aid 图集id
     * @return {@link OnlyImgRes}
     */
    OnlyImgRes getOnlyImgResByAid(Long aid);
    
    /**
     * 通过作品集id获取所有图片实体<br>
     * 用户缓存
     *
     * @param aid 图集id
     * @return {@link List}<{@link ImgsEntity}>
     */
    List<ImgsEntity> getImgsByAid(Long aid);
    
    /**
     * 通过图集id删除图集的所有图片<br>
     *
     * @param aid 图集id
     * @return boolean
     */
    boolean removeImageByAid(Long aid);
}

