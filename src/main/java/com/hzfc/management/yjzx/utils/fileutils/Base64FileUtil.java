package com.hzfc.management.yjzx.utils.fileutils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.core.log.LogFormatUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.*;
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

    public static MultipartFile base64ToMultipart(String base64) throws IOException {

        String[] baseStr = base64.split(",");
        String type = null;
        String content = null;
        if (baseStr.length < 2) {
            type = "data:application/vnd.openxmlformats-officedocument.wordprocessingml.document;base64";
            content = baseStr[0];
        } else {
            type = baseStr[0];
            content = baseStr[1];
        }
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b = new byte[0];
        b = decoder.decodeBuffer(content);
        for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {
                b[i] += 256;
            }
        }
        return new Base64DecodedMultipartFile(b, type);

    }

    public static class Base64DecodedMultipartFile implements MultipartFile {
        private byte[] imgContent;
        private String header;

        public Base64DecodedMultipartFile(byte[] imgContent, String header) {
            this.imgContent = imgContent;
            this.header = header.split(";")[0];
        }

        @Override
        public String getName() {
            return System.currentTimeMillis() + Math.random() + "." + header.split("/")[1];
        }

        @Override
        public String getOriginalFilename() {
            return System.currentTimeMillis() + (int) Math.random() * 10000 + "." + header.split("/")[1];
        }

        @Override
        public String getContentType() {
            return header.split(":")[1];
        }

        @Override
        public boolean isEmpty() {
            return imgContent == null || imgContent.length == 0;
        }

        @Override
        public long getSize() {
            return imgContent.length;
        }

        @Override
        public byte[] getBytes() throws IOException {
            return imgContent;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(imgContent);
        }

        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {
            FileOutputStream fileOutputStream = new FileOutputStream(dest);
            try {
                fileOutputStream.write(imgContent);
            } finally {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            }
        }

    }

}
