package com.hzfc.management.wyzj.modules.szwy.model;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Blob;

/**
 * <p>
 * 注册审批表
 * </p>
 *
 * @author Yxx
 * @since 2021-08-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("HZ_SZWY.TPQ_ZCSPB")
@ApiModel(value = "TPQ_ZCSPB对象", description = "注册审批表")
@KeySequence(value = "HZ_SZWY.SEQ_TPQ_ZCSPB")
public class TpqZcspb implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Integer id;

    /**
     * 业务实例ID
     */
    @TableField("YWID")
    private Long ywid;

    /**
     * 物业名称
     */
    @TableField("YWMC")
    private String ywmc;

    /**
     * 物业编号
     */
    @TableField("YWBH")
    private String ywbh;

    /**
     * 物业代码
     */
    @TableField("YWDM")
    private Long ywdm;

    /**
     * 小区id
     */
    @TableField("XQID")
    private Integer xqid;

    /**
     * 小区code
     */
    @TableField("XQCODE")
    private Integer xqcode;

    /**
     * 物业企业id
     */
    @TableField("WYQYID")
    private Integer wyqyid;

    /**
     * 物业企业code
     */
    @TableField("WYQYCODE")
    private Integer wyqycode;

    /**
     * 业委会id
     */
    @TableField("YWHID")
    private Integer ywhid;

    /**
     * 业委会code
     */
    @TableField("YWHCODE")
    private Integer ywhcode;

    @TableField("FJ1")
    private Blob fj1;

    @TableField("FJ2")
    private Blob fj2;

    @TableField("FJ3")
    private Blob fj3;

    @TableField("FJ4")
    private Blob fj4;

    /**
     * 审计单位id
     */
    @TableField("SJDWID")
    private Integer sjdwid;

    /**
     * 审计单位code
     */
    @TableField("SJDWCODE")
    private Integer sjdwcode;

    /**
     * 用户名id
     */
    @TableField("USERID")
    private Integer userid;

    /**
     * 老物业小区id
     */
    @TableField("OLDXQID")
    private Integer oldxqid;

    /**
     * 老小区code
     */
    @TableField("OLDXQCODE")
    private Integer oldxqcode;

    @TableField("SFSPTG")
    private Integer sfsptg;

    /**
     * 审计编号
     */
    @TableField("SJBH")
    private String sjbh;

    /**
     * 是否历史（0：是历史记录  1：是最新记录）
     */
    @TableField("SFLS")
    private Integer sfls;

    /**
     * 变更内容
     */
    @TableField("BGNR")
    private String bgnr;

    /**
     * 原注册审批表ID
     */
    @TableField("OLDID")
    private Integer oldid;

    @TableField("CODE")
    private Integer code;


}
