package com.hzfc.management.jsbsb.modules.testBitmap;


/**
 * @title: 排序抽象类
 * @author: Marvin Guo
 * @create time: 2016年12月28日
 */
public abstract class SortingAlgorithm {

    public void sort(int[] numbers) {
        sort(numbers, 0, numbers.length - 1);
    }

    ;

    /**
     * sort 抽象方法
     *
     * @param numbers
     */
    public abstract void sort(int[] numbers, int p, int r);

    /**
     * 两个数字交换
     *
     * @param numbers
     * @param i
     * @param j
     */
    protected void swap(int[] numbers, int i, int j) {
        int temp = numbers[i];
        numbers[i] = numbers[j];
        numbers[j] = temp;
    }

    /**
     * 验证输入
     *
     * @param numbers
     */
    protected void validateInput(int[] numbers) {
        if (numbers == null) {
            throw new IllegalArgumentException("You can't pass a null instance as parameter.");
        }
    }

    /**
     * 获取排序时间
     *
     * @param numbers
     */
    public void getSortTime(int[] numbers) {
        //
        long startTime = System.currentTimeMillis();
        this.sort(numbers);
        //
        long endTime = System.currentTimeMillis();
        long usedTime = endTime - startTime;
        System.out.println(this.getClass().getSimpleName() + " 用时：" + usedTime + "ms");
    }

}
