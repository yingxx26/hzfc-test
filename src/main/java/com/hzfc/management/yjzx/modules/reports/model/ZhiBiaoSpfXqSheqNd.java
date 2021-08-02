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
@TableName("HZ_TEST.DWS_SPFHT_XQ_SHEQ_ND")
@ApiModel(value = "DWS_SPFHT_XQ_SHEQ_ND指标", description = "商品房小区成交套数")
public class ZhiBiaoSpfXqSheqNd implements Serializable {


    @ApiModelProperty(value = "统计时间")
    @TableField("BIZDATE")
    private String bizDate;

    @ApiModelProperty(value = "城区ID")
    @TableField("DISTRICTID")
    private Long districtid;

    @ApiModelProperty(value = "城区名称")
    @TableField("DISTRICT")
    private String district;

    @ApiModelProperty(value = "小区ID")
    @TableField("XQID")
    private String xqid;

    @ApiModelProperty(value = "小区名称")
    @TableField("XQMC")
    private String xqmc;

    @ApiModelProperty(value = "社区ID")
    @TableField("COMMUNITID")
    private String communitid;

    @ApiModelProperty(value = "社区名称")
    @TableField("COMMUNITNAME")
    private String communitname;

    @ApiModelProperty(value = "本月商品房成交套数")
    @TableField("SPFHT_TS_CNT_TM_CQID")
    private Long spfhtTsCntTmCqid;

    @ApiModelProperty(value = "本年商品房成交套数")
    @TableField("SPFHT_TS_CNT_TY_CQID")
    private Long spfhtTsCntTyCqid;

    @ApiModelProperty(value = "本月商品房成交面积")
    @TableField("SPFHT_MJ_AMT_TM_CQID")
    private Double spfhtMjAmtTmCqid;

    @ApiModelProperty(value = "本年商品房成交面积")
    @TableField("SPFHT_MJ_AMT_TY_CQID")
    private Double spfhtMjAmtTyCqid;

    @ApiModelProperty(value = "本月商品房成交金额")
    @TableField("SPFHT_JE_AMT_TM_CQID")
    private Double spfhtJeAmtTmCqid;

    @ApiModelProperty(value = "本年商品房成交金额")
    @TableField("SPFHT_JE_AMT_TY_CQID")
    private Double spfhtJeAmtTyCqid;

    @ApiModelProperty(value = "本月商品房成交套数-住宅")
    @TableField("SPFHT_TS_CNT_ZZ_TM_CQID")
    private Long spfhtTsCntZzTmCqid;

    @ApiModelProperty(value = "本年商品房成交套数-住宅")
    @TableField("SPFHT_TS_CNT_ZZ_TY_CQID")
    private Long spfhtTsCntZzTyCqid;

    @ApiModelProperty(value = "本月商品房成交面积-住宅")
    @TableField("SPFHT_MJ_AMT_ZZ_TM_CQID")
    private Double spfhtMjAmtZzTmCqid;

    @ApiModelProperty(value = "本年商品房成交面积-住宅")
    @TableField("SPFHT_MJ_AMT_ZZ_TY_CQID")
    private Double spfhtMjAmtZzTyCqid;

    @ApiModelProperty(value = "本月商品房成交金额-住宅")
    @TableField("SPFHT_JE_AMT_ZZ_TM_CQID")
    private Double spfhtJeAmtZzTmCqid;

    @ApiModelProperty(value = "本年商品房成交金额-住宅")
    @TableField("SPFHT_JE_AMT_ZZ_TY_CQID")
    private Double spfhtJeAmtZzTyCqid;

    @ApiModelProperty(value = "本月商品房成交均价-住宅")
    @TableField("SPFHT_PRICE_AVG_ZZ_TM_CQID")
    private Double spfhtPriceAvgZzTmCqid;

    @ApiModelProperty(value = "本年商品房成交均价-住宅")
    @TableField("SPFHT_PRICE_AVG_ZZ_TY_CQID")
    private Double spfhtPriceAvgZzTyCqid;

    @ApiModelProperty(value = "数据来源（关联ts_dic_flag）")
    @TableField("FLAG")
    private Integer flag;

    @ApiModelProperty(value = "板块ID")
    @TableField("BKID")
    private String bkid;

    @ApiModelProperty(value = "板块名称")
    @TableField("BKMC")
    private String bkmc;

    @ApiModelProperty(value = "地址库社区")
    @TableField("COMMUNITNAME_DZK")
    private String communitnameDzk;

    @ApiModelProperty(value = "地址库城区")
    @TableField("DISTRICT_DZK")
    private String districtDzk;
}
