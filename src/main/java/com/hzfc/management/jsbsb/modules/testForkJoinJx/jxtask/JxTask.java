package com.hzfc.management.jsbsb.modules.testForkJoinJx.jxtask;

import com.hzfc.management.jsbsb.modules.testDuoxianchenJx.constant.JxConstants;
import com.hzfc.management.jsbsb.modules.testDuoxianchenJx.dto.*;
import com.hzfc.management.jsbsb.utils.dateUtils.DateUtil;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.RecursiveTask;

/**
 * RecursiveTask 并行计算，同步有返回值
 * ForkJoin框架处理的任务基本都能使用递归处理，比如求斐波那契数列等，但递归算法的缺陷是：
 * 一只会只用单线程处理，
 * 二是递归次数过多时会导致堆栈溢出；
 * ForkJoin解决了这两个问题，使用多线程并发处理，充分利用计算资源来提高效率，同时避免堆栈溢出发生。
 * 当然像求斐波那契数列这种小问题直接使用线性算法搞定可能更简单，实际应用中完全没必要使用ForkJoin框架，
 * 所以ForkJoin是核弹，是用来对付大家伙的，比如超大数组排序。
 * 最佳应用场景：多核、多内存、可以分割计算再合并的计算密集型任务
 */
public class JxTask extends RecursiveTask<List<ZhThjxDto>> {

    static final int SEQUENTIAL_THRESHOLD = 2;
    static final long NPS = (1000L * 1000 * 1000);
    static final boolean extraWork = true; // change to add more than just a sum


    /*int low;
    int high;*/
    List<List<TprJxzhzjbd>> array;

    TprJxjg tprJxjg;
    LocalDate thisDay;
    LocalDate lastlastDayOfYear;
    Date lastDayOfYearDate;
    Date dayOfYearDate;


    public JxTask(List<List<TprJxzhzjbd>> arr, TprJxjg tprJxjgvo, LocalDate thisDayvo, LocalDate lastlastDayOfYearvo, Date lastDayOfYearDatevo, Date dayOfYearDatevo
    ) {
        array = arr;
        tprJxjg = tprJxjgvo;
        thisDay = thisDayvo;
        lastlastDayOfYear = lastlastDayOfYearvo;
        lastDayOfYearDate = lastDayOfYearDatevo;
        dayOfYearDate = dayOfYearDatevo;
    }

    /**
     * fork()方法：将任务放入队列并安排异步执行，一个任务应该只调用一次fork()函数，除非已经执行完毕并重新初始化。
     * tryUnfork()方法：尝试把任务从队列中拿出单独处理，但不一定成功。
     * join()方法：等待计算完成并返回计算结果。
     * isCompletedAbnormally()方法：用于判断任务计算是否发生异常。
     */
    public List<ZhThjxDto> compute() {

        if (array.size() <= SEQUENTIAL_THRESHOLD) {
            List<ZhThjxDto> zhThjxDtos = m2(tprJxjg, thisDay, lastlastDayOfYear, lastDayOfYearDate, dayOfYearDate, array);
            for (ZhThjxDto zh : zhThjxDtos) {
                // System.out.println("账号=" + zh.getZhcode());
            }
            // System.out.println("=======================");
            return zhThjxDtos;
        } else {
            int mid = array.size() / 2;
            List<List<TprJxzhzjbd>> listsleft = array.subList(0, mid);
            List<List<TprJxzhzjbd>> listsright = array.subList(mid, array.size());

            JxTask left = new JxTask(listsleft, tprJxjg, thisDay, lastlastDayOfYear, lastDayOfYearDate, dayOfYearDate);
            JxTask right = new JxTask(listsright, tprJxjg, thisDay, lastlastDayOfYear, lastDayOfYearDate, dayOfYearDate);
            left.fork();
            right.fork();
            List<ZhThjxDto> rightAns = right.join();
            List<ZhThjxDto> leftAns = left.join();
            leftAns.addAll(rightAns);
            return leftAns;
        }
    }

    public List<ZhThjxDto> m2(TprJxjg tprJxjg, LocalDate thisDay, LocalDate lastlastDayOfYear, Date lastDayOfYearDate, Date dayOfYearDate, List<List<TprJxzhzjbd>> array) {

        List<ZhThjxDto> zhThjxDtos = mainFunction(tprJxjg, thisDay, lastlastDayOfYear, lastDayOfYearDate, dayOfYearDate, array);

        return zhThjxDtos;

    }


