package com.hzfc.management.yjzx.modules.reports.dto;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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
@Getter
@Setter
public class CategoryAdd {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "父id")
    private String parentid;

    @ApiModelProperty(value = "分类名称")
    private String categoryname;


}
