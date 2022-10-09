package com.elite.tetto.image.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * 
 * @author Elziy
 * @email sunlightcs@gmail.com
 * @date 2022-10-07 14:39:01
 */
@Data
@TableName("img_imgs")
public class ImgsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 图片id
	 */
	@TableId
	private Long id;
	/**
	 * 图集id
	 */
	private Long atlasId;
	/**
	 * 图片url
	 */
	private String imgUrl;
	
	/**
	 * 宽度
	 */
	private Integer width;
	
	/**
	 * 高度
	 */
	private Integer height;

}
