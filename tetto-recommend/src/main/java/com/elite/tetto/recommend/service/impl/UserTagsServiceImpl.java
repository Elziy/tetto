package com.elite.tetto.recommend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.Query;
import com.elite.tetto.recommend.dao.UserTagsDao;
import com.elite.tetto.recommend.entity.UserTagsEntity;
import com.elite.tetto.recommend.service.UserTagsService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("userTagsService")
public class UserTagsServiceImpl extends ServiceImpl<UserTagsDao, UserTagsEntity> implements UserTagsService {
    
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserTagsEntity> page = this.page(
                new Query<UserTagsEntity>().getPage(params),
                new QueryWrapper<UserTagsEntity>()
        );
        
        return new PageUtils(page);
    }
    
    /**
     * 保存用户标签
     *
     * @param uid  用户id
     * @param tags 标签
     * @return boolean
     */
    @Override
    public boolean saveUserTags(Long uid, Map<String, Long> tags) {
        
        tags.forEach((k, v) -> {
            UserTagsEntity userTagsEntity = new UserTagsEntity();
            userTagsEntity.setUid(uid);
            userTagsEntity.setTag(k);
            userTagsEntity.setCount(v);
            this.baseMapper.insert(userTagsEntity);
        });
        
        return false;
    }
    
}