package com.hzfc.management.jsbsb.utils.MyListUtil;

import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author yxx
 * @Date 2022/4/22 20:10
 */
public class MyListUtil {

    /**
     * Description: Java8 Stream分割list集合，此函数的作用是，传入指定的List集合和指定的数量，输出结果是新集合，新集合中包含的在若干个子集合，每个子集合的长度是splitSize
     *
     * @param list      传入的list集合
     * @param splitSize 表示每splitSize个对象分割成一组
     * @return list集合分割后的集合
     */
    public static <T> List<List<T>> splitList(List<T> list, int splitSize) {
        //判断集合是否为空
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        //计算分割后的大小
        int maxSize = (list.size() + splitSize - 1) / splitSize;
        //开始分割
        return Stream.iterate(0, n -> n + 1)
                .limit(maxSize)
                .parallel()
                .map(a -> list.parallelStream().skip(a * splitSize).limit(splitSize).collect(Collectors.toList()))
                .filter(b -> !b.isEmpty())
                .collect(Collectors.toList());
    }
}
