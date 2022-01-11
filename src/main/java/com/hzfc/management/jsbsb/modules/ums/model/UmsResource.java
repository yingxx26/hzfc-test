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
 * 后台资源表
 * </p>
 *
 * @author macro
 * @since 2020-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("HZ_CSDN.TJBB_UMS_RESOURCE")
@ApiModel(value = "UmsResource对象", description = "后台资源表")
@KeySequence(value = "HZ_CSDN.SEQ_TJBB_UMS_RESOURCE")
public class UmsResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATETIME")
    private Date createTime;

    @ApiModelProperty(value = "资源名称")
    @TableField("NAME")
    private String name;

    @ApiModelProperty(value = "资源URL")
    @TableField("URL")
    private String url;

    @ApiModelProperty(value = "描述")
    @TableField("DESCRIPTION")
    private String description;

    @ApiModelProperty(value = "资源分类ID")
    @TableField("CATEGORYID")
    private Long categoryId;


}
