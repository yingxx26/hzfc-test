package com.hzfc.management.jsbsb.modules.testShiwu.controller;


import com.hzfc.management.jsbsb.common.api.CommonResult;
import com.hzfc.management.jsbsb.modules.testShiwu.model.User;
import com.hzfc.management.jsbsb.modules.testShiwu.model.User2;
import com.hzfc.management.jsbsb.modules.testShiwu.service.TestUser2Service;
import com.hzfc.management.jsbsb.modules.testShiwu.service.TestUserService;
import com.hzfc.management.jsbsb.modules.ums.service.UmsRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 测试结息
 *
 * @author Administrator
 */
@RequestMapping("/testShiwu")
@RestController
public class TestUserController {

    @Autowired
    private TestUserService testUserService;

    @Autowired
    private TestUser2Service testUser2Service;

    @RequestMapping(value = "/testsw", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Map> testsw() {


        return CommonResult.success(null);
    }
}
