package com.hzfc.management.yjzx.modules.reports.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 用户登录参数
 * Created by hzfc on 2018/4/26.
 */
@Getter
@Setter
public class ReportsWordTemplateCategoryParam {

    private List<CategoryAdd> addOrEditList;

    private List<Long> removeIdList;
}
