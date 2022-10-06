package com.elite.tetto.upload.util;

import java.util.UUID;

public class FileNameUtil {
    /**
     * 根据UUID生成文件名
     * @return 随机文件名
     */
    public static String getUUIDFileName() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }
    
    /**
     * 根据给定的文件名截取文件类型
     * @param fileName 文件名
     * @return 文件后缀
     */
    public static String getFileType(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index == -1) {
            return ".jpg";
        }
        return fileName.substring(index);
    }
}
