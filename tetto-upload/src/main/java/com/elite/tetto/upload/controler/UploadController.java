package com.elite.tetto.upload.controler;

import com.elite.tetto.upload.entity.R;
import com.elite.tetto.upload.util.FileNameUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@RestController
@CrossOrigin
public class UploadController {
    
    public String path = "/usr/local/nginx/html/";
    
    public static String host = "http://static.tetto.com/";
    
    @PostMapping("/upload")
    public R upload(MultipartFile file) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String format = sdf.format(date);
        String filePath = path + format;
        String fileName = FileNameUtil.getUUIDFileName() + FileNameUtil.getFileType(Objects.requireNonNull(file.getOriginalFilename()));
        String url = host + format + "/" + fileName;
        File dest = new File(filePath);
        try {
            if (!dest.exists()) {
                boolean b = dest.mkdirs();
            }
            file.transferTo(new File(filePath + "/" + fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return new R(500, "上传失败", null);
        }
        return new R(200, "上传成功", url);
    }
}