    private List<ZhThjxDto> mainFunction(TprJxjg tprJxjg, LocalDate thisDay, LocalDate lastlastDayOfYear, Date lastDayOfYearDate, Date dayOfYearDate, List<List<TprJxzhzjbd>> wxjZjlistList) {

        List<ZhThjxDto> zhThjxDtoList = new ArrayList<ZhThjxDto>();
        for (int i = 0; i < wxjZjlistList.size(); i++) {
            List<TprDqjxgs> dqgslist = new ArrayList();
            List<TprHqjxgs> hqgslist = new ArrayList();
            //维修金 重要逻辑
            List<TprJxzhzjbd> tprJxzhzjbdList = wxjZjlistList.get(i);
            String zhcode = tprJxzhzjbdList.get(0).getZhcode(); //获取zhcode
            if (tprJxzhzjbdList == null) {
                tprJxzhzjbdList = new ArrayList<TprJxzhzjbd>();
            }
            //tprJxjg结息结果
            TprDqjxgs dqgs = dqgsByzhcode(zhcode, tprJxjg.getJxbz(), tprJxjg.getNd(), tprJxzhzjbdList);//计算本年结息定期金额
            dqgs.setJcsj(tprJxjg.getScjxr());//以上次结息日为本次结息的定期利率计算时间
            dqgs.setJxbz(tprJxjg.getJxbz());
            dqgs.setDqcklv(new BigDecimal(0.03 + ""));
            dqgs.setJxsj(tprJxjg.getJxr());
            dqgs.setJxpc(1);
            dqgs.setZhcode(zhcode);
            //入库
            dqgslist.add(dqgs);
            List<TprHqjxgs> hqgs = hqgsByzhcode(zhcode, tprJxjg.getJxbz(), 1, dqgs.getBndqjxje(), dqgs.getSnjyje(), tprJxjg.getScjxr(), tprJxzhzjbdList, null);
            hqgslist.addAll(hqgs);


            List<Map<String, Object>> hqlv = new ArrayList<Map<String, Object>>();//活期利率
            Map<String, Object> hqlvmap = new HashMap<String, Object>();
            hqlvmap.put("qssj", DateUtil.localDate2Date(lastlastDayOfYear));
            hqlvmap.put("lv", new BigDecimal(0.3 + ""));
            hqlv.add(hqlvmap);
            BigDecimal dqlv = new BigDecimal("0.4");//定期利率
            //计算利息
            TprZhjxmx tprZhjxmx = grjxFormHqDq(999999, "2022", lastDayOfYearDate, dayOfYearDate, DateUtil.localDate2Date(thisDay), zhcode.toString(), hqlv, dqlv, hqgslist, dqgs);

            ZhThjxDto zhThjxDto = new ZhThjxDto();
            zhThjxDto.setZhcode(new Long(zhcode));
            zhThjxDto.setBnlx(tprZhjxmx.getZhlx());
            //zhThjxDto.setZlx(zhThjxDto.getBnlx().add(zhThjxDto.getSnlx()));
            zhThjxDtoList.add(zhThjxDto);
        }
        return zhThjxDtoList;
    }


