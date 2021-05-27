package com.hzfc.management.yjzx.modules.reports.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Transient;

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
@TableName("HZ_TEST.DWS_YHBM_YHLPXX_YSB_DF")
@ApiModel(value = "DWS_YHBM_YHLPXX_YSB_DF指标", description = "摇号指标")
public class ZhiBiaoYhbm implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "楼盘城区")
    @TableField("LPCQ")
    private String lpcq;

    @ApiModelProperty(value = "楼盘名称")
    @TableField("LPMC")
    private String lpmc;

    @ApiModelProperty(value = "预售证号")
    @TableField("YSZH")
    private String yszh;

    @ApiModelProperty(value = "报名开始时间")
    @TableField("BMKSSJ")
    private Date bmkssj;

    @ApiModelProperty(value = "报名结束时间")
    @TableField("BMJSSJ")
    private Date bmjssj;

    @ApiModelProperty(value = "摇号结束时间")
    @TableField("YHJSSJ")
    private Date yhjssj;

    @ApiModelProperty(value = "创建时间")
    @TableField("CJSJ")
    private Date cjsj;

    @ApiModelProperty(value = "房源数")
    @TableField("FYS")
    private Long fys;

    @ApiModelProperty(value = "报名人数-")
    @TableField("BMRS")
    private Long bmrs;

    @ApiModelProperty(value = "中签率")
    @TableField("ZQL")
    private Double zql;

    @ApiModelProperty(value = "人才房房源数")
    @TableField("RCFYS")
    private Long rcfys;

    @ApiModelProperty(value = "人才报名人数")
    @TableField("RCBMRS")
    private Long rcbmrs;

    @ApiModelProperty(value = "")
    @TableField("RXZQL")
    private Double rxzql;

    @ApiModelProperty(value = "无房户房源数")
    @TableField("WFFYS")
    private Long wffys;

    @ApiModelProperty(value = "无房户报名人数")
    @TableField("WFBMRS")
    private Long wfbmrs;

    @ApiModelProperty(value = "无房户中签率")
    @TableField("WFZQL")
    private Double wfzql;

    @ApiModelProperty(value = "-年份")
    @TableField("QTFYS")
    private Long qtfys;

    @ApiModelProperty(value = "-年份")
    @TableField("QTBMRS")
    private Long qtbmrs;

    @ApiModelProperty(value = "-年份")
    @TableField("QTZQL")
    private Double qtzql;

    @ApiModelProperty(value = "-年份")
    @TableField("YXFRS")
    private Long yxfrs;

    @ApiModelProperty(value = "是否摇号")
    @TableField("SFYH")
    private String sfyh;

    @ApiModelProperty(value = "步骤")
    @TableField("STEP")
    private String step;

    @ApiModelProperty(value = "-年份")
    @TableField("HPBZ")
    private Long hpbz;

    @ApiModelProperty(value = "转换时间->月份")
    private transient String month;
}
