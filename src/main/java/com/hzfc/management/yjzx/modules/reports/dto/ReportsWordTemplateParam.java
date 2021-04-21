package com.hzfc.management.yjzx.modules.reports.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * 用户登录参数
 * Created by hzfc on 2018/4/26.
 */
@Getter
@Setter
public class ReportsWordTemplateParam {

    @ApiModelProperty(value = "创建人")
    private String createuser;

    @ApiModelProperty("用途")
    private String usepurpose;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "模板路径")
    private String templatepath;

    @ApiModelProperty(value = "模板名称")
    private String templatename;

    @ApiModelProperty(value = "是否在用")
    private Integer inuse;
}
