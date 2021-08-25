package com.hzfc.management.wyzj.modules.ums.model;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 后台用户和角色关系表
 * </p>
 *
 * @author hzfc
 * @since 2020-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("HZ_SZWY.UMS_ADMIN_ROLE_RELATION")
@ApiModel(value = "UmsAdminRoleRelation对象", description = "后台用户和角色关系表")
@KeySequence(value = "HZ_SZWY.SEQ_UMS_ADMINROLERELATION")
public class UmsAdminRoleRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    @TableField("ADMINID")
    private Long adminId;

    @TableField("ROLEID")
    private Long roleId;


}
