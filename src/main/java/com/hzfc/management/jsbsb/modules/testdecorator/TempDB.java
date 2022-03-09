package com.hzfc.management.jsbsb.modules.testdecorator;

import java.util.HashMap;
import java.util.Map;

public class TempDB {

    public static Map<String,Double> mapMoney=new HashMap<>();

    static {
        mapMoney.put("张三",10000.0);
        mapMoney.put("李四",20000.0);
        mapMoney.put("王五",30000.0);
    }
}
