package com.hzfc.management.yjzx.modules.ums.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 后台菜单表
 * </p>
 *
 * @author hzfc
 * @since 2020-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("HZ_TEST.UMS_MENU")
@ApiModel(value="UmsMenu对象", description="后台菜单表")
public class UmsMenu implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "父级ID")
    @TableField("PARENTID")
    private Long parentId;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATETIME")
    private Date createTime;

    @ApiModelProperty(value = "菜单名称")
    @TableField("TITLE")
    private String title;

    @ApiModelProperty(value = "菜单级数")
    @TableField("MLEVEL")
    private Integer mlevel;

    @ApiModelProperty(value = "菜单排序")
    @TableField("SORT")
    private Integer sort;

    @ApiModelProperty(value = "前端名称")
    @TableField("NAME")
    private String name;

    @ApiModelProperty(value = "前端图标")
    @TableField("ICON")
    private String icon;

    @ApiModelProperty(value = "前端隐藏")
    @TableField("HIDDEN")
    private Integer hidden;


}
