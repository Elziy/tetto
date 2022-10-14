package com.elite.tetto.common.constant;

/**
 * 权限认证相关常量
 *
 * @author Elziy
 */
public class AuthConstant {
    /**
     * redis中存储用户信息键值的前缀
     */
    public static final String LOGIN_USER_KEY = "loginUser:";
    
    public static final String USER_INFO_KEY = "userInfo";
    
    /**
     * 缓存验证码的前缀
     */
    public static final String VERIFICATION_CODE_KEY = "code:";
    
    // 邮件验证码操作
    public static final String REGISTER = "注册账号";
    
    // 邮件验证码主题
    public static final String REGISTER_SUBJECT = "welcome to tetto";
}
