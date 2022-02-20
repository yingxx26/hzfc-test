package com.hzfc.management.jsbsb.modules.test.controller;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hzfc.management.jsbsb.common.api.CommonResult;
import com.hzfc.management.jsbsb.modules.test.dto.TprDqjxgs;
import com.hzfc.management.jsbsb.modules.test.dto.TprHqjxgs;
import com.hzfc.management.jsbsb.modules.test.dto.TprJxjg;
import com.hzfc.management.jsbsb.modules.test.dto.TprJxzhzjbd;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 导出Word
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
