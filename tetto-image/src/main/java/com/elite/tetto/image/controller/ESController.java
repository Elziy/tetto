package com.elite.tetto.image.controller;

import com.elite.tetto.common.entity.vo.es.AtlasESModel;
import com.elite.tetto.common.utils.R;
import com.elite.tetto.image.service.ESService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class ESController {
    
    @Resource
    private ESService esService;
    
    @PutMapping("image/es/8BKB9ZN5lCGVqTJ0SeMWee6Uu")
    public R getESData() {
        List<AtlasESModel> atlasESModels = esService.getAtlasESModels();
        return R.ok().put("data", atlasESModels);
    }
}