    //从结息账户变动明细中查询定期的
    public TprDqjxgs dqgsByzhcode(String zhcode, Integer jxbz, String nd, List<TprJxzhzjbd> tprJxzhzjbdList) {

        Collections.sort(tprJxzhzjbdList, new Comparator<TprJxzhzjbd>() { //按bdsf排序
            @Override
            public int compare(TprJxzhzjbd vo1, TprJxzhzjbd vo2) {
                long vo11 = vo1.getBdsj().getTime();
                long vo12 = vo2.getBdsj().getTime();
                if (vo11 > vo12) {
                    return 1;
                } else if (vo11 < vo12) {
                    return -1;
                } else if (vo11 == vo12) {
                    return 0;
                }
                return 0;//默认相等
            }
        });

        TprDqjxgs tprDqjxgs = new TprDqjxgs();
        BigDecimal bndqyezdz = BigDecimal.valueOf(100);//假设初始值
        boolean scjxrdtsfyjc = true;//上次结息日当天是否有交存  应该改为：snsfjx上年是否结息
        BigDecimal snjyje = BigDecimal.valueOf(0); //TODO 上年结息日的 当天变动后余额
        BigDecimal snjyje_y = BigDecimal.valueOf(0); //上次结息日当天是否有交存
        boolean flag = false;
        for (int i = 0; i < tprJxzhzjbdList.size(); i++) {
            BigDecimal dqye = BigDecimal.valueOf(0);
            BigDecimal listBdhzhye = tprJxzhzjbdList.get(i).getBdhzhye();//变动后余额 即 当前余额
            dqye = listBdhzhye;
            Integer zhbdlx = tprJxzhzjbdList.get(i).getZhbdlx();
            BigDecimal listBdje = tprJxzhzjbdList.get(i).getBdje();//变动金额
            Date sj = tprJxzhzjbdList.get(i).getBdsj();
//            BigDecimal yye = listBdhzhye.subtract(listBdje);//元余额
            //定期利率 以本年当前余额最小值计算 以下循环取最低
            if (i == 0) {
                if (getDays(getScjxr(nd), sj) == 0) {
                    //判断是否有本金、即第一次结息，如果不是第一次，上次结息日有计算过，本年才有固定利息
                    flag = true;
                }
            }
            if (flag) {
//                if (i == 0) {
//                    bndqyezdz = listBdhzhye;//
//                    snjyje=listBdhzhye;
//                } else
                {
                    //上年结息日当天   另外还存在 多次交存使用
                    /*if (getDays(getScjxr(nd), sj) == 0) {
                        //取出当天变动后余额最低值
                        if (zhbdlx.equals(10)) {//10结息
                            bndqyezdz = bndqyezdz.add(listBdhzhye);
                            scjxrdtsfyjc = false;
                            snjyje_y = listBdhzhye;
                        } else {
                            bndqyezdz = bndqyezdz.add(listBdje);
                            if (scjxrdtsfyjc) { //默认为TRUE，如果判断已经存在有 结息的
                                scjxrdtsfyjc = true;
                                snjyje = snjyje.add(listBdje);
                            }
                        }
                    } else {*/
                    if (dqye.compareTo(bndqyezdz) > 0) {//dqye>bndqyezdz
//                        continue;
                    } else {
                        bndqyezdz = dqye;   //有使用 --变动后余额<最低值时
                    }
                    /*}*/
                }
            }
        }
        tprDqjxgs.setBndqjxje(bndqyezdz);
        if (scjxrdtsfyjc) {
            tprDqjxgs.setSnjyje(snjyje);
        } else {
            tprDqjxgs.setSnjyje(snjyje_y);
        }
        return tprDqjxgs;
    }


    public int getDays(Date start, Date end) {
        Long startTime = start.getTime();
        Long endTime = end.getTime();
        long days = (endTime - startTime) / 24 / 3600 / 1000;//24小时/3600秒/毫秒
        int dayss = Integer.parseInt(String.valueOf(days));
        return dayss;
    }


    //获取上次结息日
    public static Date getScjxr(String nd) {
        SimpleDateFormat sbf = new SimpleDateFormat("yyyy-MM-dd");
//        String jxrDate = String.valueOf(nd) + "-12-31";
        String scjxrDate = String.valueOf(Integer.valueOf(nd) - 1) + "-12-31";
        Date scjxr = null;
        try {
            scjxr = sbf.parse(scjxrDate);
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new RuntimeException("日期格式转化失败", e);
        }
        return scjxr;
    }


    //查询：从结息账户资金变动中查询活期的变动信息        sjjxr

