package com.hzfc.management.yjzx.modules.reports.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzfc.management.yjzx.common.api.CommonPage;
import com.hzfc.management.yjzx.common.api.CommonResult;
import com.hzfc.management.yjzx.modules.reports.dto.ReportsWordTemplateCategoryParam;
import com.hzfc.management.yjzx.modules.reports.dto.ReportsWordTemplateParam;
import com.hzfc.management.yjzx.modules.reports.model.ReportsWordTemplate;
import com.hzfc.management.yjzx.modules.reports.model.ReportsWordTemplateCategory;
import com.hzfc.management.yjzx.modules.reports.service.ReportsWordTemplateCategoryService;
import com.hzfc.management.yjzx.modules.reports.service.ReportsWordTemplateService;
import com.hzfc.management.yjzx.modules.ums.service.UmsAdminService;
import com.hzfc.management.yjzx.utils.fileutils.Base64FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * word模板分类管理
 * Created by hzfc on 2018/4/26.
 */
@Controller
@Api(tags = "ReportsWordTemplateCategoryController", description = "word模板分类管理")
@RequestMapping("reports/wordtemplateCategory")
public class ReportsWordTemplateCategoryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportsWordTemplateCategoryController.class);


    @Autowired
    private ReportsWordTemplateCategoryService reportsWordTemplateCategoryService;

    @ApiOperation("获取分类")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<ReportsWordTemplateCategory>> list() {
        List<ReportsWordTemplateCategory> reportsWordTemplateCategoryList = reportsWordTemplateCategoryService.list();
        return CommonResult.success(reportsWordTemplateCategoryList);
    }


    @ApiOperation("修改分类")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@RequestBody ReportsWordTemplateCategoryParam reportsWordTemplateCategoryParam) {

        reportsWordTemplateCategoryService.update(reportsWordTemplateCategoryParam);
        return CommonResult.success("");
    }

}
