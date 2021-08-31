package com.hzfc.management.yjzx.modules.reports.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("HZ_TEST.DWS_CLFHT_RDXQ_CQ_DF")
@ApiModel(value = "DWS_CLFHT_RDXQ_CQ_DF指标", description = "热点小区")
public class ZhiBiaoClfRdxqCqDf implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "合同备案时间")
    @TableField("HTBASJ")
    private Date htbasj;

    @ApiModelProperty(value = "维度名称")
    @TableField("WDNAME")
    private String wdname;

    @ApiModelProperty(value = "城区id")
    @TableField("CSDN_CQID")
    private String csdnCqid;

    @ApiModelProperty(value = "城区名称")
    @TableField("CSDN_XZQH")
    private String csdnXzqh;

    @ApiModelProperty(value = "面积区间")
    @TableField("SQNAME")
    private String sqname;

    @ApiModelProperty(value = "小区id")
    @TableField("XQID")
    private String xqid;

    @ApiModelProperty(value = "小区名称")
    @TableField("XQMC")
    private String xqmc;

    @ApiModelProperty(value = "房屋用途")
    @TableField("FWYT")
    private String fwyt;

    @ApiModelProperty(value = "二手房套数")
    @TableField("CLFHT_TS_CNT_CQID")
    private Double clfhtTsCntCqid;

    @ApiModelProperty(value = "二手房面积")
    @TableField("CLFHT_MJ_AMT_CQID")
    private Double clfhtMjAmtCqid;

    @ApiModelProperty(value = "二手房金额")
    @TableField("CLFHT_JE_AMT_CQID")
    private Double clfhtJeAmtCqid;

}
