package com.hzfc.management.jsbsb;

import com.hzfc.management.jsbsb.utils.PageUtil;
import com.hzfc.management.jsbsb.utils.redis.MyRedisCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTests {

    @Autowired
    private MyRedisCache redisCache;

    @Test
    public void testRedis() throws IOException, InterruptedException, BrokenBarrierException {

        String feedVersion = String.valueOf(System.currentTimeMillis());
        List<Long> cookbookIdList = new ArrayList<Long>();
        cookbookIdList.add(2L);
        cookbookIdList.add(4L);
        cookbookIdList.add(1L);
        cookbookIdList.add(7L);
        cookbookIdList.add(9L);
        cookbookIdList.add(8L);
        String formatkey = String.format("HOME_FEED_KEY:%s", feedVersion);
        String[] strings = cookbookIdList.stream().map(Object::toString).toArray(String[]::new);
        // feed流中存储的菜谱ID集合
        redisCache.rPushAll(formatkey, strings);


        int start = PageUtil.pageStart(1, 3);
        int end = PageUtil.redisPageEnd(1, 3);
        List<String> cookIdList = redisCache.lRange(formatkey, start, end);
        System.out.println();
    }

    @Test
    public void testRedis2() throws IOException, InterruptedException, BrokenBarrierException {


        // redisCache.hPut("myhash", "2","3");
        Long myhash = redisCache.hLen("myhash");
        //redisCache.zadd("myzadd","m2",2D);
        //redisCache.zadd("myzadd","m1",1D);
        //Set myzadd = redisCache.zrangeByScore2("myzadd", 0, 3);
        System.out.println();
    }
}
