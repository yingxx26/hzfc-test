package com.hzfc.management.yjzx.modules.reports.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

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
@TableName("HZ_TEST.DWS_YBTJ_SPFJY_ZH_CQ")
@ApiModel(value = "DWS_YBTJ_SPFJY_ZH_CQ指标", description = "商品房综合成交指标")
public class ZhiBiaoSpfjyZhCq implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "统计时间")
    @TableField("TJSJ")
    private String tjsj;

    @ApiModelProperty(value = "指标名")
    @TableField("ZBNAME")
    private String zbname;

    @ApiModelProperty(value = "累计指标")
    @TableField("SPFJY_ZBZ_TY_CQ")
    private BigDecimal spfjyZbzTyCq;

    @ApiModelProperty(value = "同比增长")
    @TableField("SPFJY_ZBZ_TY_CQ_TB")
    private BigDecimal spfjyZbzTyCqTb;

    @ApiModelProperty(value = "住宅累计指标")
    @TableField("SPFJY_ZBZ_ZZ_TY_CQ")
    private BigDecimal spfjyZbzZzTyCq;

    @ApiModelProperty(value = "住宅同比增长")
    @TableField("SPFJY_ZBZ_ZZ_TY_CQ_TB")
    private BigDecimal spfjyZbzZzTyCqTb;

    @ApiModelProperty(value = "市区累计指标")
    @TableField("SPFJY_ZBZ_TY_SQ")
    private BigDecimal spfjyZbzTySq;

    @ApiModelProperty(value = "市区同比增长")
    @TableField("SPFJY_ZBZ_TY_SQ_TB")
    private BigDecimal spfjyZbzTySqTb;

    @ApiModelProperty(value = "市区住宅累计指标")
    @TableField("SPFJY_ZBZ_ZZ_TY_SQ")
    private BigDecimal spfjyZbzZzTySq;

    @ApiModelProperty(value = "市区住宅同比增长")
    @TableField("SPFJY_ZBZ_ZZ_TY_SQ_TB")
    private BigDecimal spfjyZbzZzTySqTb;

}
