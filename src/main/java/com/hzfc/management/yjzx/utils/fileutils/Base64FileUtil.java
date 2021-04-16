package com.hzfc.management.yjzx.utils.fileutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/**
 * @Author yxx
 * @Date 2021/4/16 15:54
 */
public class Base64FileUtil {
    /**
     * fileToBase64:(将文件转成base64 字符串).
     *
     * @param path 文件路径
     * @return Base64字符串
     * @throws Exception
     */
    public static String fileToBase64(String path) throws Exception {
        String base64 = null;
        InputStream in = null;
        try {
            File file = new File(path);
            in = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            in.read(bytes);
            base64 = Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return base64;
    }
}
