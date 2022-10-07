package com.elite.tetto.image.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-10-07 14:39:01
 */
@Data
@TableName("img_atlas")
public class AtlasEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 图集id
	 */
	@TableId
	private Long id;
	
	/**
	 * 作者id
	 */
	private Long uId;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 介绍
	 */
	private String introduce;
	/**
	 * 是否可以展示
	 */
	private Integer isPublic;
	/**
	 * 缩略图url
	 */
	private String thumbnailUrl;
	/**
	 * 上传日期
	 */
	private Date date;

}
