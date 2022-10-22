package com.elite.tetto.image.feign;

import com.elite.tetto.common.entity.vo.es.AtlasESModel;
import com.elite.tetto.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("tetto-search-service")
public interface SearchClient {
    
    @PostMapping("/search/es/save/atlas")
    R saveAtlas(@RequestBody AtlasESModel atlasESModel);
    
    @PostMapping("/search/es/delete/atlas/{aid}")
    R deleteAtlas(@PathVariable("aid") Long aid);
}
