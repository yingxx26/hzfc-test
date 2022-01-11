package com.hzfc.management.jsbsb.modules.test.controller;


import com.hzfc.management.jsbsb.common.api.CommonResult;
import org.springframework.web.bind.annotation.*;

/**
 * 导出Word
 *
 * @author Administrator
 */
@RequestMapping("/test")
@RestController
public class TestController {

    @RequestMapping(value = "/getewm", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<String> getewm() {
        return CommonResult.success(null);
    }
}
