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
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
    public void update(ReportsWordTemplateCategoryParam reportsWordTemplateCategoryParam) {
        List<CategoryAdd> addOrEditList = reportsWordTemplateCategoryParam.getAddOrEditList();
        List<Long> removeIdList = reportsWordTemplateCategoryParam.getRemoveIdList();

        if (CollectionUtils.isNotEmpty(addOrEditList)) {
            List<ReportsWordTemplateCategory> categoryList = addOrEditList.stream().map(category -> {
                ReportsWordTemplateCategory reportsWordTemplateCategory = new ReportsWordTemplateCategory();
                BeanUtils.copyProperties(category, reportsWordTemplateCategory);
                reportsWordTemplateCategory.setCreateTime(new Date());
                return reportsWordTemplateCategory;
            }).collect(Collectors.toList());
            saveOrUpdateBatch(categoryList);
        }
        if (CollectionUtils.isNotEmpty(removeIdList)) {
            removeByIds(removeIdList);
        }
    }
}
