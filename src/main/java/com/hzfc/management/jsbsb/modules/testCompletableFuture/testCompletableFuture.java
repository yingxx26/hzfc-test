package com.hzfc.management.jsbsb.modules.testCompletableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @Author yxx
 * @Date 2022/3/17 15:26
 */
public class testCompletableFuture {


    public static void main(String[] args) {
        List<CompletableFuture<Integer>> futures = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            futures.add(CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("" + finalI);
                return finalI;
            }));
        }
        //等待全部完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[1])).join();

        System.out.println("完成");
    }
}
