package com.hzfc.management.jsbsb;

import com.hzfc.management.jsbsb.modules.testresponsibilitychain2.Handler;
import com.hzfc.management.jsbsb.modules.testresponsibilitychain2.ResponsibilitychainManageer2;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ResponsibilitychainTest {

    @Autowired
    private Map<String, Handler> handlerMap;

    @Autowired
    private ResponsibilitychainManageer2 responsibilitychainManageer2;

    @Test
    public void responsibilitychainTest() throws Exception {
        List<Handler> collect = handlerMap.values().stream().collect(Collectors.toList());
        Handler result = null;

        for (int i = 1; i < collect.size(); i++) {
            Handler pre = collect.get(i - 1);
            Handler cur = collect.get(i);
            cur.setSuccessor(pre);
            result = cur;
        }
        String s = result.handlerFeeRequset("小李", 600);

        log.debug("结果:{}", s);
    }


    @Test
    public void Test2() throws Exception {

        String s = responsibilitychainManageer2.handlerFeeRequset("小李", 600);

        log.debug("结果:{}", s);
    }
}
