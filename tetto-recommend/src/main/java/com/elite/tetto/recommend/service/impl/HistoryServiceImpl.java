package com.elite.tetto.recommend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.Query;
import com.elite.tetto.recommend.dao.HistoryDao;
import com.elite.tetto.recommend.entity.HistoryEntity;
import com.elite.tetto.recommend.service.HistoryService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service("historyService")
public class HistoryServiceImpl extends ServiceImpl<HistoryDao, HistoryEntity> implements HistoryService {
    
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<HistoryEntity> page = this.page(
                new Query<HistoryEntity>().getPage(params),
                new QueryWrapper<HistoryEntity>()
        );
        
        return new PageUtils(page);
    }
    
    /**
     * 添加浏览历史
     *
     * @param uid 用户id
     * @param aid 图集id
     * @return boolean
     */
    @Override
    public boolean addHistory(Long uid, Long aid) {
        Date date = new Date();
        System.out.println(date);
        return this.baseMapper.addHistory(uid, aid, date);
    }
}