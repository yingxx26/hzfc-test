package com.hzfc.management.jsbsb.modules.testdecorator;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component("SumPrizeDecorator")
public class SumPrizeDecorator extends Decorator {

    @Override
    public double calcPrise(String user, Date begin, Date end) {
        double money = super.calcPrise(user, begin, end);
        double prize =1000000*0.01;
        System.out.println(user+"累计奖金"+prize);
        return money+prize;
    }
}
