package com.hzfc.management.wyzj.modules.ums.model;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 后台角色菜单关系表
 * </p>
 *
 * @author hzfc
 * @since 2020-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("HZ_SZWY.UMS_ROLE_MENU_RELATION")
@ApiModel(value="UmsRoleMenuRelation对象", description="后台角色菜单关系表")
@KeySequence(value = "HZ_SZWY.SEQ_UMS_ROLEMENURELATION")
public class UmsRoleMenuRelation implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty(value = "角色ID")
    @TableField("ROLEID")
    private Long roleId;

    @ApiModelProperty(value = "菜单ID")
    @TableField("MENUID")
    private Long menuId;


}
