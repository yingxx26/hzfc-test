package com.hzfc.management.jsbsb.modules.testJiaofei.controller;


import com.hzfc.management.jsbsb.common.api.CommonResult;
import com.hzfc.management.jsbsb.modules.testJiaofei.dto.FeeDto;
import com.hzfc.management.jsbsb.modules.testJiaofei.dto.OwnerCarDto;
import com.hzfc.management.jsbsb.modules.testJiaofei.dto.RoomDto;
import com.hzfc.management.jsbsb.modules.testJiaofei.util.DateUtil2;
import com.hzfc.management.jsbsb.modules.testJiaofei.util.StringUtil;
import com.hzfc.management.jsbsb.utils.dateUtils.DateUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试结息
 *
 * @author Administrator
 */
@RequestMapping("/testJiaoFei")
@RestController
public class TestJiaoFeiController {


    @RequestMapping(value = "/testjf", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<Map> testjf() {

        return CommonResult.success(null);
    }

}
