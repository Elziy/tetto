package com.elite.tetto.image.feign;

import com.elite.tetto.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("tetto-auth-service")
public interface AuthClient {
    /**
     * 通过uid获取用户信息
     *
     * @param uid uid
     * @return {@link R}
     */
    @GetMapping("/auth/user/info/{uid}")
    R getUserInfoByUid(@PathVariable("uid") Long uid);
}
