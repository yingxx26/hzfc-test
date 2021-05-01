package com.hzfc.management.yjzx.modules.reports.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzfc.management.yjzx.modules.reports.dto.CategoryAdd;
import com.hzfc.management.yjzx.modules.reports.dto.ReportsWordTemplateCategoryParam;
import com.hzfc.management.yjzx.modules.reports.mapper.ReportsWordTemplateCategoryMapper;
import com.hzfc.management.yjzx.modules.reports.model.ReportsWordTemplateCategory;
import com.hzfc.management.yjzx.modules.reports.service.ReportsWordTemplateCategoryService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
