package com.hzfc.management.jsbsb.modules.testBloomFilter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.util.concurrent.TimeUnit;

public class BloomFilterDemo {

    private static int size = 100;

    private static BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), size);

    public static void main(String[] args) {

        int randomNum;

        for (int i = 0; i < size; i++) {
            randomNum = (int) (Math.random() * 100);
            bloomFilter.put(randomNum);
        }

        long startTime = System.nanoTime(); // 获取开始时间
        int count = 0;
        for (int i = 0; i < size; i++) {
            /*
             * ns（nanosecond）：纳秒， 时间单位。一秒的10亿分之一，即等于10的负9次方秒。常用作 内存读写速度的单位。
             * 1纳秒=0.000001 毫秒
             * 1纳秒=0.00000 0001秒
             */

            if (bloomFilter.mightContain(i)) {
                count++;
                System.out.println("命中了" + i);
            }
        }
        long endTime = System.nanoTime(); // 获取结束时间
        System.out.println("共命中：" + count + "次 ， 程序运行时间： " + (endTime - startTime) + "纳秒" + " ， 等价于  " + TimeUnit.MILLISECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS) + " 毫秒 ||  "
                + TimeUnit.SECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS) + "秒");
    }


}
