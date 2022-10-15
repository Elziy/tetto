package com.elite.tetto.image.feign;

import com.elite.tetto.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("tetto-recommend-service")
public interface RecommendClient {
    
    /**
     * 添加浏览历史
     *
     * @param aid 图集id
     * @return {@link R}
     */
    @PostMapping("recommend/history/{aid}")
    R addHistory(@PathVariable("aid") Long aid);
}
