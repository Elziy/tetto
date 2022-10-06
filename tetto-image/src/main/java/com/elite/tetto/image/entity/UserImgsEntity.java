package com.elite.tetto.image.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-10-04 17:23:12
 */
@Data
@TableName("img_user_imgs")
public class UserImgsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 图集id
	 */
	private Long imgsId;

}