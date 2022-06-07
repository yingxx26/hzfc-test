package com.hzfc.management.jsbsb;

import com.alibaba.fastjson.JSON;
import com.hzfc.management.jsbsb.utils.PageUtil;
import com.hzfc.management.jsbsb.utils.redis.MyRedisCache;
import com.hzfc.management.jsbsb.vo.Constant;
import com.hzfc.management.jsbsb.vo.Coupon;
import com.hzfc.management.jsbsb.vo.CouponStatus;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTests {

    @Autowired
    private MyRedisCache redisCache;

    @Autowired
    private StringRedisTemplate redisTemplate;

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
        // Long myhash = redisCache.hLen("myhash");
        //boolean myhash = redisCache.hFieldExists("myhash", "3");
        //redisCache.zadd("myzadd","m2",2D);
        //redisCache.zadd("myzadd","m1",1D);
        Set myzadd = redisCache.zrangeByScore2("myzadd", 0, 3);
        Set myzadd2 = redisCache.zrevrange("myzadd", 0, 3);
        System.out.println();
    }


    @Test
    public void testRedis3领取() throws IOException, InterruptedException, BrokenBarrierException {

        redisCache.lPushAll("imooc_coupon_template_code_1", "3520220600000001", "3520220600000002");
        String couponCode = redisTemplate.opsForList().leftPop("imooc_coupon_template_code_1");
        System.out.println();

        Coupon newCoupon = new Coupon(
                11,
                1,
                111L,
                couponCode,
                CouponStatus.USABLE
        );
        List<Coupon> couponList = Collections.singletonList(newCoupon);
        Map<String, String> needCachedObject = new HashMap<>(couponList.size());
        couponList.forEach(coupon ->
                // key 是 coupon_id，value是序列化的coupon
                needCachedObject.put(
                        coupon.getId().toString(),
                        JSON.toJSONString(coupon)
                ));
        String redisKey = status2RedisKey(CouponStatus.USABLE.getCode(), 111L);

        redisTemplate.opsForHash().putAll(redisKey, needCachedObject);
        System.out.println();
    }

    @Test
    public void testRedis4使用() throws IOException, InterruptedException, BrokenBarrierException {


        Coupon newCoupon = new Coupon(
                11,
                1,
                111L,
                "3520220600000002",
                CouponStatus.USED
        );
        List<Coupon> couponList = Collections.singletonList(newCoupon);

        Map<String, String> needCachedForUsed = new HashMap<>(couponList.size());

        String redisKeyForUsable = status2RedisKey(CouponStatus.USABLE.getCode(), 111L);
        String redisKeyForUsed = status2RedisKey(CouponStatus.USED.getCode(), 111L);

        //获取当前用户可用的优惠券
        List<Coupon> currentUsableCouponList = getCachedCoupons(111L, CouponStatus.USABLE.getCode());

        // 可用的优惠券数量 一定大于 将已使用的
      // assert currentUsableCouponList.size() > couponList.size();

        List<Integer> paramUsedIdList = couponList.stream()
                .map(Coupon::getId)
                .collect(Collectors.toList());


        List<String> needCleanKey = paramUsedIdList.stream()
                .map(Object::toString)
                .collect(Collectors.toList());

        couponList.forEach(coupon -> needCachedForUsed.put(coupon.getId().toString(), JSON.toJSONString(coupon)));

        SessionCallback<Object> sessionCallback = new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                //1、已使用的优惠券 Cache 缓存添加
                redisOperations.opsForHash().putAll(redisKeyForUsed, needCachedForUsed);

                //2、可用的优惠券 Cache 需要清理
                redisOperations.opsForHash().delete(redisKeyForUsable, needCleanKey.toArray());

             /*   //3、重置过期时间
                redisOperations.expire(redisKeyForUsable, getRandomExpirationTime(1, 2), TimeUnit.SECONDS);

                redisOperations.expire(redisKeyForUsed, getRandomExpirationTime(1, 2), TimeUnit.SECONDS);*/

                return null;
            }
        };
        redisTemplate.executePipelined(sessionCallback);
        System.out.println();
    }

    private String status2RedisKey(Integer status, Long userId) {
        String redisKey = null;
        CouponStatus couponStatus = CouponStatus.of(status);

        switch (couponStatus) {
            case USED:
                redisKey = String.format("%s%s", Constant.RedisPrefix.USER_COUPON_USED, userId);
                break;
            case USABLE:
                redisKey = String.format("%s%s", Constant.RedisPrefix.USER_COUPON_USABLE, userId);
                break;
            case EXPIRED:
                redisKey = String.format("%s%s", Constant.RedisPrefix.USER_COUPON_EXPIRED, userId);
                break;
        }
        return redisKey;
    }

    /**
     * 根据 suerId 和 状态 找到缓存的优惠券列表数据
     *
     * @param userId 用户id
     * @param status 优惠券状态
     * @return
     */
    public List<Coupon> getCachedCoupons(Long userId, Integer status) {


        String redisKey = status2RedisKey(status, userId);

        List<String> couponStrList = redisTemplate.opsForHash().values(redisKey).stream()
                .map(obj -> Objects.toString(obj, null))
                .collect(Collectors.toList());

        // 没有获取到就保存空的优惠券列表到缓存中，避免缓存穿透
        if (CollectionUtils.isEmpty(couponStrList)) {
            //saveEmptyCouponListToCache(userId, Collections.singletonList(status));
            return Collections.emptyList();
        }
        return couponStrList.stream()
                .map(couponStr -> JSON.parseObject(couponStr, Coupon.class))
                .collect(Collectors.toList());
    }
}
