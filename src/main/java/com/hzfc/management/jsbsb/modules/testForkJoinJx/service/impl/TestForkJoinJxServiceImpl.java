package com.hzfc.management.jsbsb.modules.testForkJoinJx.service.impl;

import com.google.common.collect.Lists;
import com.hzfc.management.jsbsb.modules.testDuoxianchenJx.dto.*;
import com.hzfc.management.jsbsb.modules.testForkJoinJx.jxtask.JxTask;
import com.hzfc.management.jsbsb.modules.testForkJoinJx.service.TestForkJoinJxService;
import com.hzfc.management.jsbsb.utils.dateUtils.DateUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.*;

/**
 * 后台管理员管理Service实现类
 * Created by hzfc on 2018/4/26.
 */
@Service
public class TestForkJoinJxServiceImpl implements TestForkJoinJxService {

    @Override
    public void test() throws ExecutionException, InterruptedException {
        //		代码运行开始时间
        Long startTime = System.currentTimeMillis();


        TprJxjg tprJxjg = new TprJxjg();
        tprJxjg.setJxbz(0);
        tprJxjg.setNd("2022");
        LocalDate thisDay = LocalDate.now();
        LocalDate lastyear = thisDay.minusYears(1);
        LocalDate lastDayOfYear = lastyear.with(TemporalAdjusters.lastDayOfYear());
        LocalDate lastlastDayOfYear = lastDayOfYear.minusYears(1);

        Date lastDayOfYearDate = DateUtil.localDate2Date(lastDayOfYear);
        Date llastlastDayOfYearDate = DateUtil.localDate2Date(lastlastDayOfYear);
        tprJxjg.setScjxr(lastDayOfYearDate);
        LocalDate DayOfYear = thisDay.with(TemporalAdjusters.lastDayOfYear());
        Date DayOfYearDate = DateUtil.localDate2Date(DayOfYear);
        //Map<String, List<TprJxzhzjbd>> wxjZjlistMap = Maps.newHashMapWithExpectedSize(16);
        ArrayList<List<TprJxzhzjbd>> wxjZjlistList = Lists.newArrayListWithExpectedSize(10000);
        for (int i = 1; i < 1000000; i++) {
            ArrayList<TprJxzhzjbd> tprJxzhzjbdList = Lists.newArrayListWithExpectedSize(10);
            for (int j = 5; j > 0; j--) {// 10 ，-20 ，30，-40 , 50
                TprJxzhzjbd tprJxzhzjbd = new TprJxzhzjbd();
                tprJxzhzjbd.setZhcode(i + "");
                tprJxzhzjbd.setBdhzhye(new BigDecimal(j * 10 + ""));
                tprJxzhzjbd.setZhbdlx(j);
                int xxx = j;
                if (j % 2 == 0) {
                    xxx = -j;
                }
                tprJxzhzjbd.setBdje(new BigDecimal(xxx + ""));
                tprJxzhzjbd.setBdsj(DateUtil.localDate2Date(lastDayOfYear.plusMonths(5 - j)));
                tprJxzhzjbdList.add(tprJxzhzjbd);
            }
            TprJxzhzjbd tprJxzhzjbd = new TprJxzhzjbd();
            tprJxzhzjbd.setZhcode(i + "");
            tprJxzhzjbd.setBdhzhye(new BigDecimal(100 + ""));
            tprJxzhzjbd.setZhbdlx(1);
            int xxx = -3;
            tprJxzhzjbd.setBdje(new BigDecimal(xxx + ""));
            tprJxzhzjbd.setBdsj(DateUtil.localDate2Date(lastDayOfYear.plusMonths(6)));
            tprJxzhzjbdList.add(tprJxzhzjbd);
            wxjZjlistList.add(tprJxzhzjbdList);
        }


        ///
        //采用fork/join方式将数组求和任务进行拆分执行，最后合并结果
        JxTask ls = new JxTask(wxjZjlistList,
                tprJxjg, thisDay, lastlastDayOfYear, lastDayOfYearDate, DayOfYearDate);
        ForkJoinPool fjp = new ForkJoinPool(100); //使用的线程数
        ForkJoinTask<List<ZhThjxDto>> task = fjp.submit(ls);
        List<ZhThjxDto> zhThjxDtos = task.get();
        System.out.println("forkjoin sum=" + zhThjxDtos.size());

        if (task.isCompletedAbnormally()) {
            System.out.println(task.getException());
        }

        fjp.shutdown();


        //		代码运行结束时间
        Long endTime = System.currentTimeMillis();
//		计算并打印耗时
        Long tempTime = (endTime - startTime);
        System.out.println("开支单列表查询花费时间：" +
                (((tempTime / 86400000) > 0) ? ((tempTime / 86400000) + "d") : "") +
                ((((tempTime / 86400000) > 0) || ((tempTime % 86400000 / 3600000) > 0)) ? ((tempTime % 86400000 / 3600000) + "h") : ("")) +
                ((((tempTime / 3600000) > 0) || ((tempTime % 3600000 / 60000) > 0)) ? ((tempTime % 3600000 / 60000) + "m") : ("")) +
                ((((tempTime / 60000) > 0) || ((tempTime % 60000 / 1000) > 0)) ? ((tempTime % 60000 / 1000) + "s") : ("")) +
                ((tempTime % 1000) + "ms"));

    }


}

