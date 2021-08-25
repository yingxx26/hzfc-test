package com.hzfc.management.wyzj.modules.szwy.controller;


import com.hzfc.management.wyzj.common.api.CommonResult;
import com.hzfc.management.wyzj.modules.szwy.model.TpqWyqygx;
import com.hzfc.management.wyzj.modules.szwy.service.ITpqZcspbService;
import com.hzfc.management.wyzj.modules.szwy.vo.ZcspVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * <p>
 * 注册审批关系 前端控制器
 * </p>
 *
 * @author Yxx
 * @since 2021-08-19
 */
@RestController
@RequestMapping("/wyzj/zcsp")
public class ZcspController {

    @Autowired
    private ITpqZcspbService iTpqZcspbService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<String> saveZcsp(@RequestBody TpqWyqygx tpqWyqygx, Principal principal) {
        String username = principal.getName();
        iTpqZcspbService.saveTpqZcspb(tpqWyqygx, username);
        return CommonResult.success(null);
    }


    @ApiOperation("根据ywslid注册审批详情")
    @RequestMapping(value = "/get/{ywslid}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<ZcspVo> getItem(@PathVariable Long ywslid) {
        if (StringUtils.isEmpty(ywslid)) {
            return CommonResult.success(null);
        }
        ZcspVo zcspVo = iTpqZcspbService.getzcps(ywslid);
        return CommonResult.success(zcspVo);
    }


}