    public List<TprHqjxgs> hqgsByzhcode(String zhcode, Integer jxbz, Integer jxpc, BigDecimal dingqje, BigDecimal snjyje, Date scjxr, List<TprJxzhzjbd> tprJxzhzjbdList, Date sjjxr) {
//        String zhzjbd = "from TprJxzhzjbd t where" +
//                " t.zhcode='" + zhcode + "' and jxbz = '" + jxbz + "' order by t.bdsj ";
//        List<TprJxzhzjbd> tprJxzhzjbdList = this.findByQuery(zhzjbd);
        if (tprJxzhzjbdList.size() == 0) {
            return new ArrayList<TprHqjxgs>();
        }
        List<TprHqjxgs> tprHqjxgsList = new ArrayList();
        Collections.sort(tprJxzhzjbdList, new Comparator<TprJxzhzjbd>() { //按bdsf排序
            @Override
            public int compare(TprJxzhzjbd vo1, TprJxzhzjbd vo2) {
                long vo11 = vo1.getBdsj().getTime();
                long vo12 = vo2.getBdsj().getTime();
                if (vo11 > vo12) {
                    return 1;
                } else if (vo11 < vo12) {
                    return -1;
                } else if (vo11 == vo12) {
                    return 0;
                }
                return 0;//默认相等
            }
        });
//        Map<Integer, TprHqjxgs> map = new HashMap() {
//        };//初始化  Map改为ArrayList
//        int xh = 0;//序号 作为计算过程标识 以0为开始
        for (int i = 0; i < 1; i++) {
            BigDecimal yye = BigDecimal.valueOf(0);
            BigDecimal bdhye = tprJxzhzjbdList.get(i).getBdhzhye();
            BigDecimal bdje = tprJxzhzjbdList.get(i).getBdje();
//            yye = bdhye.subtract(bdje);//原余额=变动后余额-变动金额 -原余额作为本次结息一开始的本金
//            if(yye.compareTo(BigDecimal.ZERO)<0){
//                yye = new BigDecimal("0");
//            }
            Date bdsj = tprJxzhzjbdList.get(i).getBdsj();
            //在循环之外 递归
            //20140919  修改 将所有上次结息日的合计 成一条 TODO
            /*if (i == 0 && getDays(bdsj, scjxr) == 0) {//上次结息日的作为本年结息的
                BigDecimal dqsyJe = bdhye.subtract(dingqje);//上次结息日的变动后余额-定期=本年本金剩余活期金额
                TprHqjxgs tprHqjxgs = new TprHqjxgs();
                tprHqjxgs.setJxbz(jxbz);
//                tprHqjxgs.setWdyje(dqsyJe);//定期剩余可作为活期的金额
                tprHqjxgs.setWdyje(snjyje.subtract(dingqje));//合计了
                tprHqjxgs.setSfdy(0);//初次 未动用JxConstsants.SFDY_WDY
                tprHqjxgs.setWdyjejcsj(bdsj);//==scjxr
                tprHqjxgs.setZhcode(zhcode);
                tprHqjxgs.setJxpc(jxpc);
                tprHqjxgsList.add(tprHqjxgs);
                break;//结束
            } else {*/
            //本年没有定期的
            TprHqjxgs tprHqjxgs = new TprHqjxgs();
            tprHqjxgs.setJxbz(jxbz);
            tprHqjxgs.setWdyje(bdje);//将所有的第一次存的当做活期本金
            tprHqjxgs.setSfdy(JxConstants.SFDY_WDY);//初次 未动用
            tprHqjxgs.setWdyjejcsj(bdsj);//==scjxr
            tprHqjxgs.setZhcode(zhcode);
            tprHqjxgs.setJxpc(jxpc);
            tprHqjxgsList.add(tprHqjxgs);
            // break;//结束
            /* }*/
        }
        for (int i = 1; i < tprJxzhzjbdList.size(); i++) {
            BigDecimal bdje = tprJxzhzjbdList.get(i).getBdje();
            Date bdsj = tprJxzhzjbdList.get(i).getBdsj();
            if (getDays(bdsj, scjxr) != 0) {   //不是上次结息日的变动 因为上面已经合并了y
                if (bdje.compareTo(BigDecimal.ZERO) >= 0) {
                    //交存
                    TprHqjxgs tprHqjxgs = new TprHqjxgs();
                    tprHqjxgs.setJxbz(jxbz);
                    tprHqjxgs.setWdyje(bdje);//交存金额
                    tprHqjxgs.setSfdy(JxConstants.SFDY_WDY);//本年之中存入的首先作为未动用，分解放在 递归中
                    tprHqjxgs.setWdyjejcsj(bdsj);
                    tprHqjxgs.setJxpc(jxpc);
                    tprHqjxgs.setZhcode(zhcode);
                    tprHqjxgsList.add(tprHqjxgs);
//                    map.put(xh, tprHqjxgs);
//                    xh++;//xh++
                } else {//第一层  每次变动<0，都把他它和上次产生的结果list 进行计算（同一账号）
                    tprHqjxgsList = fenjieSYje(tprHqjxgsList, bdje, bdsj);
                }
            }
        }
//        for (int k = 0; k < map.size(); k++) {
//            tprHqjxgsList.add(map.get(k)); //去掉map中的序号xh
//        }
        return tprHqjxgsList;
    }

