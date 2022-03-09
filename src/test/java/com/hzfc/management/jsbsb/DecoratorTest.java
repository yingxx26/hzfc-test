package com.hzfc.management.jsbsb;


import com.hzfc.management.jsbsb.modules.testdecorator.ConcreteComponent;
import com.hzfc.management.jsbsb.modules.testdecorator.GroupPrizeDecorator;
import com.hzfc.management.jsbsb.modules.testdecorator.MonthPrizeDecorator;
import com.hzfc.management.jsbsb.modules.testdecorator.SumPrizeDecorator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DecoratorTest {

    @Autowired
    private ConcreteComponent c;

    @Autowired
    private GroupPrizeDecorator g;

    @Autowired
    private MonthPrizeDecorator m;

    @Autowired
    private SumPrizeDecorator s;

    @Test
    public void testDecorator() throws Exception{
      //任意组合 装饰器模式

        m.setC(c);
        g.setC(m);
        s.setC(g);


        double prise1 = s.calcPrise("张三", null, null);
        System.out.println("张三"+prise1);

        double prise2 = s.calcPrise("李四", null, null);
        System.out.println("李四"+prise2);

        double prise3 = s.calcPrise("王五", null, null);
        System.out.println("王五"+prise3);
    }



}
