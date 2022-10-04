package com.elite.tetto.image.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.image.entity.ImgsEntity;

import java.util.Map;

/**
 * 
 *
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-10-04 17:23:12
 */
public interface ImgsService extends IService<ImgsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