    //递归  --计算活期动用金额的利息
    public List<TprHqjxgs> fenjieSYje(List<TprHqjxgs> tprHqjxgses, BigDecimal bdje, Date bdrq) {//bdje为负值
        //此过程中还需要进行循环
        tprHqjxgses = this.sortByBdsj(tprHqjxgses);//paixu
        for (int k = tprHqjxgses.size() - 1; k >= 0; k--) { //递归for循环，累次计算最近未动用的
            TprHqjxgs tprHqjxgs = tprHqjxgses.get(k);//依次计算
            if (tprHqjxgs.getSfdy().equals(JxConstants.SFDY_WDY)) {
                BigDecimal xbig = tprHqjxgs.getWdyje().add(bdje);//使用金额 + 上次存的金额  此时bdje<0
                if (xbig.compareTo(BigDecimal.ZERO) >= 0) {//够用
                    tprHqjxgses.remove(k);//拆分之前，删除未动用 :之后拆分为以下两步
                    //1\动用
                    TprHqjxgs xx = new TprHqjxgs();
                    xx.setJxbz(tprHqjxgs.getJxbz());
                    xx.setJxpc(tprHqjxgs.getJxpc());
                    xx.setDyje(bdje.multiply(BigDecimal.valueOf(-1)));//将负值变为正值
                    xx.setSfdy(JxConstants.SFDY_DY);
                    xx.setDyjejcsj(tprHqjxgs.getWdyjejcsj());//交存时间=原 交存时间
                    xx.setDysj(bdrq);//动用时间=本次使用金额时间
                    xx.setZhcode(tprHqjxgs.getZhcode());
                    tprHqjxgses.add(xx);
                    //2\未动用  拆分
                    xx = new TprHqjxgs();//
                    xx.setSfdy(JxConstants.SFDY_WDY);//原本金 剩余未动用部分继续保留
                    xx.setJxpc(tprHqjxgs.getJxpc());
                    xx.setZhcode(tprHqjxgs.getZhcode());
                    xx.setJxbz(tprHqjxgs.getJxbz());
                    xx.setWdyje(xbig); //拆分之后未动用=原未动用+使用金额（为负值）
                    xx.setWdyjejcsj(tprHqjxgs.getWdyjejcsj());
                    tprHqjxgses.add(xx);
                    //够用的情况，完成之后进行排序返回
                    tprHqjxgses = this.sortByBdsj(tprHqjxgses);
                    return tprHqjxgses;
                } else {
                    //拆分  不够用的情况
                    tprHqjxgses.remove(k);//以序号来删
                    // 之后有两步：2，增加动用金额 3，将不够的那部分,进行递归
                    //2\动用
                    TprHqjxgs xx = new TprHqjxgs();
                    xx.setJxbz(tprHqjxgs.getJxbz());
                    xx.setJxpc(tprHqjxgs.getJxpc());
                    xx.setDyje(tprHqjxgs.getWdyje());//剩余未动用的金额全部使用完 ，第三部再用其他的
                    xx.setSfdy(JxConstants.SFDY_DY);
                    xx.setDyjejcsj(tprHqjxgs.getWdyjejcsj());//
                    xx.setDysj(bdrq);
                    xx.setZhcode(tprHqjxgs.getZhcode());
                    tprHqjxgses.add(xx);
                    tprHqjxgses = this.sortByBdsj(tprHqjxgses);
                    //3\ 排序之后递归循环计算
                    BigDecimal dg_bdje = xbig;//为负值 ==不够用（差的那部分钱）
                    List<TprHqjxgs> tprHqjxgses1 = fenjieSYje(tprHqjxgses, dg_bdje, bdrq);
                    return tprHqjxgses1;//必须先return 再去分解
                }
            } else {
                continue;//不从动用的金额去使用
            }
        }
        return tprHqjxgses;
    }

    //根据变动时间排序活期结息公式过程数据
    public List<TprHqjxgs> sortByBdsj(List<TprHqjxgs> tprHqjxgses) {
        Collections.sort(tprHqjxgses, new Comparator<TprHqjxgs>() { //按bdsf排序
            @Override
            public int compare(TprHqjxgs vo1, TprHqjxgs vo2) {
                long vo1Time = 0;
                long vo2Time = 0;
                if (vo1.getSfdy() == JxConstants.SFDY_WDY) {
                    vo1Time = vo1.getWdyjejcsj().getTime();
                } else {
                    vo1Time = vo1.getDyjejcsj().getTime();
                }
                if (vo2.getSfdy() == JxConstants.SFDY_WDY) {
                    vo2Time = vo2.getWdyjejcsj().getTime();
                } else {
                    vo2Time = vo2.getDyjejcsj().getTime();
                }
                if (vo1Time > vo2Time) {
                    return 1;
                } else if (vo1Time < vo2Time) {
                    return -1;
                } else if (vo1Time == vo2Time) {
                    return 0;
                }
                return 0;//默认相等
            }
        });
        return tprHqjxgses;
    }


