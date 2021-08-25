package com.hzfc.management.wyzj.modules.ums.model;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 后台用户表
 * </p>
 *
 * @author macro
 * @since 2020-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("HZ_SZWY.UMS_ADMIN")
@ApiModel(value = "UmsAdmin对象", description = "后台用户表")
@KeySequence(value = "HZ_SZWY.SEQ_UMS_ADMIN")
public class UmsAdmin implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    @TableField("USERNAME")
    private String username;

    @TableField("PASSWORD")
    private String password;

    @ApiModelProperty(value = "头像")
    @TableField("ICON")
    private String icon;

    @ApiModelProperty(value = "邮箱")
    @TableField("EMAIL")
    private String email;

    @ApiModelProperty(value = "昵称")
    @TableField("NICKNAME")
    private String nickName;

    @ApiModelProperty(value = "备注信息")
    @TableField("NOTE")
    private String note;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATETIME")
    private Date createTime;

    @ApiModelProperty(value = "最后登录时间")
    @TableField("LOGINTIME")
    private Date loginTime;

    @ApiModelProperty(value = "帐号启用状态：0->禁用；1->启用")
    @TableField("STATUS")
    private Integer status;


}
