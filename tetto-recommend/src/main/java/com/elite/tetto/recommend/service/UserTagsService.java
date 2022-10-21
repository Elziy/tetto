package com.elite.tetto.recommend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.recommend.entity.UserTagsEntity;

import java.util.Map;

/**
 * 
 *
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-10-21 19:57:54
 */
public interface UserTagsService extends IService<UserTagsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

