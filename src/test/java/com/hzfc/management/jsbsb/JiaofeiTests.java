package com.hzfc.management.jsbsb;

import com.hzfc.management.jsbsb.modules.testJiaofei.dto.FeeDto;
import com.hzfc.management.jsbsb.modules.testJiaofei.dto.RoomDto;
import com.hzfc.management.jsbsb.modules.testJiaofei.service.TestJfService;
import com.hzfc.management.jsbsb.modules.testJx.service.TestService;
import com.hzfc.management.jsbsb.modules.testShiwu.model.User;
import com.hzfc.management.jsbsb.modules.testShiwu.model.User2;
import com.hzfc.management.jsbsb.modules.testShiwu.service.TestUser2Service;
import com.hzfc.management.jsbsb.modules.testShiwu.service.TestUserService;
import com.hzfc.management.jsbsb.utils.dateUtils.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JiaofeiTests {


    @Autowired
    private TestJfService testJfService;


    @Test
    public void testJf() throws IOException {

        FeeDto feeDto = new FeeDto();

        feeDto.setPaymentCycle("1");
        feeDto.setPaymentCd("1200");  // 1200预付费
        feeDto.setSquarePrice("20");
        feeDto.setComputingFormula("1001");
        LocalDate thisDay = LocalDate.now();
        LocalDate thismonthfirstDay = thisDay.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate localDate = thismonthfirstDay;//thismonthfirstDay.minusDays(5);
        LocalDate startdate = localDate.minusMonths(3);
        LocalDate enddate = startdate.plusMonths(1).plusDays(9);

        LocalDate configEndTime = thismonthfirstDay.plusMonths(5);
        String formatstartdate = DateUtil.format(DateUtil.localDate2Date(startdate), "yyyy-MM-dd");
        String formatenddate = DateUtil.format(DateUtil.localDate2Date(enddate), "yyyy-MM-dd");
        System.out.println("首次计费时间" + formatstartdate);
        System.out.println("计费开始时间" + formatenddate);
        String formatconfigEndTime = DateUtil.format(DateUtil.localDate2Date(configEndTime), "yyyy-MM-dd");
        System.out.println("费用项结束时间" + formatconfigEndTime);
        Date lastmonthDate = DateUtil.localDate2Date(startdate);
        Date nextmonthDate = DateUtil.localDate2Date(enddate);
        Date configEndTimeDate = DateUtil.localDate2Date(configEndTime);
        feeDto.setStartTime(lastmonthDate);
        feeDto.setConfigEndTime(configEndTimeDate);
        feeDto.setEndTime(nextmonthDate);
        feeDto.setAdditionalAmount("10");
        feeDto.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
        RoomDto roomDto = new RoomDto();
        roomDto.setBuiltUpArea("100");

        testJfService.computeFeePrice(feeDto, roomDto);
        System.out.println();
    }
}
