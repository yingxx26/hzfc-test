package com.hzfc.management.jsbsb;

import com.hzfc.management.jsbsb.modules.testForkJoinJx.service.TestForkJoinJxService;
import com.hzfc.management.jsbsb.modules.testparallestreamJx.service.TestParallelstreamJxService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SoarparallelstreamJxTests {


    @Autowired
    private TestParallelstreamJxService testParallelstreamJxService;


    @Test
    public void testJf() throws IOException, ExecutionException, InterruptedException {

        testParallelstreamJxService.test();

    }
}
