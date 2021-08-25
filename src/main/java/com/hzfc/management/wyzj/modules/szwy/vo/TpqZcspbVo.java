package com.hzfc.management.wyzj.modules.szwy.vo;

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
public class TpqZcspbVo implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;

    /**
     * 业务实例ID
     */
    private Long ywid;

    /**
     * 物业名称
     * private String ywmc;
     * <p>
     * /**
     * 物业编号
     */
    private String ywbh;

    /**
     * 物业代码
     */
    @TableField("YWDM")
    private Long ywdm;

    /**
     * 小区id
     */
    private Integer xqid;

    /**
     * 小区code
     */
    private Integer xqcode;

    /**
     * 物业企业id
     */
    private Integer wyqyid;

    /**
     * 物业企业code
     */
    private Integer wyqycode;

    /**
     * 业委会id
     */
    private Integer ywhid;

    /**
     * 业委会code
     */
    private Integer ywhcode;

    private Blob fj1;
    private Blob fj2;

    private Blob fj3;

    private Blob fj4;

    /**
     * 审计单位id
     */
    private Integer sjdwid;

    /**
     * 审计单位code
     */
    private Integer sjdwcode;

    /**
     * 用户名id
     */
    private Integer userid;

    /**
     * 老物业小区id
     */
    private Integer oldxqid;

    /**
     * 老小区code
     */
    private Integer oldxqcode;

    private Integer sfsptg;

    /**
     * 审计编号
     */
    private String sjbh;

    /**
     * 是否历史（0：是历史记录  1：是最新记录）
     */
    private Integer sfls;

    /**
     * 变更内容
     */
    private String bgnr;

    /**
     * 原注册审批表ID
     */
    private Integer oldid;

    private Integer code;


}
