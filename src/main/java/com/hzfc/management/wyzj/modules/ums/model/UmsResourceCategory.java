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
 * 资源分类表
 * </p>
 *
 * @author macro
 * @since 2020-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("HZ_SZWY.UMS_RESOURCE_CATEGORY")
@ApiModel(value = "UmsResourceCategory对象", description = "资源分类表")
@KeySequence(value = "HZ_SZWY.SEQ_UMS_RESOURCECATEGORY")
public class UmsResourceCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATETIME")
    private Date createTime;

    @ApiModelProperty(value = "分类名称")
    @TableField("NAME")
    private String name;

    @ApiModelProperty(value = "排序")
    @TableField("SORT")
    private Integer sort;


}
