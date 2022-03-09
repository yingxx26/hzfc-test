package com.hzfc.management.jsbsb.modules.testdontaidaili;

import org.springframework.stereotype.Component;

/**
 * @Author yxx
 * @Date 2021/3/16 9:51
 */
@Component(value = "studentService")
public class StudentServiceImpl implements IStudentService {

    @Override
    public Student createStudent(String name, int age) {
        Student student = new Student("lishengtao", 32);
        return student;
    }
}
