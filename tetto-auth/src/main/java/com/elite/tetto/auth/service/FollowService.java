package com.elite.tetto.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.auth.entity.FollowEntity;

import java.util.Map;

/**
 * 
 *
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-10-04 14:32:08
 */
public interface FollowService extends IService<FollowEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

