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
@TableName("HZ_TEST.DWS_GY_DWDXX_CQ_DF")
@ApiModel(value = "DWS_GY_DWDXX_CQ_DF指标", description = "商品房供应多维度指标")
public class ZhiBiaoGyDwDxxCqDf implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "统计时间")
    @TableField("TJDATE")
    private String tjdate;

    @ApiModelProperty(value = "城区ID")
    @TableField("CQID")
    private String cqid;

    @ApiModelProperty(value = "城区名称")
    @TableField("CQMC")
    private String cqmc;


    @ApiModelProperty(value = "均价区间")
    @TableField("JJQJ")
    private String jjqj;

    @ApiModelProperty(value = "面积区间")
    @TableField("MJQJ")
    private String mjqj;

    @ApiModelProperty(value = "房屋用途")
    @TableField("FWYT")
    private String fwyt;

    @ApiModelProperty(value = "本月供应套数")
    @TableField("GY_TS_CNT_TM_CQID")
    private Long gyTsCntTmCqid;

    @ApiModelProperty(value = "本月供应面积")
    @TableField("GY_MJ_AMT_TM_CQID")
    private Double gyMjAmtTmCqid;

    @ApiModelProperty(value = "本月供应金额")
    @TableField("GY_JE_AMT_TM_CQID")
    private Double gyJeAmtTmCqid;

    @ApiModelProperty(value = "本年供应套数")
    @TableField("GY_TS_CNT_TY_CQID")
    private Long gyTsCntTyCqid;

    @ApiModelProperty(value = "本年供应面积")
    @TableField("GY_MJ_AMT_TY_CQID")
    private Double gyMjAmtTyCqid;

    @ApiModelProperty(value = "本年供应金额")
    @TableField("GY_JE_AMT_TY_CQID")
    private Double gyJeAmtTyCqid;
}
