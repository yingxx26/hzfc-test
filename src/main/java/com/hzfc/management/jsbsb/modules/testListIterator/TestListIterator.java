package com.hzfc.management.jsbsb.modules.testListIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * @Author yxx
 * @Date 2022/4/28 10:31
 */
public class TestListIterator {

    public static void main(String[] args) {
        List<String> e = new ArrayList<String>();
        e.add("a");
        e.add("b");
        e.add("c");
        System.out.println("通过List集合自身特有的ListIterator迭代器遍历");
        //ListItrerator接口继承了Iterator接口，拥有自己的特有功能，逆序遍历等
        ListIterator<String> t2 = e.listIterator();
        while (t2.hasNext()) {
            System.out.println(t2.next());
        }
        System.out.println("通过ListIterator接口的特有功能实现逆序输出");
        ListIterator<String> t3 = e.listIterator();
        while (t3.hasNext()) {
            System.out.println(t3.next());
            if (!t3.hasNext()) {
                System.out.println("开始逆序输出");
                while (t3.hasPrevious()) {
                    //判断上一个元素是否存在
                    System.out.println(t3.previous());
                }
                break;
            }
        }
    }

}
