package com.elite.tetto.search.util;

import com.elite.tetto.common.entity.vo.LoginUserRes;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static Long getLoginUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUserRes loginUserRes = (LoginUserRes) authentication.getPrincipal();
        return loginUserRes.getUid();
    }
}
