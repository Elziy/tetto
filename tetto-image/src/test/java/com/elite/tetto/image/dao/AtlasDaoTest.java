package com.elite.tetto.image.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.elite.tetto.image.entity.AtlasEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AtlasDaoTest {
    
    @Resource
    AtlasDao atlasDao;
    
    @Test
    public void getLikeAtlasPageByUid() {
        Page<AtlasEntity> page = new Page<>(1, 2);
        Page<AtlasEntity> entityPage = atlasDao.getLikeAtlasPageByUid(page, 1L);
        List<AtlasEntity> atlasEntities = entityPage.getRecords();
        atlasEntities.forEach(System.out::println);
        System.out.println("hasNext = " + entityPage.hasNext());
    }
}