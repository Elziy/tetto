package com.elite.tetto.common.entity.vo;

import lombok.Data;

import java.util.Date;

/**
 * 添加图集信息
 *
 * @author 17610
 * @date 2022/10/04
 */
@Data
public class UpLoadAtlasVo {

    private Long author;
    private Date uploaddate;
    private Long collectionnum;
    private Long browsenum;
    private String title;
    private String introduction;
    private String thumbnail;
}
