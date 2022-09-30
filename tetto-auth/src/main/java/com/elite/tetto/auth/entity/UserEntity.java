package com.elite.tetto.auth.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-09-30 20:52:00
 */
@Data
@TableName("usr_user")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户id
     */
    @TableId
    private Long usrId;
    /**
     * 用户名
     */
    private String usrUsername;
    /**
     * 密码
     */
    private String usrPassword;
    /**
     * 性别
     */
    private String usrSex;
    /**
     * 生日
     */
    private Date usrBirthday;
    /**
     * 头像
     */
    private String usrHeader;
    /**
     * 邮箱
     */
    private String usrEmail;
    
}