    /**
     * @param jxbz
     * @param nd
     * @param scjxr
     * @param jxr           年度计息日  n
     * @param jxrq          实际结息日 个人结息（非年底结息）
     * @param zhcode
     * @param hqlv
     * @param dqlv
     * @param tprHqjxgsList
     * @param dqgs
     * @return
     */
    public TprZhjxmx grjxFormHqDq(Integer jxbz, String nd, Date scjxr, Date jxr, Date jxrq, String zhcode,
                                  List<Map<String, Object>> hqlv, BigDecimal dqlv, List<TprHqjxgs> tprHqjxgsList, TprDqjxgs dqgs) {
        //将活期和定期利率提出来
        String ndts = String.valueOf(getDays(scjxr, jxr));//本年天数
        //结息明细定义
        BigDecimal yzhye = BigDecimal.valueOf(0); //初始化为0，做累计
        BigDecimal zhhqlx = BigDecimal.valueOf(0);
        BigDecimal zhdqlx = BigDecimal.valueOf(0);
        BigDecimal glfy = BigDecimal.valueOf(0);
        BigDecimal gdlv = BigDecimal.valueOf(0);
        BigDecimal jxsDqje = BigDecimal.valueOf(0); //结息时当前金额
        //        -----------------------     开始计算活期利息      ----------------------
        //遍历账户活期结息
        for (int k = 0; k < tprHqjxgsList.size(); k++) {
            TprHqjxgs tprHqjxgs = tprHqjxgsList.get(k);
            Integer sfdy = tprHqjxgs.getSfdy();//是否动用
            BigDecimal dyje = tprHqjxgs.getDyje();
            Date dyjejcsj = tprHqjxgs.getDyjejcsj();
            Date dysj = tprHqjxgs.getDysj();
            BigDecimal wdyje = tprHqjxgs.getWdyje();
            Date wdyjejcsj = tprHqjxgs.getWdyjejcsj();
            Date wdyjesj = jxrq;//未动用的金额计算到 结息日  实际的结息日不是12月31日
            //计算lixi
            if (sfdy == 0) { //未动用
                if (cmpDateByFormat(wdyjejcsj, scjxr)) {
                    //合计原账户余额:时间为上次结息日的
                    yzhye = yzhye.add(wdyje);//原账户余额
                }
                jxsDqje = jxsDqje.add(wdyje);//结息时当前金额
                //计算产生的活期利息 wdyjejcsj开始  wdyjesj结束
                BigDecimal hqlx = calcLx(wdyje, wdyjejcsj, wdyjesj, hqlv, ndts);
                //累计账户本年利息
                zhhqlx = zhhqlx.add(hqlx);
            } else if (sfdy == 1) { //动用
                if (cmpDateByFormat(dyjejcsj, scjxr)) {
                    if (wdyje == null) {
                        wdyje = new BigDecimal("0");
                    }
                    yzhye = yzhye.add(wdyje); //如果是上次结息日的，原账户本金
                }
                //计算产生的活期利息  dyjejcs开始  dysj结束
                BigDecimal hqlx = calcLx(dyje, dyjejcsj, dysj, hqlv, ndts);
                //累计账户本年利息
                zhhqlx = zhhqlx.add(hqlx);
            }
        }
        //        -----------------------     开始计算定期利息      ----------------------
        BigDecimal bndqjxje = dqgs.getBndqjxje();
        jxsDqje = jxsDqje.add(bndqjxje);//加定期的
        Date jcsj = dqgs.getJcsj();
        Date jxsj = dqgs.getJxsj();
        gdlv = dqlv;//本年定期利率
        Integer ts = 0;
        //此处进行校验？ 交存时间=上次计息日 && 结息时间=结息日  个人结息无定期，去掉下面一行代码，如计算定期取消注释
//                if (wyzjJxService.cmpDateByFormat(jcsj, scjxr) && wyzjJxService.cmpDateByFormat(jxsj, jxr)) {
//                    ts = getDays(scjxr, jxr);
//                }
        zhdqlx = bndqjxje.multiply(dqlv).multiply(new BigDecimal("0.01"));//计算本年定期产生利息 累计、理论上不需要 此处已经是年利率
        yzhye = yzhye.add(bndqjxje);//原账户余额

        //        -----------------------   结束定期     ----------------------
        //生成计算账户明细
        TprZhjxmx tprZhjxmx = new TprZhjxmx();
        tprZhjxmx.setJxbz(jxbz);//-------------------------
        tprZhjxmx.setJxlx(0);//结息类型返回之后在Action定义
        tprZhjxmx.setJxr(jxr);
        tprZhjxmx.setCzr(new Date());
        tprZhjxmx.setGlfy(glfy);
        tprZhjxmx.setZhcode(zhcode);
        //产生明细
        tprZhjxmx.setYzhye(jxsDqje);//结息时用户余额
        tprZhjxmx.setZhhqlx(zhhqlx.setScale(2, BigDecimal.ROUND_HALF_UP));
        tprZhjxmx.setZhdqlx(zhdqlx.setScale(2, BigDecimal.ROUND_HALF_UP));
        tprZhjxmx.setJxzlx(tprZhjxmx.getZhhqlx().add(tprZhjxmx.getZhdqlx()));//账户总利息=活期+定期
        tprZhjxmx.setZhlx(tprZhjxmx.getJxzlx().add(tprZhjxmx.getGlfy()));//账户利息=总利息+管理费用
        tprZhjxmx.setDqye(tprZhjxmx.getYzhye().add(tprZhjxmx.getZhlx()));//当前余额=原账户余额+账户利息
        tprZhjxmx.setNd(nd);
        tprZhjxmx.setGdlv(gdlv);
        tprZhjxmx.setJxzq(getDays(scjxr, jxr));//结息周期
        return tprZhjxmx;
    }

