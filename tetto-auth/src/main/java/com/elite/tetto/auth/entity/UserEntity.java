package com.elite.tetto.auth.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-10-04 14:32:08
 */
@Data
@TableName("usr_user")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户id
	 */
	@TableId
	private Long id;
	/**
	 * 用户名
	 */
	@NotEmpty(message = "用户名不能为空")
	private String username;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 性别
	 */
	private String sex;
	/**
	 * 生日
	 */
	private Date birthday;
	/**
	 * 头像
	 */
	private String header;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 个人介绍
	 */
	private String introduce;

}
