package com.elite.tetto.upload.util;

import org.apache.tomcat.util.codec.binary.Base64;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Base64Util {
    // 对字节数组字符串进行Base64解码并生成图片
    //imgFilePath 待保存的本地路径
    public static void GenerateImage(String base64Str, String imgFilePath) {
        if (base64Str == null) // 图像数据为空
            return;
        try {
            // Base64解码
            byte[] bytes = Base64.decodeBase64(base64Str);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            OutputStream out = Files.newOutputStream(Paths.get(imgFilePath));
            out.write(bytes);
            out.flush();
            out.close();
            //====
        } catch (Exception e) {
        }
    }
}

