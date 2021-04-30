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
 * word模板表
 * </p>
 *
 * @author yxx
 * @since 2020-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("HZ_TEST.TJBB_WORDTEMPLATE")
@ApiModel(value = "wordTemplate对象", description = "word模板")
@KeySequence(value = "HZ_TEST.SEQ_TJBB_WORDTEMPLATE")
public class ReportsWordTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty(value = "创建人")
    @TableField("CREATEUSER")
    private String createuser;

    @ApiModelProperty(value = "用途")
    @TableField("USEPURPOSE")
    private String usepurpose;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATETIME")
    private Date createTime;

    @ApiModelProperty(value = "模板路径")
    @TableField("TEMPLATEPATH")
    private String templatepath;

    @ApiModelProperty(value = "模板名称")
    @TableField("TEMPLATENAME")
    private String templatename;

    @ApiModelProperty(value = "是否在用")
    @TableField("INUSE")
    private Integer inuse;

    @ApiModelProperty(value = "指标")
    @TableField("ZHIBIAOS")
    private String zhibiaos;

    @ApiModelProperty(value = "分类id")
    @TableField("CATEGORYID")
    private Long categoryid;


}
