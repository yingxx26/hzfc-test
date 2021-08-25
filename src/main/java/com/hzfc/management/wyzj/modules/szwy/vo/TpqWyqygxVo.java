package com.hzfc.management.wyzj.modules.szwy.vo;


import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Date;

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
public class TpqWyqygxVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer code;

    /**
     * 物业企业名称
     */
    private String wyqymc;

    /**
     * 联系人
     */
    private String lxr;

    /**
     * 联系电话
     */
    private String lxdh;

    /**
     * 从业主体id
     */
    private String cyztid;

    /**
     * 从业主题code
     */
    private String cyztcode;

    /**
     * 物业企业状态
     */
    private Integer zt;

    /**
     * 物业服务合同时间起
     */
    private Date wyfwhtsjq;

    /**
     * 物业服务合同时间止
     */
    private Date wyfwhtsjz;

    /**
     * 联系人电话
     */
    private String lxrdh;

    /**
     * 联系人手机
     */
    private String lxrsj;


}
