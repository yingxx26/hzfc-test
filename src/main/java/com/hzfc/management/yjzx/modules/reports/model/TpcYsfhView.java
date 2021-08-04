package com.hzfc.management.yjzx.modules.reports.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author Yxx
 * @since 2021-08-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("HZ_TEST.TPC_YSFH_VIEW")
@ApiModel(value = "HZ_TEST.TPC_YSFH_VIEW指标", description = "项目排名")
public class TpcYsfhView implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private Long id;

    @TableField("YWSLID")
    private Long ywslid;

    @TableField("FWID")
    private Long fwid;

    @TableField("FWCODE")
    private Long fwcode;

    @TableField("YSXKZID")
    private Long ysxkzid;

    @TableField("PZKSZT")
    private Integer pzkszt;

    @TableField("FWYT")
    private Integer fwyt;

    @TableField("FCXZ")
    private Integer fcxz;

    @TableField("LSBZ")
    private Integer lsbz;

    @TableField("HXID")
    private Long hxid;

    @TableField("ZXQK")
    private Long zxqk;

    @TableField("YXWXBZ")
    private Double yxwxbz;

    @TableField("YSXMID")
    private Long ysxmid;

    @TableField("ZRZID")
    private Long zrzid;

    @TableField("CJSJ")
    private LocalDateTime cjsj;

    @TableField("ZZSJ")
    private LocalDateTime zzsj;

    @TableField("YSZID")
    private Long yszid;

    @TableField("CQAZBZ")
    private Integer cqazbz;

    @TableField("GHPJYT")
    private Integer ghpjyt;

    @TableField("MYZID")
    private Long myzid;

    @TableField("YSXMCODE")
    private Long ysxmcode;

    @TableField("HX")
    private Integer hx;

    @TableField("HXJG")
    private Integer hxjg;

    @TableField("HXMC")
    private String hxmc;

    @TableField("ZXQKMC")
    private String zxqkmc;

    @TableField("ZXSP")
    private Integer zxsp;

    @TableField("JZXJG")
    private Double jzxjg;

    @TableField("MPJG")
    private Double mpjg;

    @TableField("YZXSJ")
    private LocalDateTime yzxsj;

    @TableField("YZXBZ")
    private Integer yzxbz;

    @TableField("YZXYWSLID")
    private Long yzxywslid;

    @TableField("FWNSJG")
    private Double fwnsjg;

    @TableField("AID")
    private String aid;

    @TableField("FLAG")
    private Double flag;


}
