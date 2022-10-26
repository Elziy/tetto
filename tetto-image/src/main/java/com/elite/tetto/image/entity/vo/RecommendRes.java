package com.elite.tetto.image.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class RecommendRes {
    private List<AtlasRes> atlas;
    
    private List<String> tags;
}
