package com.hzfc.management.jsbsb;

import com.hzfc.management.jsbsb.modules.testDuoxianchenJx.service.TestDuoxcService;
import com.hzfc.management.jsbsb.modules.testForkJoinJx.service.TestForkJoinJxService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SoarForkJoinJxTests {


    @Autowired
    private TestForkJoinJxService testForkJoinJxService;


    @Test
    public void testJf() throws IOException, ExecutionException, InterruptedException {

        testForkJoinJxService.test();

    }
}
