package com.hzfc.management.jsbsb.modules.testdontaidaili;

import java.io.IOException;

/**
 * @Author yxx
 * @Date 2021/3/16 9:50
 */
public class Student {

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    private String name;

    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) throws IOException {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
