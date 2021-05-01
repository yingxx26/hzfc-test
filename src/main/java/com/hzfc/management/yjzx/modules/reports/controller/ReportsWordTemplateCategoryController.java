package com.hzfc.management.yjzx.modules.reports.controller;

import com.hzfc.management.yjzx.common.api.CommonResult;
import com.hzfc.management.yjzx.modules.reports.dto.ReportsWordTemplateCategoryParam;
import com.hzfc.management.yjzx.modules.reports.model.ReportsWordTemplateCategory;
import com.hzfc.management.yjzx.modules.reports.service.ReportsWordTemplateCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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
        return CommonResult.success(null);
    }

}
