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
 * @date 2022-10-11 20:25:39
 */
@Data
@TableName("img_like")
public class LikeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 用户id
	 */
	private Long uid;
	/**
	 * 图集id
	 */
	private Long aid;
	
	/**
	 * 收藏日期
	 */
	private Date date;

}
