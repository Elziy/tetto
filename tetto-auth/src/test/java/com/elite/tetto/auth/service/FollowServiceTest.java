package com.elite.tetto.auth.service;

import com.elite.tetto.common.constant.AuthConstant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FollowServiceTest {
    @Resource
    JavaMailSender javaMailSender;
    
    @Resource
    EmailService emailService;
    
    @Test
    public void test() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("welcome to tetto");
        message.setText("尊敬的zsheep6:\n" +
                "欢迎您注册Tetto，您的验证码为：123456，有效期为5分钟。");
        message.setTo("zsheep6@163.com");
        
        message.setFrom("Tetto用户注册<code_tetto@163.com>");
        javaMailSender.send(message);
    }
    
    @Test
    public void testTemplate() {
        boolean b = emailService.sendMail("zsheep6@163.com", "123456", AuthConstant.REGISTER, AuthConstant.REGISTER_SUBJECT);
        System.out.println(b);
    }
    
}