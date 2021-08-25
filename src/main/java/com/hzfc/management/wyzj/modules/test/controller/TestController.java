package com.hzfc.management.wyzj.modules.test.controller;


import com.hzfc.management.wyzj.common.api.CommonResult;
import com.hzfc.management.wyzj.wbsocket.PushMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.hzfc.management.wyzj.wbsocket.SocketIoService;

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
        //1 如果是新用户  生成优惠券（分享者和被分享者）
        //2 不是新用户
        //  2.1 已经领过
        //  2.2 没有领过 生成优惠券（分享者和被分享者）
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
