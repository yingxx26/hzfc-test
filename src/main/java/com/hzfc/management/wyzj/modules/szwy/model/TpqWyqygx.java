package com.hzfc.management.wyzj.modules.szwy.model;


import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;

/**
 * <p>
 * 物业企业关系
 * </p>
 *
 * @author Yxx
 * @since 2021-08-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("HZ_SZWY.TPQ_WYQYGX")
@ApiModel(value = "TPQ_WYQYGX对象", description = "物业企业信息")
@KeySequence(value = "HZ_SZWY.SEQ_TPQ_WYQYGX")
public class TpqWyqygx implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Integer id;

    @TableField("CODE")
    private Integer code;

    /**
     * 物业企业名称
     */
    @TableField("WYQYMC")
    private String wyqymc;

    /**
     * 联系人
     */
    @TableField("LXR")
    private String lxr;

    /**
     * 联系电话
     */
    @TableField("LXDH")
    private String lxdh;

    /**
     * 从业主体id
     */
    @TableField("CYZTID")
    private String cyztid;

    /**
     * 从业主题code
     */
    @TableField("CYZTCODE")
    private String cyztcode;

    /**
     * 物业企业状态
     */
    @TableField("ZT")
    private Integer zt;

    /**
     * 物业服务合同时间起
     */
    @TableField("WYFWHTSJQ")
    private Date wyfwhtsjq;

    /**
     * 物业服务合同时间止
     */
    @TableField("WYFWHTSJZ")
    private Date wyfwhtsjz;

    /**
     * 联系人电话
     */
    @TableField("LXRDH")
    private String lxrdh;

    /**
     * 联系人手机
     */
    @TableField("LXRSJ")
    private String lxrsj;


}
