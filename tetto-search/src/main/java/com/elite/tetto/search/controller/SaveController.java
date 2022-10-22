package com.elite.tetto.search.controller;

import com.elite.tetto.common.entity.vo.es.AtlasESModel;
import com.elite.tetto.common.utils.R;
import com.elite.tetto.search.service.SaveService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class SaveController {
    
    @Resource
    private SaveService saveService;
    
    @PostMapping("/search/es/save/atlas")
    public R saveAtlas(@RequestBody AtlasESModel atlasESModel) {
        boolean b = saveService.saveAtlas(atlasESModel);
        if (b) {
            return R.ok();
        } else {
            return R.error();
        }
    }
    
    @PostMapping("/search/es/delete/atlas/{aid}")
    public R deleteAtlas(@PathVariable("aid") Long aid) {
        boolean b = saveService.deleteAtlas(aid);
        if (b) {
            return R.ok();
        } else {
            return R.error();
        }
    }
}
