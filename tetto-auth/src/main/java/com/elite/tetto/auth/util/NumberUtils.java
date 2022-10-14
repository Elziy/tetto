package com.elite.tetto.auth.util;

public class NumberUtils {
    /**
     * 获取随机验证码
     *
     * @return {@link String}
     */
    public static String getCode() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
    }
}
