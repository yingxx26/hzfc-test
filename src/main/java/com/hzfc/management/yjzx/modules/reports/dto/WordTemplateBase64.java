package com.hzfc.management.yjzx.modules.reports.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 用户登录参数
 * Created by hzfc on 2018/4/26.
 */
@Getter
@Setter
public class WordTemplateBase64 {

    @ApiModelProperty(value = "文件base64")
    private String wordBase64;

}
