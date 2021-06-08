package com.hzfc.management.yjzx.modules.test.controller;


import com.hzfc.management.yjzx.common.api.CommonResult;
import com.hzfc.management.yjzx.modules.reports.dto.ExportParam;
import com.hzfc.management.yjzx.wbsocket.PushMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.hzfc.management.yjzx.wbsocket.SocketIoService;

/**
 * 导出Word
 *
 * @author Administrator
 */
@RequestMapping("/test")
@RestController
public class TestController {

    @Autowired
    private SocketIoService socketIoService;


    @RequestMapping(value = "/myshare/{pid}/{firstid}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<String> myshare(@PathVariable("pid") Long pid,
                                        @PathVariable("firstid") Long firstid) {
        System.out.println("扫描分享二维码");
        socketIoService.pushMessageToUser(new PushMessage("yxx", "", "hello"));
        return CommonResult.success(null);
    }


    @RequestMapping(value = "/getewm", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<String> getewm() {
        System.out.println("获取二维码");
        return CommonResult.success(null);
    }
}
