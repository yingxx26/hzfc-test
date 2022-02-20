package com.hzfc.management.jsbsb;

import com.hzfc.management.jsbsb.modules.test.service.TestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HzfcApplicationTests {
    @Autowired
    private TestService testService;


    @Test
    public void contextLoads() {
    }

    @Test
    public void mytest() throws IOException {

        testService.test();

    }
}
