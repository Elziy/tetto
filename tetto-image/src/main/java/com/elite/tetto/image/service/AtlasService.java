package com.elite.tetto.image.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.elite.tetto.common.entity.vo.UpLoadAtlasVo;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.image.entity.AtlasEntity;

import java.util.Map;

/**
 * 
 *
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-10-04 17:23:12
 */
public interface AtlasService extends IService<AtlasEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 添加新的图集
     *
     * @param upLoadAtlasVo 新建图集实体
     * @return boolean  true:注册成功 false:注册失败
     */
    boolean upLoadAtla(UpLoadAtlasVo upLoadAtlasVo);
}

