package com.hzfc.management.wyzj.modules.szwy.model;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
@TableName("HZ_SZWY.ACT_PROCDEF")
@ApiModel(value = "ACT_PROCDEF对象", description = "流程定义表")
@KeySequence(value = "HZ_SZWY.SEQ_ACT_PROCDEF")
public class ActProcdef implements Serializable {


    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private Double id;

    /**
     * 流程定义id
     */
    @TableField("PROCDEFID")
    private String procdefid;

    /**
     * 流程定义名称
     */
    @TableField("PROCDEFNAME")
    private String procdefname;

    /**
     * 流程定义信息
     */
    @TableField("PROCDEFINFO")
    private String procdefinfo;


}
