package com.hzfc.management.yjzx.modules.reports.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzfc.management.yjzx.modules.reports.dto.ReportsWordTemplateCategoryParam;
import com.hzfc.management.yjzx.modules.reports.model.ReportsWordTemplateCategory;

import java.util.List;

/**
 * 后台管理员管理Service
 * Created by hzfc on 2018/4/26.
 */
public interface ReportsWordTemplateCategoryService extends IService<ReportsWordTemplateCategory> {


    void update(ReportsWordTemplateCategoryParam reportsWordTemplateCategoryParam);

}