    //比较日期先后
    public boolean cmpDateByFormat(Date date1, Date date2) {
        if (date1.getYear() == date2.getYear()
                && date1.getMonth() == date2.getMonth()
                && date1.getDay() == date2.getDay()) {
            return true;
        } else {
            return false;
        }
    }

    //计算此金额在时间段内的活期利率
    public BigDecimal calcLx(BigDecimal je, Date start, Date end, List<Map<String, Object>> lv, String ndts) {
        BigDecimal lvs = BigDecimal.valueOf(0);//利率
        Integer ts = getDays(start, end);//天数
        //end只作为计算天数
        for (int k = 0; k < lv.size(); k++) {
            Date qssj = (Date) lv.get(k).get("qssj");
            BigDecimal liv = (BigDecimal) lv.get(k).get("lv");
            //lv已经在查询时候倒叙排序过， 结息的利率以交存的时候为准，那么以下是正确的计算方式
            if (start.after(qssj) || getDays(start, qssj) == 0) {//大于等于 利率的日期
//                return je.multiply(liv).multiply(BigDecimal.valueOf(ts)).multiply(JxConstants.JX_LLDW);
//                BigDecimal dwl = new BigDecimal("1").divide(new BigDecimal(ndts), 10, BigDecimal.ROUND_HALF_DOWN);
                return je.multiply(liv).multiply(BigDecimal.valueOf(ts)).multiply(new BigDecimal("0.01")).divide(new BigDecimal(ndts), 10, BigDecimal.ROUND_HALF_DOWN);
            } else {
                if (k == lv.size() - 1) {
//                    return new BigDecimal("0");//当循环结束，TODO 此处应该返回异常：利率未录入，不应该以0计算
                    throw new RuntimeException("com.hzfc.soar.wyjj.zjjx.service.WyzjJxServiceImpl.calcLx 没有" + start.toString() + "这个时间的活期利率！");
                } else {
                    continue;//
                }
            }
        }
        return new BigDecimal("0");
    }

    /*
    活期利率
       SELECT
            T .lv,
            T .qssj
        FROM
            hz_wyzj.tpr_ll T
        WHERE
            T .sfyx = 1
        AND T .lvlx = 2
        AND TO_CHAR (T .qssj, 'YYYY-MM-DD') <= '2022-12-31'
        ORDER BY
            T .qssj DESC

    * */
}

