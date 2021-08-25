package com.hzfc.management.wyzj.modules.szwy.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzfc.management.wyzj.common.api.CommonPage;
import com.hzfc.management.wyzj.common.api.CommonResult;
import com.hzfc.management.wyzj.modules.szwy.model.ActProcinst;
import com.hzfc.management.wyzj.modules.szwy.service.SzwyActProcinstService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * 导出Word
 *
 * @author Administrator
 */
@RequestMapping("/wyzj/procinst")
@RestController
public class WyzjActProcinstController {

    @Autowired
    private SzwyActProcinstService szwyActProcinstService;

    @ApiOperation("分页查询后台业务流程")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<ActProcinst>> list(
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, Principal principal) {
        String username = principal.getName();
        Page<ActProcinst> actProcinstList = szwyActProcinstService.list(username, pageSize, pageNum);

        return CommonResult.success(CommonPage.restPage(actProcinstList));
    }
}
