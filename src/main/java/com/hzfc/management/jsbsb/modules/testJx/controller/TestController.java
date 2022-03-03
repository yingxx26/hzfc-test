package com.hzfc.management.jsbsb.modules.testJx.controller;


import com.hzfc.management.jsbsb.common.api.CommonResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 测试结息
 *
 * @author Administrator
 */
@RequestMapping("/test")
@RestController
public class TestController {


    @RequestMapping(value = "/testjx", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Map> testjx() {
        return CommonResult.success(null);
    }
}
