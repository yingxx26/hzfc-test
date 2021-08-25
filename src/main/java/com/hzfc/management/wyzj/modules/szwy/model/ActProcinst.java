package com.hzfc.management.wyzj.modules.szwy.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Yxx
 * @since 2021-08-18
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("HZ_SZWY.ACT_PROCINST")
@ApiModel(value = "ACT_PROCINST对象", description = "流程实例表")
@KeySequence(value = "HZ_SZWY.SEQ_ACT_PROCINST")
public class ActProcinst implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Double id;

    /**
     * 业务实例id
     */
    @TableField("YWSLID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long ywslid;

    /**
     * 业务名称
     */
    @TableField("YWNAME")
    private String ywname;

    /**
     * 创建时间
     */
    @TableField("CREATETIME")
    private Date createtime;

    /**
     * 更新时间
     */
    @TableField("UPDATETIME")
    private Date updatetime;

    /**
     * 当前环节使用人
     */
    @TableField("CURRENTUSER")
    private String currentuser;

    /**
     * 流程定义id
     */
    @TableField("PROCDEFID")
    private Long procdefid;

    /**
     * 当前活动名称
     */
    @TableField("CURRENTTASK")
    private String currenttask;


    /**
     * 流程实例id
     */
    @TableField("PROCINSTID")
    private Long procinstid;


    /**
     * 流程定义名称
     */
    @TableField("PROCDEFNAME")
    private String procdefname;
}
