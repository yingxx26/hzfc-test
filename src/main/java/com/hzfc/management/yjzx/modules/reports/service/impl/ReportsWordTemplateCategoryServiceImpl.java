package com.hzfc.management.yjzx.modules.reports.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzfc.management.yjzx.common.exception.ApiException;
import com.hzfc.management.yjzx.modules.reports.dto.CategoryAdd;
import com.hzfc.management.yjzx.modules.reports.dto.ReportsWordTemplateCategoryParam;
import com.hzfc.management.yjzx.modules.reports.mapper.ReportsWordTemplateCategoryMapper;
import com.hzfc.management.yjzx.modules.reports.mapper.ReportsWordTemplateMapper;
import com.hzfc.management.yjzx.modules.reports.model.ReportsWordTemplate;
import com.hzfc.management.yjzx.modules.reports.model.ReportsWordTemplateCategory;
import com.hzfc.management.yjzx.modules.reports.service.ReportsWordTemplateCategoryService;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台管理员管理Service实现类
 * Created by hzfc on 2018/4/26.
 */
@Service
public class ReportsWordTemplateCategoryServiceImpl extends ServiceImpl<ReportsWordTemplateCategoryMapper, ReportsWordTemplateCategory> implements ReportsWordTemplateCategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportsWordTemplateCategoryServiceImpl.class);


    @Override
    @Transactional
    public boolean update(ReportsWordTemplateCategoryParam reportsWordTemplateCategoryParam) {
        List<CategoryAdd> addOrEditList = reportsWordTemplateCategoryParam.getAddOrEditList();
        List<CategoryAdd> addList = addOrEditList.stream().filter(category -> category.getId() == null).collect(Collectors.toList());
        List<CategoryAdd> editList = addOrEditList.stream().filter(t -> !addList.contains(t)).collect(Collectors.toList());

        List<Long> removeIdList = reportsWordTemplateCategoryParam.getRemoveIdList();

        return false;
    }
}
