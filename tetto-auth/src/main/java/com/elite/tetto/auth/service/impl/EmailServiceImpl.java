package com.elite.tetto.auth.service.impl;

import com.elite.tetto.auth.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;

/**
 * @author Jie
 */
@Service
public class EmailServiceImpl implements EmailService {
    
    @Resource
    private JavaMailSender javaMailSender;
    
    /**
     * 邮件发件人
     */
    @Value("${mail.fromMail.fromAddress}")
    private String fromAddress;
    
    @Resource
    TemplateEngine templateEngine;
    
    
    @Override
    public boolean sendMail(String mail, String code, String operate, String subject) {
        //创建邮件正文
        Context context = new Context();
        context.setVariable("Code", Arrays.asList(code.split("")));
        context.setVariable("operate", operate);
        
        //将模块引擎内容解析成html字符串
        String emailContent = templateEngine.process("EmailVerificationCode", context);
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromAddress);
            helper.setTo(mail);
            helper.setSubject(subject);
            helper.setText(emailContent, true);
            javaMailSender.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
