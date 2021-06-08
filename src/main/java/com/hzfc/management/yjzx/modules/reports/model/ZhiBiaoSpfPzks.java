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
@TableName("HZ_TEST.DWS_YBTJ_SPF_PZKS")
@ApiModel(value = "DWS_YBTJ_SPF_PZKS指标", description = "商品房批准可售")
public class ZhiBiaoSpfPzks implements Serializable {


    @ApiModelProperty(value = "统计时间")
    @TableField("TJSJ")
    private String tjsj;

    @ApiModelProperty(value = "指标名")
    @TableField("ZBNAME")
    private String zbname;

    @ApiModelProperty(value = "全市本年zbz")
    @TableField("SPF_PZKS_ZBZ_TY_ALL")
    private Double spfPzksZbzTyAll;

    @ApiModelProperty(value = "全市去年指标值")
    @TableField("SPF_PZKS_ZBZ_TY_LASTY_ALL")
    private Double spfPzksZbzTyLastyAll;

    @ApiModelProperty(value = "全市本月zbz")
    @TableField("SPF_PZKS_ZBZ_TM_ALL")
    private Double spfPzksZbzTmAll;

    @ApiModelProperty(value = "全市去年同期指标值")
    @TableField("SPF_PZKS_ZBZ_TM_LASTY_ALL")
    private Double spfPzksZbzTmLastyAll;

    @ApiModelProperty(value = "市区本年zbz")
    @TableField("SPF_PZKS_ZBZ_TY_SQ")
    private Double spfPzksZbzTySq;

    @ApiModelProperty(value = "市区去年指标值")
    @TableField("SPF_PZKS_ZBZ_TY_LASTY_SQ")
    private Double spfPzksZbzTyLastySq;

    @ApiModelProperty(value = "市区本月zbz")
    @TableField("SPF_PZKS_ZBZ_TM_SQ")
    private Double spfPzksZbzTmSq;

    @ApiModelProperty(value = "市区去年同期指标值")
    @TableField("SPF_PZKS_ZBZ_TM_LASTY_SQ")
    private Double spfPzksZbzTmLastySq;

}
