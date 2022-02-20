package com.hzfc.management.jsbsb.modules.api.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户登录参数
 * Created by hzfc on 2018/4/26.
 */
@Getter
@Setter
public class Param {

    private String charset;

    private String method;
    private String sign;
    private String notify_url;
    private String version;
    private String app_id;

    private String sign_type;
    private String timestamp;
    private String alipay_sdk;
    private String format;
}
