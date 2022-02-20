package com.hzfc.management.jsbsb.modules.test.controller;


import com.hzfc.management.jsbsb.common.api.CommonResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 导出Word
 *
 * @author Administrator
 */
@RequestMapping("/test")
@RestController
public class TestController {


    @RequestMapping(value = "/getewm", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Map> getewm() {
        System.out.println("==========================good ");

        return CommonResult.success(null);
    }

}
