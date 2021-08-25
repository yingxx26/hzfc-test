package com.hzfc.management.wyzj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})*/
@SpringBootApplication
public class HzfcApplication {

    public static void main(String[] args) {
        SpringApplication.run(HzfcApplication.class, args);
    }

}
