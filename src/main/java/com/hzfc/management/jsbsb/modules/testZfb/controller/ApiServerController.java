package com.hzfc.management.jsbsb.modules.testZfb.controller;

import com.hzfc.management.jsbsb.common.api.CommonResult;
import com.hzfc.management.jsbsb.common.outapi.AlipaySignature;
import com.hzfc.management.jsbsb.common.outapi.DefaultEncryptor;
import com.hzfc.management.jsbsb.config.AliPayBean;
import com.hzfc.management.jsbsb.encryption.AESUtils;
import com.hzfc.management.jsbsb.modules.testZfb.dto.Param;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 导出Word
 *
 * @author Administrator
 */
@RequestMapping("/aliPayServer")
@RestController
public class ApiServerController {


    @Resource
    private AliPayBean aliPayBean;

    @RequestMapping(value = "/getewm", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Map> getewm(Param param) throws Exception {
        System.out.println("==========================good " + param);


        HttpServletRequest myrequest = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, String> params = convertRequestParamsToMap(myrequest);

        boolean signVerified = AlipaySignature.rsaCheckV1(params, aliPayBean.getPublicKey(),
                "UTF-8", "RSA");

        byte[] encryptKey = Base64.decodeBase64(DefaultEncryptor.encryptKey_MAP.get("encryptKeyBase64"));
        byte[] biz_contents = Base64.decodeBase64(params.get("biz_content").getBytes());
        byte[] plainBytes = AESUtils.decrypt(biz_contents, encryptKey);
        String x = new String(plainBytes, "UTF-8");

        Map outermap = new HashMap();
        outermap.put("outer", "outer");
        Map innermap = new HashMap();
        outermap.put("inner", innermap);
        return CommonResult.success(outermap);
    }


    private static Map<String, String> convertRequestParamsToMap(HttpServletRequest request) {
        Map<String, String> retMap = new HashMap<String, String>();

        Set<Map.Entry<String, String[]>> entrySet = request.getParameterMap().entrySet();

        for (Map.Entry<String, String[]> entry : entrySet) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            int valLen = values.length;

            if (valLen == 1) {
                retMap.put(name, values[0]);
            } else if (valLen > 1) {
                StringBuilder sb = new StringBuilder();
                for (String val : values) {
                    sb.append(",").append(val);
                }
                retMap.put(name, sb.toString().substring(1));
            } else {
                retMap.put(name, "");
            }
        }

        return retMap;
    }

}
