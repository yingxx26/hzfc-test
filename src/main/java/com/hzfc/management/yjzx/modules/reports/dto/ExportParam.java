package com.hzfc.management.yjzx.modules.reports.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * word模板分类表
 * </p>
 *
 * @author yxx
 * @since 2020-08-21
 */
@Getter
@Setter
public class ExportParam {


    @ApiModelProperty(value = "时间")
    private List<Date> dates;


}
