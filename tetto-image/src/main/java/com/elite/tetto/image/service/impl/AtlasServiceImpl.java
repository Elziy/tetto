package com.elite.tetto.image.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.elite.tetto.common.entity.vo.UpLoadAtlasVo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.Query;

import com.elite.tetto.image.dao.AtlasDao;
import com.elite.tetto.image.entity.AtlasEntity;
import com.elite.tetto.image.service.AtlasService;


@Service("atlasService")
public class AtlasServiceImpl extends ServiceImpl<AtlasDao, AtlasEntity> implements AtlasService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AtlasEntity> page = this.page(
                new Query<AtlasEntity>().getPage(params),
                new QueryWrapper<AtlasEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public boolean upLoadAtla(UpLoadAtlasVo upLoadAtlasVo) {
        Long author=upLoadAtlasVo.getAuthor();
        Date uploaddate=upLoadAtlasVo.getUploaddate();
        Long collectionnum=upLoadAtlasVo.getCollectionnum();
        Long browsenum=upLoadAtlasVo.getBrowsenum();
        String title=upLoadAtlasVo.getTitle();
        String introduction=upLoadAtlasVo.getIntroduction();
        String thumbnail=upLoadAtlasVo.getThumbnail();
        LambdaQueryWrapper<AtlasEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AtlasEntity::getTitle, title);
        AtlasEntity atlasEntity = this.getOne(wrapper);
        if (atlasEntity != null) {
            return false;
        } else {
            AtlasEntity entity = new AtlasEntity();
            entity.setAuthor(author);
            entity.setUploaddate(uploaddate);
            entity.setCollectionnum(collectionnum);
            entity.setBrowsenum(browsenum);
            entity.setTitle(title);
            entity.setIntroduction(introduction);
            entity.setThumbnail(thumbnail);
            return this.save(entity);
        }
    }


}