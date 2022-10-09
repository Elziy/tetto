package com.elite.tetto.image.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.image.entity.ImgsEntity;
import com.elite.tetto.image.entity.vo.ImgRes;

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
     * 通过作品集id获取所有图片实体
     *
     * @param aid 援助
     * @return {@link List}<{@link ImgsEntity}>
     */
    List<ImgsEntity> getImgsByAid(Long aid);
}

