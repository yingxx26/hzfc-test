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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.l;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HzfcApplicationTests {
    @Autowired
    private TestService testService;

    @Autowired
    private TestUserService testUserService;

    @Autowired
    private TestUser2Service testUser2Service;


    @Autowired
    private TestJfService testJfService;

    @Test
    public void contextLoads() throws ScriptException {

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        String value = "";

        String formulaValue = "14/7*100+2";
        value = engine.eval(formulaValue).toString();
        System.out.println();

    }

    @Test
    public void testUser() throws IOException {

        User user = new User();
        user.setName("user1");

        testUserService.testSaveUser(user);


    }

    @Test
    public void testUser2() throws IOException {


        User2 user2 = new User2();
        user2.setName("user2");
        testUser2Service.testSaveUser2(user2);

    }

    @Test
    public void testsw1() throws IOException {

        User user = new User();
        user.setName("user1");

        testUserService.testSaveUser(user);

    }


    @Test
    public void testdate() throws IOException {

        LocalDate thisDay = LocalDate.now();
        LocalDate thismonthfirstDay = thisDay.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate localDate = thismonthfirstDay.minusDays(5); // 2022/3/27
        LocalDate startdate = localDate.minusMonths(2);// 2022/1/27
        LocalDate enddate = thisDay;// 2022/4/20
        long absm = Math.abs(ChronoUnit.MONTHS.between(startdate, enddate));
        BigDecimal absmDecimal = new BigDecimal(absm);

        int startAlldays = startdate.lengthOfMonth();//31
        LocalDate startdatelastDay = startdate.with(TemporalAdjusters.lastDayOfMonth());// 2022/1/31
        long abs = Math.abs(ChronoUnit.DAYS.between(startdate, startdatelastDay));
        BigDecimal absDecimal = new BigDecimal(abs);
        BigDecimal startAlldaysDecimal = new BigDecimal(startAlldays);
        BigDecimal start = absDecimal.divide(startAlldaysDecimal, 2, BigDecimal.ROUND_HALF_UP);

        int endAlldays = enddate.lengthOfMonth();//31
        LocalDate enddatefirstDay = enddate.with(TemporalAdjusters.firstDayOfMonth());// 2022/4/1
        long abs2 = Math.abs(ChronoUnit.DAYS.between(enddatefirstDay, enddate));
        BigDecimal abs2Decimal = new BigDecimal(abs2);
        BigDecimal endAlldaysDecimal = new BigDecimal(endAlldays);
        BigDecimal end = abs2Decimal.divide(endAlldaysDecimal, 2, BigDecimal.ROUND_HALF_UP);


        BigDecimal res = absmDecimal.add(start).add(end);
        System.out.println(res);
    }


}
