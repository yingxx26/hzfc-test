package com.hzfc.management.jsbsb.config;

import com.hzfc.management.jsbsb.common.config.BaseSwaggerConfig;
import com.hzfc.management.jsbsb.common.domain.SwaggerProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger API文档相关配置
 * Created by macro on 2018/4/26.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.hzfc.management.jsbsb.modules")
                .title("management-tiny项目骨架")
                .description("management-tiny项目骨架相关接口文档")
                .contactName("yxx")
                .version("1.0")
                .enableSecurity(true)
                .build();
    }
}
