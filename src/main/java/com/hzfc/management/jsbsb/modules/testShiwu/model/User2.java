package com.hzfc.management.jsbsb.modules.testShiwu.model;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

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
@TableName("HZ_CSDN.TEST_USER2")
@ApiModel(value = "UmsAdmin对象", description = "后台用户表")
@KeySequence(value = "HZ_CSDN.SEQ_USER2")
public class User2 implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    @TableField("NAME")
    private String name;


}
