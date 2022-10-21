package com.elite.tetto.image.feign;

import com.elite.tetto.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("tetto-recommend-service")
public interface RecommendClient {
    
    /**
     * 获取推荐图集id
     *
     * @return {@link R}
     */
    @GetMapping("/recommend/atlas")
    R getRecommendAtlasIds();
}
