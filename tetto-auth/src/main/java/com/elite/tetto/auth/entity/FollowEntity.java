package com.elite.tetto.auth.entity;

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
 * @date 2022-10-04 14:32:08
 */
@Data
@TableName("follow")
public class FollowEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 关注者id
	 */
	private Long uid;
	/**
	 * 被关注者id
	 */
	private Long fid;

}
