package com.elite.tetto.image.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 阿特拉斯实体
 *
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-10-04 17:23:12
 */
@Data
@TableName("img_atlas")
public class AtlasEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 作者
	 */
	private Long author;
	/**
	 * 上传日期
	 */
	private Date uploaddate;
	/**
	 * 收藏数
	 */
	private Long collectionnum;
	/**
	 * 浏览数
	 */
	private Long browsenum;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 介绍
	 */
	private String introduction;
	/**
	 * 缩略图链接
	 */
	private String thumbnail;

}
