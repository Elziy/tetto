package com.elite.tetto.search.controller;

import com.elite.tetto.common.exception.ExceptionCode;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.common.utils.R;
import com.elite.tetto.search.service.impl.SearchService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/search")
public class SearchController {
    
    @Resource
    private SearchService searchService;
    
    @PostMapping("/atlas")
    public R search(@RequestBody Map<String, Object> params) {
        PageUtils pageUtils = null;
        try {
            pageUtils = searchService.searchAtlasPage(params);
        } catch (Exception e) {
            return R.error(ExceptionCode.UNKNOWN_EXCEPTION.getCode(), e.getMessage());
        }
        return R.ok().put("data", pageUtils);
    }
}
