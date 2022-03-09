package com.hzfc.management.jsbsb.modules.testdecorator;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component("ConcreteComponent")
public   class ConcreteComponent extends MyComponent {
    public    double calcPrise(String user, Date begin, Date end) {
        return  0;
    }
}
