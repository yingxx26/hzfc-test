package com.hzfc.management.jsbsb.config;

 import com.hzfc.management.jsbsb.common.outapi.AliPayApiConfig;
import com.hzfc.management.jsbsb.common.outapi.AliPayApiConfigKit;
 import com.hzfc.management.jsbsb.common.outapi.DefaultDecryptor;
 import com.hzfc.management.jsbsb.common.outapi.DefaultEncryptor;
 import com.hzfc.management.jsbsb.modules.api.controller.AbstractAliPayApiController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;

/**
 * @Author yxx
 * @Date 2022/1/11 15:45
 */
@Configuration
public class IJPayConfigurer extends WebMvcConfigurationSupport {



    @Resource
    private AliPayBean aliPayBean;

    @Bean
    public void  getConfig() {
        AliPayApiConfig aliPayApiConfig;
        try {
            aliPayApiConfig = AliPayApiConfigKit.getApiConfig(aliPayBean.getAppId());
        } catch (Exception e) {
            aliPayApiConfig = AliPayApiConfig.builder()
                    .setAppId(aliPayBean.getAppId())
                    .setAliPayPublicKey(aliPayBean.getPublicKey())
                    //.setAppCertPath(aliPayBean.getAppCertPath())
                    //.setAliPayCertPath(aliPayBean.getAliPayCertPath())
                    //.setAliPayRootCertPath(aliPayBean.getAliPayRootCertPath())
                    .setCharset("UTF-8")
                    .setPrivateKey(aliPayBean.getPrivateKey())
                    .setServiceUrl(aliPayBean.getServerUrl())
                    .setSignType("RSA2")
                    // 普通公钥方式
                    .build();
            // 证书模式
            //.buildByCert();

        }
        AliPayApiConfigKit.setThreadLocalAliPayApiConfig(aliPayApiConfig);
        DefaultEncryptor.encryptKey_MAP.put("encryptKey",aliPayBean.getAesKey());
        DefaultDecryptor.encryptKey_MAP.put("encryptKey",aliPayBean.getAesKey());

    }

}
