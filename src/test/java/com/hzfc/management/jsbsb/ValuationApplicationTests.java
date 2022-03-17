package com.hzfc.management.jsbsb;


import com.hzfc.management.jsbsb.modules.testOnlineTaxiJiJia.basedto.DriveMeter;
import com.hzfc.management.jsbsb.modules.testOnlineTaxiJiJia.constatnt.ChargingCategoryEnum;
import com.hzfc.management.jsbsb.modules.testOnlineTaxiJiJia.dto.map.Route;
import com.hzfc.management.jsbsb.modules.testOnlineTaxiJiJia.dto.valuation.charging.*;
import com.hzfc.management.jsbsb.modules.testOnlineTaxiJiJia.entity.Order;
import com.hzfc.management.jsbsb.modules.testOnlineTaxiJiJia.service.ValuationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ValuationApplicationTests {

    @Autowired
    private ValuationService valuationService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void mytest() throws IOException, ParseException {
        DriveMeter driveMeter=new DriveMeter(ChargingCategoryEnum.Forecast);
        Rule rule =new Rule();
        KeyRule keyRule=new KeyRule();
        keyRule.setCityCode("1");
        rule.setKeyRule(keyRule);

        BasicRule basicRule=new BasicRule();
        basicRule.setBasePrice(new BigDecimal("10"));
        basicRule.setLowestPrice(new BigDecimal("1"));
        basicRule.setKilos(10D);
        basicRule.setMinutes(10D);
        rule.setBasicRule(basicRule);

        PriceRule priceRule =new PriceRule();
        priceRule.setPerKiloPrice(new BigDecimal("2"));
        priceRule.setPerMinutePrice(new BigDecimal("2"));
        TimeRule timeRule=new TimeRule();
        timeRule.setStart(23);
        timeRule.setEnd(1);
        timeRule.setPerKiloPrice(new BigDecimal("3"));
        timeRule.setPerMinutePrice(new BigDecimal("3"));

        TimeRule timeRule1=new TimeRule();
        timeRule1.setStart(1);
        timeRule1.setEnd(2);
        timeRule1.setPerKiloPrice(new BigDecimal("4"));
        timeRule1.setPerMinutePrice(new BigDecimal("4"));
        List<TimeRule> timeRuleList=new ArrayList<TimeRule>();
        timeRuleList.add(timeRule);
        timeRuleList.add(timeRule1);
        priceRule.setTimeRules(timeRuleList);
        rule.setPriceRule(priceRule);


        BeyondRule  beyondRule =new BeyondRule();
        beyondRule.setStartKilo(20D);
        beyondRule.setPerKiloPrice(new BigDecimal("5"));
        rule.setBeyondRule(beyondRule);


        NightRule nightRule=new NightRule();
        nightRule.setStart(strToDate2("02:00:00"));
        nightRule.setEnd(strToDate2("03:00:00"));
        nightRule.setPerKiloPrice(new BigDecimal("1"));
        nightRule.setPerMinutePrice(new BigDecimal("1"));
        rule.setNightRule(nightRule);
        driveMeter.setRule(rule);


        Order order=new Order();
        order.setStartTime(strToDate("2021-12-28 23:20:00"));
        order.setOrderStartTime(strToDate("2021-12-28 23:21:00"));
        order.setDriverStartTime(strToDate("2021-12-28 23:22:00"));
        order.setDriverArrivedTime(strToDate("2021-12-28 23:23:00"));
        order.setReceivePassengerTime(strToDate("2021-12-28 23:24:00"));
        order.setDriverGrabTime(strToDate("2021-12-28 23:25:00"));
        order.setPickUpPassengerTime(strToDate("2021-12-28 23:26:00"));
        order.setPassengerGetoffTime(strToDate("2021-12-28 23:27:00"));
        driveMeter.setOrder(order);


        Route route =new Route();
        route.setDistance(2000D);
        route.setDuration(4000D);
        driveMeter.setRoute(route);


        valuationService.generatePriceMeter_yxxtest(driveMeter);
    }

    public static Date strToDate(String strDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        ParsePosition pos = new ParsePosition(0);

        Date strtodate = formatter.parse(strDate);
        return strtodate;

    }
    public static Date strToDate2(String strDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

        ParsePosition pos = new ParsePosition(0);

        Date strtodate = formatter.parse(strDate);
        return strtodate;

    }
}
