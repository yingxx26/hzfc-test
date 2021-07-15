package com.hzfc.management.yjzx.utils.numutils;

import io.swagger.models.auth.In;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;

/**
 * @Author yxx
 * @Date 2021/7/15 11:10
 */
public class NumUtils {

    public static Double DoubleFormat(Double num, Integer Scale) {
        if (ObjectUtils.isEmpty(num)) {
            return 0D;
        }
        BigDecimal source = new BigDecimal(num);
        Double result = source.setScale(Scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        return result;
    }

}
