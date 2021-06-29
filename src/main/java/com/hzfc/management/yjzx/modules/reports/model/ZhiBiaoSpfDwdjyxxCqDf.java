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
@TableName("HZ_TEST.DWS_SPFHT_DWDJYXX_CQ_DF")
@ApiModel(value = "DWS_SPFHT_DWDJYXX_CQ_DF指标", description = "商品房多维度指标")
public class ZhiBiaoSpfDwdjyxxCqDf implements Serializable {

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

    @ApiModelProperty(value = "房产性质")
    @TableField("FCXZ")
    private String fcxz;

    @ApiModelProperty(value = "均价区间")
    @TableField("JJQJ")
    private String jjqj;

    @ApiModelProperty(value = "面积区间")
    @TableField("MJQJ")
    private String mjqj;

    @ApiModelProperty(value = "房屋用途")
    @TableField("FWYT")
    private String fwyt;

    @ApiModelProperty(value = "本月商品房合同签订套数")
    @TableField("SPFHT_TS_CNT_TM_CQID")
    private Double spfhtTsCntTmCqid;

    @ApiModelProperty(value = "本月商品房合同签订面积")
    @TableField("SPFHT_MJ_AMT_TM_CQID")
    private Double spfhtMjAmtTmCqid;

    @ApiModelProperty(value = "本月商品房合同签订金额")
    @TableField("SPFHT_JE_AMT_TM_CQID")
    private Double spfhtJeAmtTmCqid;

    @ApiModelProperty(value = "本年商品房合同签订套数")
    @TableField("SPFHT_TS_CNT_TY_CQID")
    private Double spfhtTsCntTyCqid;

    @ApiModelProperty(value = "本年商品房合同签订面积")
    @TableField("SPFHT_MJ_AMT_TY_CQID")
    private Double spfhtMjAmtTyCqid;

    @ApiModelProperty(value = "本年商品房合同签订金额")
    @TableField("SPFHT_JE_AMT_TY_CQID")
    private Double spfhtJeAmtTyCqid;
}
