package com.elite.tetto.image.entity.vo;

import com.elite.tetto.common.entity.vo.es.ImgUrl;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class UploadAtlasVo {
    
    @Size(min = 1, max = 10, message = "作品应该在1-10个之间")
    private List<ImgUrl> imgUrls;
    
    private String introduce;
    
    @NotNull(message = "是否公开不能为空")
    private int isPublic;
    
    @Size(min = 1, max = 10, message = "标签数量必须在1-10之间")
    private List<String> tags;
    
    @URL(message = "封面图必须为合法url地址")
    private String thumbnailUrl;
    
    @NotBlank(message = "图集名称不能为空")
    private String title;
    
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    @URL(message = "用户头像必须为合法url地址")
    private String avatar;
}
