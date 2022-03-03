package com.hzfc.management.jsbsb.config;

 import com.hzfc.management.jsbsb.common.outapi.AliPayApiConfig;
import com.hzfc.management.jsbsb.common.outapi.AliPayApiConfigKit;
 import com.hzfc.management.jsbsb.common.outapi.DefaultDecryptor;
 import com.hzfc.management.jsbsb.common.outapi.DefaultEncryptor;
 import com.hzfc.management.jsbsb.encryption.AESUtils;
 import org.apache.commons.codec.binary.Base64;
 import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public void  getConfig() throws Exception {
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

        String encryptKeyBase64 = Base64.encodeBase64String(AESUtils.initkey());

        DefaultEncryptor.encryptKey_MAP.put("encryptKeyBase64",encryptKeyBase64);
        DefaultDecryptor.encryptKey_MAP.put("encryptKeyBase64",encryptKeyBase64);

    }

}
