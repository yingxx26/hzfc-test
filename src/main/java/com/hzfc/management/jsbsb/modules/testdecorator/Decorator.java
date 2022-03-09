package com.hzfc.management.jsbsb.modules.testdecorator;

import java.util.Date;

public abstract class Decorator extends MyComponent {

    protected MyComponent c;

    public MyComponent getC() {
        return c;
    }

    public void setC(MyComponent c) {
        this.c = c;
    }

    @Override
    public double calcPrise(String user, Date begin, Date end) {
        return c.calcPrise(user,begin,end);
    }
}
