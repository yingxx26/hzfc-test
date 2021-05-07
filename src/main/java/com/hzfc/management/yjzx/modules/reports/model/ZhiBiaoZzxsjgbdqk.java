package com.hzfc.management.yjzx.modules.reports.model;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 指标
 * </p>
 *
 * @author yxx
 * @since 2020-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("HZ_TEST.ODS_PY_ZZXSJGBDQK_MM")
@ApiModel(value = "ODS_PY_ZZXSJGBDQK_MM指标", description = "指标")
public class ZhiBiaoZzxsjgbdqk implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty(value = "标题")
    @TableField("TITLE")
    private Date title;

    @ApiModelProperty(value = "面积")
    @TableField("AREA")
    private String area;

    @ApiModelProperty(value = "城市")
    @TableField("CITY")
    private String city;

    @ApiModelProperty(value = "环比")
    @TableField("MOM")
    private Long mom;

    @ApiModelProperty(value = "同比")
    @TableField("YOY")
    private Long yoy;

    @ApiModelProperty(value = "定基")
    @TableField("FIXEDBASE")
    private Long FIXEDBASE;

    @ApiModelProperty(value = "类别")
    @TableField("LB")
    private String lb;

    @ApiModelProperty(value = "爬取时间")
    @TableField("SPIDERTIME")
    private Date spidertime;
}
