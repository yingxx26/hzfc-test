package com.hzfc.management.jsbsb.modules.testBitmap;


/**
 * @title: bitmap排序, 线性时间处理海量大数据, 数据不能重复
 * @author: Marvin Guo
 * @create time: 2016年12月29日
 */
public class BitSort extends SortingAlgorithm {

    private final int LEN_INT = 32;

    private final int mmax = 9000000;
    private final int mmin = 0;
    private final int N = mmax - mmin + 1;

    // 默认p和r无作用
    @Override
    public void sort(int[] numbers, int p, int r) {
        int[] bitArray = setBit(numbers);
        int index = 0;
        for (int i = 0; i < N; i++) {
            int bit = getBit(bitArray, i);
            if (bit != 0) {
                numbers[index++] = i + mmin;
            }
        }
    }

    //获取数字k在位数组中是否存在，返回所在位
    public int getBit(int[] bitArray, int k) {
        int i = bitArray[k / LEN_INT];
        int j = k % LEN_INT;
        int m = 1 << j;
        return i & m; // 关键是 &
    }

    //设置位数组:
    public int[] setBit(int[] array) {
        int bit_arr_len = N / LEN_INT + 1;
        int[] bitArray = new int[bit_arr_len];

        for (int i = 0; i < array.length; i++) {
            int num = array[i] - mmin;
            int j = num % LEN_INT;
            int k = 1 << j;
            bitArray[num / LEN_INT] |= k;// 关键是 |=
        }
        return bitArray;

    }

    public static void myprint(int[] as) {
        for (int a : as) {
            System.out.println(a);
        }
    }

    public static void main(String[] args) {

        int[] a = {5, 4, 11, 9, 8, 2, 7, 14, 6, 10};
        BitSort bsort = new BitSort();
        bsort.sort(a);
        myprint(a);
        bsort.getSortTime(a);
    }

}
