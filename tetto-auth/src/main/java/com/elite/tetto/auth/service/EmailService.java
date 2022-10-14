package com.elite.tetto.auth.service;

/**
 * 邮箱服务
 *
 * @author Elziy
 */
public interface EmailService {
    
    /**
     * 发送邮件
     *
     * @param mail 邮件
     * @param code 验证码
     */
    boolean sendMail(String mail, String code, String operate, String subject);
}
