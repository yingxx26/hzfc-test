package com.hzfc.management.jsbsb.modules.testZfb.controller;


import com.alibaba.fastjson.JSONObject;
import com.hzfc.management.jsbsb.common.outapi.*;
import com.hzfc.management.jsbsb.config.AliPayBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 导出Word
 *
 * @author Administrator
 */
@RequestMapping("/aliPay")
@RestController
public class ApiController  {

    @Resource
    private AliPayBean aliPayBean;

    /**
     * 扫码支付
     */
    @RequestMapping(value = "/tradePreCreatePay")
    @ResponseBody
    public String tradePreCreatePay() {
        String subject = "Javen 支付宝扫码支付测试";
        String totalAmount = "86";
        String storeId = "123";
//        String notifyUrl = aliPayBean.getDomain() + NOTIFY_URL;
        String notifyUrl =   "/aliPay/cert_notify_url";

        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        model.setSubject(subject);
        model.setTotalAmount(totalAmount);
        model.setStoreId(storeId);
        model.setTimeoutExpress("5m");
        model.setOutTradeNo("1");

        try {
            String resultStr = AliPayApi.tradePrecreatePayToResponse(model, notifyUrl).getBody();
            JSONObject jsonObject = JSONObject.parseObject(resultStr);
            return jsonObject.getJSONObject("alipay_trade_precreate_response").getString("qr_code");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
