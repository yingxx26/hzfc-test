package com.hzfc.management.jsbsb;


import com.hzfc.management.jsbsb.modules.testdontaidaili.DynamicProxy;
import com.hzfc.management.jsbsb.modules.testdontaidaili.IStudentService;
import com.hzfc.management.jsbsb.modules.testdontaidaili.Student;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author yxx
 * @Date 2021/3/16 9:53
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DynamicTest {

    @Autowired
    IStudentService studentService;

    @Test
    public void test() {


        InvocationHandler invocationHandler = new DynamicProxy(studentService);

        IStudentService proxy = (IStudentService) Proxy.newProxyInstance(studentService.getClass().getClassLoader(), studentService.getClass().getInterfaces(), invocationHandler);

        Student student = proxy.createStudent("李胜涛", 22);

        System.out.println(student);
    }


}
