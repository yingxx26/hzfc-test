package com.hzfc.management.wyzj.utils.fileutils;

/**
 * @Author yxx
 * @Date 2021/4/27 14:21
 */

import com.deepoove.poi.XWPFTemplate;
import com.hzfc.management.wyzj.common.exception.ApiException;
import org.springframework.util.ClassUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;

/**
 * 删除文件和目录
 */
public class SaveFileUtil {

    /**
     * @param params
     * @param templatePath 模板路径
     * @param tempfilePath 临时文件
     * @return
     * @throws IOException
     */
    public static String savePoiFile(Map<String, Object> params, String templatePath, String tempfilePath) throws IOException {

        XWPFTemplate template = XWPFTemplate.compile(templatePath).render(params);
        ;//生成临时文件存放地址
        //生成文件名
        Long time = new Date().getTime();
        // 生成的word格式
        String formatSuffix = ".docx";
        // 拼接后的文件名
        String fileName = time + formatSuffix;//文件名  带后缀
        String fullpath = tempfilePath + fileName;
        FileOutputStream fos = new FileOutputStream(fullpath);
        template.write(fos);
        //=================生成word到设置浏览默认下载地址=================
        /*// 设置强制下载不打开
        response.setContentType("application/force-download");
        // 设置文件名
        response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);*/
        fos.flush();
        fos.close();
        template.close();
        return fileName;
    }


}
