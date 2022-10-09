package com.elite.tetto.image.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class UploadAtlasVo {
    private List<ImgUrl> imgUrls;
    private String introduce;
    private int isPublic;
    private List<String> tags;
    private String thumbnailUrl;
    private String title;
}
