package com.hzfc.management.yjzx.modules.reports.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzfc.management.yjzx.common.exception.ApiException;
import com.hzfc.management.yjzx.modules.reports.mapper.ReportsWordTemplateMapper;
import com.hzfc.management.yjzx.modules.reports.model.ReportsWordTemplate;
import com.hzfc.management.yjzx.modules.reports.service.ReportsWordTemplateService;
import com.hzfc.management.yjzx.utils.fileutils.Base64FileUtil;
import com.hzfc.management.yjzx.utils.fileutils.DeleteFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 后台管理员管理Service实现类
 * Created by hzfc on 2018/4/26.
 */
@Service
public class ReportsWordTemplateServiceImpl extends ServiceImpl<ReportsWordTemplateMapper, ReportsWordTemplate> implements ReportsWordTemplateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportsWordTemplateServiceImpl.class);
    // 将 yml 中的自定义配置注入到这里
    @Value("${hzfc.uploadfile.wordTemplate.path}")
    private String filePath;

    @Override
    @Transactional
    public boolean create(ReportsWordTemplate reportsWordTemplate, String wordBase64) {

        ZonedDateTime now = ZonedDateTime.now();
        // ISO_LOCAL_DATE 2020-03-25
        String format1 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(now);
        // 时间 和 日期拼接
        String newFileName = "WT" + format1 + ".docx";
        String path = filePath + newFileName;
        // 得到文件保存的位置以及新文件名
        try {
            File dest = new File(path);
            MultipartFile multipartFile = Base64FileUtil.base64ToMultipart(wordBase64);
            if (!dest.exists()) {
                //先得到文件的上级目录，并创建上级目录，在创建文件
                dest.getParentFile().mkdirs();
                //创建文件
                dest.createNewFile();
            }
            // 上传的文件被保存了
            multipartFile.transferTo(dest);
            // 打印日志
            LOGGER.info("上传成功，当前上传的文件保存在 {}", path);
        } catch (IOException e) {
            LOGGER.error(e.toString());
            throw new ApiException("上传异常");
        }
        reportsWordTemplate.setTemplatepath(path);
        return save(reportsWordTemplate);
    }

    @Override
    public Page<ReportsWordTemplate> list(String keyword, Integer pageSize, Integer pageNum) {
        Page<ReportsWordTemplate> page = new Page<>(pageNum, pageSize);
        QueryWrapper<ReportsWordTemplate> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<ReportsWordTemplate> lambda = wrapper.lambda();
        if (StrUtil.isNotEmpty(keyword)) {
            lambda.like(ReportsWordTemplate::getCreateuser, keyword);
            lambda.or().like(ReportsWordTemplate::getUsepurpose, keyword);
        }
        return page(page, wrapper);
    }

    @Override
    public boolean update(Long id, ReportsWordTemplate reportsWordTemplate) {
        reportsWordTemplate.setId(id);
        boolean success = updateById(reportsWordTemplate);
        return success;
    }

    @Override
    public boolean delete(Long id) {
        ReportsWordTemplate reportsWordTemplate = getById(id);
        DeleteFileUtil.delete(reportsWordTemplate.getTemplatepath());
        boolean success = removeById(id);
        return success;
    }

}
