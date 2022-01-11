package com.hzfc.management.jsbsb.modules.ums.model;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 后台用户登录日志表
 * </p>
 *
 * @author macro
 * @since 2020-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("HZ_CSDN.TJBB_UMS_ADMIN_LOGIN_LOG")
@ApiModel(value = "UmsAdminLoginLog对象", description = "后台用户登录日志表")
@KeySequence(value = "HZ_CSDN.SEQ_TJBB_UMS_ADMINLOGIN")
public class UmsAdminLoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    @TableField("ADMINID")
    private Long adminId;

    @TableField("CREATETIME")
    private Date createTime;

    @TableField("IP")
    private String ip;

    @TableField("ADDRESS")
    private String address;

    @ApiModelProperty(value = "浏览器登录类型")
    @TableField("USERAGENT")
    private String userAgent;


}
