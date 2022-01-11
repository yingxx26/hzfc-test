package com.hzfc.management.jsbsb.modules.api.controller;


import com.hzfc.management.jsbsb.common.outapi.AliPayApiConfig;
import com.hzfc.management.jsbsb.common.outapi.AlipayApiException;

/**
 * @author Javen
 */
public abstract class AbstractAliPayApiController {
    /**
     * 获取支付宝配置
     *
     * @return {@link AliPayApiConfig} 支付宝配置
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public abstract AliPayApiConfig getApiConfig() throws AlipayApiException;
}
