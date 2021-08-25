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
 * 后台用户角色表
 * </p>
 *
 * @author hzfc
 * @since 2020-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("HZ_SZWY.UMS_ROLE")
@ApiModel(value = "UmsRole对象", description = "后台用户角色表")
@KeySequence(value = "HZ_SZWY.SEQ_UMS_ROLE")
public class UmsRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty(value = "名称")
    @TableField("NAME")
    private String name;

    @ApiModelProperty(value = "描述")
    @TableField("DESCRIPTION")
    private String description;

    @ApiModelProperty(value = "后台用户数量")
    @TableField("ADMINCOUNT")
    private Integer adminCount;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATETIME")
    private Date createTime;

    @ApiModelProperty(value = "启用状态：0->禁用；1->启用")
    @TableField("STATUS")
    private Integer status;

    @TableField("SORT")
    private Integer sort;


}
