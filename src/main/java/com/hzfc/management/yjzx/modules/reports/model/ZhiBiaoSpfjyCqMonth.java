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
@TableName("HZ_TEST.DWS_YBTJ_SPFJY_CQ_MONTH")
@ApiModel(value = "DWS_YBTJ_SPFJY_CQ_MONTH指标", description = "商品房成交指标按城区月份")
public class ZhiBiaoSpfjyCqMonth implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "统计时间")
    @TableField("TJSJ")
    private String tjsj;

    @ApiModelProperty(value = "城区ID")
    @TableField("CQID")
    private String cqid;

    @ApiModelProperty(value = "城区名称")
    @TableField("CQMC")
    private String cqmc;

    @ApiModelProperty(value = "可售面积")
    @TableField("SPFJY_MJ_AMT_EYD_CQ")
    private Double spfjyMjAmtEydCq;

    @ApiModelProperty(value = "本月交易量")
    @TableField("SPFJY_TS_CNT_TM_CQ")
    private Long spfjyTsCntTmCq;

    @ApiModelProperty(value = "成交量增速")
    @TableField("SPFJY_CS_AMT_TM_CQ")
    private Double spfjyCsAmtTmCq;

    @ApiModelProperty(value = "去化周期")
    @TableField("SPFJY_QHZQ_AMT_TM_CQ")
    private Double spfjyQhzqAmtTmCq;

    @ApiModelProperty(value = "住宅可售面积")
    @TableField("SPFJY_MJ_AMT_ZZ_EYD_CQ")
    private Double spfjyMjAmtZzEydCq;

    @ApiModelProperty(value = "住宅本月交易量")
    @TableField("SPFJY_TS_CNT_ZZ_TM_CQ")
    private Long spfjyTsCntZzTmCq;

    @ApiModelProperty(value = "住宅成交量增速")
    @TableField("SPFJY_CS_AMT_ZZ_TM_CQ")
    private Double spfjyCsAmtZzTmCq;

    @ApiModelProperty(value = "住宅去化周期")
    @TableField("SPFJY_QHZQ_AMT_ZZ_TM_CQ")
    private Double spfjyQhzqAmtZzTmCq;

    @ApiModelProperty(value = "非主宅可售面积")
    @TableField("SPFJY_MJ_AMT_FZZ_EYD_CQ")
    private Double spfjyMjAmtFzzEydCq;

    @ApiModelProperty(value = "非住宅本月交易量")
    @TableField("SPFJY_TS_CNT_FZZ_TM_CQ")
    private Long spfjyTsCntFzzTmCq;

    @ApiModelProperty(value = "非住宅成交量增速")
    @TableField("SPFJY_CS_AMT_FZZ_TM_CQ")
    private Double spfjyCsAmtFzzTmCq;

    @ApiModelProperty(value = "非住宅去化周期")
    @TableField("SPFJY_QHZQ_AMT_FZZ_TM_CQ")
    private Double spfjyQhzqAmtFzzTmCq;

}
