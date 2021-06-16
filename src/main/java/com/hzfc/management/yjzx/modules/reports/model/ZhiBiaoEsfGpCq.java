package com.hzfc.management.yjzx.modules.reports.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

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
@TableName("HZ_TEST.DWS_YBTJ_ESFGP_CQ")
@ApiModel(value = "DWS_YBTJ_ESFGP_CQ指标", description = "二手房挂牌指标")
public class ZhiBiaoEsfGpCq implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "统计时间")
    @TableField("TJSJ")
    private String tjsj;

    @ApiModelProperty(value = "指标名")
    @TableField("ZBNAME")
    private String zbname;

    @ApiModelProperty(value = "全市本月zbz")
    @TableField("ESF_GP_ZBZ_TM_ALL")
    private Double esfGpZbzTmAll;

    @ApiModelProperty(value = "全市去年指标值")
    @TableField("ESF_GP_ZBZ_TY_LASTY_ALL")
    private Double esfGpZbzTyLastyAll;

    @ApiModelProperty(value = "市区本月zbz")
    @TableField("ESF_GP_ZBZ_TM_SQ")
    private Double esfGpZbzTmSq;

    @ApiModelProperty(value = "市区去年指标值")
    @TableField("ESF_GP_ZBZ_TY_LASTY_SQ")
    private Double esfGpZbzTyLastySq;

    @ApiModelProperty(value = "去化周期")
    @TableField("ESF_GP_ZBZ_QHZQ")
    private Double esfFpZbzQhzq;



}
