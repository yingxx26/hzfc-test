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
@TableName("HZ_TEST.DWS_YBTJ_PZYS_CQ")
@ApiModel(value = "DWS_YBTJ_PZYS_CQ指标", description = "商品房批准预售")
public class ZhiBiaoSpfPzYs implements Serializable {


    @ApiModelProperty(value = "统计时间")
    @TableField("TJSJ")
    private String tjsj;

    @ApiModelProperty(value = "指标名")
    @TableField("ZBNAME")
    private String zbname;

    @ApiModelProperty(value = "全市本月zbz")
    @TableField("YS_PZYS_ZBZ_TM_ALL")
    private Double ysPzysZbzTmAll;

    @ApiModelProperty(value = "全市去年同期指标值")
    @TableField("YS_PZYS_ZBZ_TM_LASTY_ALL")
    private Double ysPzysZbzTmLastyAll;

    @ApiModelProperty(value = "全市本年zbz")
    @TableField("YS_PZYS_ZBZ_TY_ALL")
    private Double ysPzysZbzTyAll;

    @ApiModelProperty(value = "全市去年指标值")
    @TableField("YS_PZYS_ZBZ_TY_LASTY_ALL")
    private Double ysPzysZbzTyLastyAll;

    @ApiModelProperty(value = "市区本月zbz")
    @TableField("YS_PZYS_ZBZ_TM_SQ")
    private Double ysPzysZbzTmSq;

    @ApiModelProperty(value = "市区去年同期指标值")
    @TableField("YS_PZYS_ZBZ_TM_LASTY_SQ")
    private Double ysPzysZbzTmLastySq;

    @ApiModelProperty(value = "市区本年zbz")
    @TableField("YS_PZYS_ZBZ_TY_SQ")
    private Double ysPzysZbzTySq;

    @ApiModelProperty(value = "市区去年指标值")
    @TableField("YS_PZYS_ZBZ_TY_LASTY_SQ")
    private Double ysPzysZbzTyLastySq;

}
