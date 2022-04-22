package com.hzfc.management.jsbsb;

import com.hzfc.management.jsbsb.modules.testJiaofei.dto.FeeDto;
import com.hzfc.management.jsbsb.modules.testJiaofei.dto.RoomDto;
import com.hzfc.management.jsbsb.modules.testJiaofei.service.TestJfService;
import com.hzfc.management.jsbsb.modules.testJx.service.TestService;
import com.hzfc.management.jsbsb.utils.dateUtils.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SoarTests {


    @Autowired
    private TestService testService;


    @Test
    public void testJf() throws IOException {

        testService.test();

    }
}
