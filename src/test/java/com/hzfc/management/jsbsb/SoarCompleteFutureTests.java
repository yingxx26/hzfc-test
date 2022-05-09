package com.hzfc.management.jsbsb;

import com.hzfc.management.jsbsb.modules.testCompletablefutureJx.service.TestCompletableFutureService;
import com.hzfc.management.jsbsb.modules.testDuoxianchenJx.service.TestDuoxcService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SoarCompleteFutureTests {


    @Autowired
    private TestCompletableFutureService testCompletableFutureService;


    @Test
    public void testJf() throws IOException {

        testCompletableFutureService.test();

    }
}
