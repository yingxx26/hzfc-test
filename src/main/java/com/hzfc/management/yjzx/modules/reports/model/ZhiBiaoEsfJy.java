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
@TableName("HZ_TEST.DWS_YBTJ_ESFJY")
@ApiModel(value = "DWS_YBTJ_ESFJY指标", description = "二手房综合成交指标")
public class ZhiBiaoEsfJy implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "统计时间")
    @TableField("TJSJ")
    private String tjsj;

    @ApiModelProperty(value = "指标名")
    @TableField("ZBNAME")
    private String zbname;

    @ApiModelProperty(value = "本月zbz")
    @TableField("ESFJY_ZBZ_CNT_TM")
    private Double esfjyZbzCntTm;

    @ApiModelProperty(value = "本年zbz")
    @TableField("ESFJY_ZBZ_CNT_TY")
    private Double esfjyZbzCntTy;

    @ApiModelProperty(value = "本年zbz同比")
    @TableField("ESFJY_ZBZ_CNT_TY_TB")
    private Double esfjyZbzCntTyTb;

    @ApiModelProperty(value = "本月住宅zbz")
    @TableField("ESFJY_ZBZ_ZZ_CNT_TM")
    private Double esfjyZbzZzCntTm;

    @ApiModelProperty(value = "本年住宅zbz")
    @TableField("ESFJY_ZBZ_ZZ_CNT_TY")
    private Double esfjyZbzZzCntTy;

    @ApiModelProperty(value = "本月住宅zbz同比")
    @TableField("ESFJY_ZBZ_ZZ_CNT_TY_TB")
    private Double esfjyZbzZzCntTyTb;


}
