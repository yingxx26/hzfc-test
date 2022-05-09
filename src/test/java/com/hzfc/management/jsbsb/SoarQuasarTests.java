package com.hzfc.management.jsbsb;

import com.hzfc.management.jsbsb.modules.testDuoxianchenJx.service.TestDuoxcService;
import com.hzfc.management.jsbsb.modules.testquasarJx.service.TestQuasarervice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SoarQuasarTests {


    @Autowired
    private TestQuasarervice testQuasarervice;


    @Test
    public void testJf() throws IOException {

        testQuasarervice.test();

    }
}
