package com.elite.tetto.recommend.Controller;

import com.elite.tetto.common.constant.RecommendConstant;
import com.elite.tetto.common.utils.R;
import com.elite.tetto.recommend.service.RecommendService;
import com.elite.tetto.recommend.util.SecurityUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("recommend")
public class RecommendController {
    @Resource
    private RecommendService recommendService;
    
    // 获取推荐图集id
    @GetMapping("/atlas")
    public R getRecommendAtlasIds() {
        Long uid = SecurityUtil.getLoginUserId();
        List<String> tags = recommendService.getRecommendTags(uid, RecommendConstant.RECOMMEND_TAG_LIMIT);
        List<Long> atlasIds = recommendService.getRecommendAtlasIds(uid, RecommendConstant.RECOMMEND_ATLAS_LIMIT, tags);
        return R.ok().put("data", atlasIds).put("tags", tags);
    }
}
