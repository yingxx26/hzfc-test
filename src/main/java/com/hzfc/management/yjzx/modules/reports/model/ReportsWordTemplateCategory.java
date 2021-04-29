package com.hzfc.management.yjzx.modules.reports.model;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * word模板分类表
 * </p>
 *
 * @author yxx
 * @since 2020-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("HZ_TEST.TJBB_WORDTEMPLATE_CATEGORY")
@ApiModel(value = "WordTemplateCategory对象", description = "word模板分类")
@KeySequence(value = "HZ_TEST.SEQ_TJBB_WORDTEMPLATE_CATEGORY")
public class ReportsWordTemplateCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty(value = "父id")
    @TableField("PARENTID")
    private String parentid;

    @ApiModelProperty(value = "分类名称")
    @TableField("CATEGORYNAME")
    private String categoryname;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATETIME")
    private Date createTime;

}
