package com.hzfc.management.jsbsb.modules.ums.model;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 后台角色资源关系表
 * </p>
 *
 * @author macro
 * @since 2020-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("HZ_CSDN.TJBB_UMS_ROLE_RESOURCE_REL")
@ApiModel(value="UmsRoleResourceRelation对象", description="后台角色资源关系表")
@KeySequence(value = "HZ_CSDN.SEQ_TJBB_UMS_ROLERESOURCEREL")
public class UmsRoleResourceRelation implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty(value = "角色ID")
    @TableField("ROLEID")
    private Long roleId;

    @ApiModelProperty(value = "资源ID")
    @TableField("RESOURCEID")
    private Long resourceId;


}
