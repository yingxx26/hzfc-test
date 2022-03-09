package com.hzfc.management.jsbsb.modules.testdontaidaili;

import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;

/**
 * @Author yxx
 * @Date 2021/3/16 9:52
 */
public class DynamicProxy implements InvocationHandler {

    private Object target;

    public DynamicProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("执行开始");
        Object obj = method.invoke(target, args);
        System.out.println("执行结束");
        return obj;
    }
}
