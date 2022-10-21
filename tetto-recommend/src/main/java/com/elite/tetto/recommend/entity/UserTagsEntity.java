package com.elite.tetto.recommend.entity;

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
 * @date 2022-10-21 19:57:54
 */
@Data
@TableName("rec_user_tags")
public class UserTagsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 用户id
	 */
	private Long uid;
	/**
	 * 推荐标签
	 */
	private String tag;
	/**
	 * 推荐标签数量
	 */
	private Long count;

}
