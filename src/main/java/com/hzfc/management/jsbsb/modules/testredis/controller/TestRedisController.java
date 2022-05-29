package com.hzfc.management.jsbsb.modules.testredis.controller;


import com.hzfc.management.jsbsb.common.api.CommonResult;
import com.hzfc.management.jsbsb.utils.PageUtil;
import com.hzfc.management.jsbsb.utils.redis.MyRedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 测试结息
 *
 * @author Administrator
 */
@RequestMapping("/testRedis")
@RestController
public class TestRedisController {


    @Autowired
    private MyRedisCache redisCache;

    @RequestMapping(value = "/testRedis", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<Map> testjf() {


        String feedVersion = String.valueOf(System.currentTimeMillis());
        List<Long> cookbookIdList = new ArrayList<Long>();
        cookbookIdList.add(2L);
        cookbookIdList.add(4L);
        cookbookIdList.add(1L);
        cookbookIdList.add(7L);
        cookbookIdList.add(9L);
        cookbookIdList.add(8L);
        String formatkey = String.format("HOME_FEED_KEY:%s", feedVersion);
        // feed流中存储的菜谱ID集合
        redisCache.rPushAll(formatkey, cookbookIdList.stream().map(Object::toString).toArray(String[]::new));


        int start = PageUtil.pageStart(1, 3);
        int end = PageUtil.redisPageEnd(1, 3);
        List<String> cookIdList = redisCache.lRange(formatkey, start, end);
        return CommonResult.success(null);
    }

}
