package com.elite.tetto.search.feign;

import com.elite.tetto.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient("tetto-image-service")
public interface ImageClient {
    @PutMapping("image/es/8BKB9ZN5lCGVqTJ0SeMWee6Uu")
    R getESData();
}
