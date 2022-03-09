package com.hzfc.management.jsbsb.modules.testdecorator;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component("MonthPrizeDecorator")

public class MonthPrizeDecorator extends Decorator {

    @Override
    public double calcPrise(String user, Date begin, Date end) {
        double money = super.calcPrise(user, begin, end);
        double prize =TempDB.mapMoney.get(user)*0.03;
        System.out.println(user+"当月业务奖金"+prize);
        return money+prize;
    }
}
