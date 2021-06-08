package com.hzfc.management.yjzx.modules.reports.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.deepoove.poi.data.*;
import com.hzfc.management.yjzx.common.api.CommonResult;
import com.hzfc.management.yjzx.modules.reports.dto.*;
import com.hzfc.management.yjzx.modules.reports.model.*;
import com.hzfc.management.yjzx.modules.reports.service.*;
import com.hzfc.management.yjzx.utils.dateUtils.DateUtil;
import com.hzfc.management.yjzx.utils.fileutils.Base64FileUtil;
import com.hzfc.management.yjzx.utils.fileutils.DeleteFileUtil;
import com.hzfc.management.yjzx.utils.fileutils.SaveFileUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 导出Word
 *
 * @author Administrator
 */
@RequestMapping("/reports/exportWord/")
@RestController
public class ExportWordController {

    @Value("${hzfc.temporaryfile.word.path}")
    private String temporaryPath;

    @Value("${hzfc.uploadfile.wordTemplate.path}")
    private String templatePath;

    @Value("${hzfc.zhibiao.city}")
    private String city;

    @Value("${hzfc.zhibiao.hz}")
    private String hz;

    @Value("${hzfc.cq.zcq}")
    private String zcq;

    @Value("${hzfc.cq.xs}")
    private String xs;

    @Value("${hzfc.cq.yh}")
    private String yh;

    @Value("${hzfc.cq.fy}")
    private String fy;

    @Value("${hzfc.cq.la}")
    private String la;

    @Autowired
    private ReportsWordTemplateService reportsWordTemplateService;

    @Autowired
    private ZhiBiaoZzxsjgbdqkService zhiBiaoZzxsjgbdqkService;

    @Autowired
    private ZhiBiaoYhbmService zZhiBiaoYhbmService;

    @Autowired
    private ZhiBiaoSpfjyCqService zhiBiaoSpfjyCqService;

    @Autowired
    private ZhiBiaoSpfjyZhCqService zhiBiaoSpfjyZhCqService;

    @Autowired
    private ZhiBiaoSpfPzksService zhiBiaoSpfPzksService;


    @RequestMapping(value = "/exportUserWord/{templateId}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<String> exportUserWord(@PathVariable("templateId") Long templateId, @RequestBody ExportParam exportParam) {

        Map<String, Object> dataFinal = new HashMap<String, Object>();
        List<Date> dates = exportParam.getDates();

        if (CollectionUtils.isEmpty(dates) || dates.size() < 2) {
            return CommonResult.failed("请填写日期");
        }
        List<String> dateList = dates.stream().map(date -> DateUtil.format(date, "yyyy-MM-dd")).collect(Collectors.toList());
        dataFinal.put("firstDate", dateList.get(0));
        dataFinal.put("secondDate", dateList.get(1));
        LocalDate last = DateUtil.toLocalDate(dates.get(0));


        String year = last.format(DateTimeFormatter.ofPattern("yyyy"));
        String lastMM = last.format(DateTimeFormatter.ofPattern("MM"));
        dataFinal.put("lastMonth", Integer.parseInt(lastMM));
        dataFinal.put("year", year);
        ExportDataPackage exportDataPackage = new ExportDataPackage();
        showSpf(templateId, dataFinal, last, exportDataPackage);
        ReportsWordTemplate reportsWordTemplate = showClf(templateId, dataFinal, last, exportDataPackage);
        try {
            String fullTemplatePath = templatePath + reportsWordTemplate.getTemplatepath();
            String fileName = SaveFileUtil.savePoiFile(dataFinal, fullTemplatePath, temporaryPath);
            String fullTemporaryPath = temporaryPath + fileName;
            String base64 = Base64FileUtil.fileToBase64(fullTemporaryPath);
            //  这里再删除文件
            DeleteFileUtil.delete(fullTemporaryPath);
            return CommonResult.success(base64);
        } catch (Exception e) {
            return CommonResult.failed("文件异常");
        }
    }

    /**
     * 查看文件
     *
     * @param filename
     * @return
     */
    @ApiOperation("查看生成数据的文件")
    @RequestMapping(value = "/viewbyname/{filename}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<String> viewbypath(@PathVariable("filename") String filename) {

        String fullpath = temporaryPath + filename;
        String base64 = null;
        try {
            base64 = Base64FileUtil.fileToBase64(fullpath);
        } catch (Exception e) {
            return CommonResult.failed("文件异常");
        }
        return CommonResult.success(base64);
    }


    @ApiOperation("删除生成数据的文件")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {

        boolean success = reportsWordTemplateService.delete(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }


    private ReportsWordTemplate showSpf(Long templateId, Map<String, Object> dataFinal, LocalDate last, ExportDataPackage exportDataPackage) {
        ReportsWordTemplate reportsWordTemplate = this.jiageZhishu_spf(templateId, dataFinal, last, exportDataPackage);
        this.yhbm(templateId, dataFinal, last, exportDataPackage);
        this.spfcj(templateId, dataFinal, last, exportDataPackage);

        //渲染表格
        this.dealTableSpf_jiagezhishu(dataFinal, exportDataPackage);
        this.dealTableSpf_yhbm(dataFinal, exportDataPackage);
        //渲染图表
        this.dealChart_Yh(dataFinal, exportDataPackage);
        return reportsWordTemplate;
    }

    private ReportsWordTemplate showClf(Long templateId, Map<String, Object> dataFinal, LocalDate last, ExportDataPackage exportDataPackage) {
        ReportsWordTemplate reportsWordTemplate = this.jiageZhishu_clf(templateId, dataFinal, last, exportDataPackage);
        //渲染表格
        this.dealTableClf_jiagezhishu(dataFinal, exportDataPackage);
        //渲染图表
        //this.dealChart(dataFinal);
        return reportsWordTemplate;
    }

    private void spfcj(Long templateId, Map<String, Object> dataFinal, LocalDate last, ExportDataPackage exportDataPackage) {

        Date firstDayOfYearDate = DateUtil.localDate2Date(last.with(TemporalAdjusters.firstDayOfYear()));
        LocalDate lastMonthlastDay = last.with(TemporalAdjusters.lastDayOfMonth());
        Date thisMonthLastdayDate = DateUtil.localDate2Date(lastMonthlastDay);
        String firstDayOfYearDate_yyyyMM = DateUtil.format(firstDayOfYearDate, "yyyyMM");
        String thisMonthLastdayDate_yyyyMM = DateUtil.format(thisMonthLastdayDate, "yyyyMM");

        QueryWrapper<ZhiBiaoSpfjyZhCq> wrapper1 = new QueryWrapper<>();
        LambdaQueryWrapper<ZhiBiaoSpfjyZhCq> lambda1 = wrapper1.lambda();
        lambda1.ge(ZhiBiaoSpfjyZhCq::getTjsj, firstDayOfYearDate_yyyyMM);
        lambda1.le(ZhiBiaoSpfjyZhCq::getTjsj, thisMonthLastdayDate_yyyyMM);
        List<ZhiBiaoSpfjyZhCq> zhiBiaoSpfjyZhCqList = zhiBiaoSpfjyZhCqService.list(wrapper1);

        QueryWrapper<ZhiBiaoSpfjyCq> wrapper2 = new QueryWrapper<>();
        LambdaQueryWrapper<ZhiBiaoSpfjyCq> lambda2 = wrapper2.lambda();
        lambda2.ge(ZhiBiaoSpfjyCq::getTjsj, firstDayOfYearDate_yyyyMM);
        lambda2.le(ZhiBiaoSpfjyCq::getTjsj, thisMonthLastdayDate_yyyyMM);
        List<ZhiBiaoSpfjyCq> zhiBiaoSpfjyCqList = zhiBiaoSpfjyCqService.list(wrapper2);

        QueryWrapper<ZhiBiaoSpfPzks> wrapper3 = new QueryWrapper<>();
        LambdaQueryWrapper<ZhiBiaoSpfPzks> lambda3 = wrapper3.lambda();
        lambda3.ge(ZhiBiaoSpfPzks::getTjsj, firstDayOfYearDate_yyyyMM);
        lambda3.le(ZhiBiaoSpfPzks::getTjsj, thisMonthLastdayDate_yyyyMM);
        List<ZhiBiaoSpfPzks> zhiBiaoSpfPzksCqList = zhiBiaoSpfPzksService.list(wrapper3);


        Optional<ZhiBiaoSpfjyZhCq> first = zhiBiaoSpfjyZhCqList.stream().filter(x -> thisMonthLastdayDate_yyyyMM.equals(x.getTjsj()) && "本月套数".equals(x.getZbname())).findFirst();
        ZhiBiaoSpfjyZhCq zhiBiaoSpfjyZhCq_thisMonth = first.get();
        System.out.println();
       /* QueryWrapper<ZhiBiaoYhbm> wrapper2 = new QueryWrapper<>();
        LambdaQueryWrapper<ZhiBiaoYhbm> lambda2 = wrapper2.lambda();
        LocalDate quNian = last.plusYears(-1);
        Date firstDayOfYearDate_quNian = DateUtil.localDate2Date(quNian.with(TemporalAdjusters.firstDayOfYear()));
        LocalDate thisMonthFirstday_quNian = LocalDate.of(quNian.getYear(), last.getMonth(), 1);
        Date thisMonthFirstdayDate_quNian = DateUtil.localDate2Date(thisMonthFirstday_quNian);
        lambda2.ge(ZhiBiaoYhbm::getYhjssj, firstDayOfYearDate_quNian);
        lambda2.le(ZhiBiaoYhbm::getYhjssj, thisMonthFirstdayDate_quNian);
        List<ZhiBiaoYhbm> sourceListLast2 = zZhiBiaoYhbmService.list(wrapper2);
        List<ZhiBiaoYhbm> formatSourceListThis2 = Optional.ofNullable(sourceListLast2).get().stream().map(x -> {
            x.setMonth(DateUtil.format(x.getYhjssj(), "M"));
            return x;
        }).collect(Collectors.toList());

        //今年按月份 原数据
        Map<String, List<ZhiBiaoYhbm>> maplist_month_thisnian = Optional.ofNullable(formatSourceListThis).get().stream().collect(Collectors.groupingBy(ZhiBiaoYhbm::getMonth));
        //去年按月份 原数据
        Map<String, List<ZhiBiaoYhbm>> maplist_month_qunian = Optional.ofNullable(formatSourceListThis2).get().stream().collect(Collectors.groupingBy(ZhiBiaoYhbm::getMonth));

        /////////////////////////////////////按月统计////////////////////////////////////////////
        //计算今年每月总登记数
        Map<String, String> yhbm_everyMonth_djsize_Map = new HashMap<String, String>();
        //计算今年每月摇号数
        Map<String, String> yhbm_everyMonth_yhsize_Map = new HashMap<String, String>();
        //计算今年每月流摇数
        Map<String, String> yhbm_everyMonth_lysize_Map = new HashMap<String, String>();
        //计算今年每月流摇lv
        Map<String, String> yhbm_everyMonth_lylv_Map = new HashMap<String, String>();
        //计算今年每月中签率
        Map<String, String> yhbm_everyMonth_pjzql_Map = new HashMap<String, String>();

        String month = last.getMonthValue() + "";
        String lastmonth = last.getMonthValue() - 1 + "";

        List<String> yhbm_everyMonth_month_List = new ArrayList<String>();
        List<Integer> yhbm_everyMonth_yhsize_List = new ArrayList<Integer>();
        List<Integer> yhbm_everyMonth_lysize_List = new ArrayList<Integer>();
        List<Double> yhbm_everyMonth_lylv_List = new ArrayList<Double>();
        maplist_month_thisnian.forEach((k, v) -> {
            Integer yhbm_thisMonth_djsize = v.size();
            yhbm_everyMonth_djsize_Map.put(k, String.valueOf(yhbm_thisMonth_djsize));
            List<ZhiBiaoYhbm> yhbm_thisMonth = v.stream().filter(x -> x.getBmrs() != null && x.getFys() != null).filter(x -> (x.getBmrs() - x.getFys()) > 0).collect(Collectors.toList());
            Integer yhbm_thisMonth_yhsize = yhbm_thisMonth.size();
            yhbm_everyMonth_yhsize_Map.put(k, String.valueOf(yhbm_thisMonth_yhsize));
            Integer yhbm_thisMonth_lysize = yhbm_thisMonth_djsize - yhbm_thisMonth_yhsize;
            yhbm_everyMonth_lysize_Map.put(k, String.valueOf(yhbm_thisMonth_lysize));
            Double yhbm_thisMonth_lylv = new BigDecimal((float) yhbm_thisMonth_lysize / yhbm_thisMonth_djsize).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            yhbm_everyMonth_lylv_Map.put(k, yhbm_thisMonth_lylv + "");
            Double yhbm_thisMonth_pjzql = v.stream().filter(x -> x.getZql() != null).mapToDouble(ZhiBiaoYhbm::getZql).average().getAsDouble();
            yhbm_everyMonth_pjzql_Map.put(k, yhbm_thisMonth_pjzql + "");

            ///图表
            yhbm_everyMonth_month_List.add(k + "月");
            yhbm_everyMonth_yhsize_List.add(yhbm_thisMonth_yhsize);
            yhbm_everyMonth_lysize_List.add(yhbm_thisMonth_lysize);
            yhbm_everyMonth_lylv_List.add(yhbm_thisMonth_lylv);
        });
        exportDataPackage.setYhbm_everyMonth_month_List(yhbm_everyMonth_month_List);
        exportDataPackage.setYhbm_everyMonth_yhsize_List(yhbm_everyMonth_yhsize_List);
        exportDataPackage.setYhbm_everyMonth_lysize_List(yhbm_everyMonth_lysize_List);
        exportDataPackage.setYhbm_everyMonth_lylv_List(yhbm_everyMonth_lylv_List);
        //这月登记数
        Integer yhbm_thisMonth_djsize = Integer.valueOf(yhbm_everyMonth_djsize_Map.get(month));
        dataFinal.put("yhbm_thisMonth_djsize", yhbm_thisMonth_djsize);
        //上月登记数
        Integer yhbm_lastMonth_djsize = Integer.valueOf(yhbm_everyMonth_djsize_Map.get(lastmonth));
        Integer yhbm_thisMonth_hb = yhbm_thisMonth_djsize - yhbm_lastMonth_djsize;
        //这月登记环比
        if (yhbm_thisMonth_hb > 0) {
            dataFinal.put("yhbm_thisMonth_hb_true", true);
        } else {
            dataFinal.put("yhbm_thisMonth_hb_false", true);
        }
        dataFinal.put("yhbm_thisMonth_hb", Math.abs(yhbm_thisMonth_hb));
        //这月流摇绿
        dataFinal.put("yhbm_thisMonth_lylv", new BigDecimal(yhbm_everyMonth_lylv_Map.get(month)).multiply(new BigDecimal("100")) + "%");
        //这月平均中签率
        dataFinal.put("yhbm_thisMonth_pjzql", new BigDecimal(yhbm_everyMonth_pjzql_Map.get(month)).multiply(new BigDecimal("100")) + "%");
        //上月平均中签率
        Double yhbm_lastMonth_pjzql = Double.valueOf(yhbm_everyMonth_pjzql_Map.get(lastmonth));
        //中签率环比
        Double yhbm_thisMonth_pjzql = Double.valueOf(yhbm_everyMonth_pjzql_Map.get(month));
        BigDecimal yhbm_thisMonth_zqlhb = new BigDecimal((float) ((yhbm_thisMonth_pjzql - yhbm_lastMonth_pjzql) / yhbm_lastMonth_pjzql)).setScale(2, BigDecimal.ROUND_HALF_UP);

        if (yhbm_thisMonth_zqlhb.compareTo(BigDecimal.ZERO) > 0) {
            dataFinal.put("yhbm_thisMonth_zqlhb_true", true);
        } else {
            dataFinal.put("yhbm_thisMonth_zqlhb_false", true);
        }
        dataFinal.put("yhbm_thisMonth_zqlhb", yhbm_thisMonth_zqlhb.abs().multiply(new BigDecimal("100")) + "%");
        //续销
        List<ZhiBiaoYhbm> list_month_this = maplist_month_thisnian.get(month);
        List<ZhiBiaoYhbm> before_thismonth_newList = new ArrayList<ZhiBiaoYhbm>(Arrays.asList(new ZhiBiaoYhbm[formatSourceListThis.size()]));
        Collections.copy(before_thismonth_newList, formatSourceListThis);
        before_thismonth_newList.removeAll(list_month_this);

        StringBuffer yhbm_thisMonth_zqlhb_rise = new StringBuffer();
        StringBuffer yhbm_thisMonth_zqlhb_reduce = new StringBuffer();
        StringBuffer yhbm_thisMonth_zqlhb_equal = new StringBuffer();
        for (ZhiBiaoYhbm thismonth : list_month_this) {
            for (ZhiBiaoYhbm beforemonth : before_thismonth_newList) {
                if (beforemonth.getLpmc().indexOf(thismonth.getLpmc()) > 0) {
                    if (thismonth.getZql() > beforemonth.getZql()) {
                        yhbm_thisMonth_zqlhb_rise.append(thismonth.getLpmc());
                    } else if (thismonth.getZql() < beforemonth.getZql()) {
                        yhbm_thisMonth_zqlhb_reduce.append(thismonth.getLpmc());
                    } else {
                        yhbm_thisMonth_zqlhb_equal.append(thismonth.getLpmc());
                    }
                }
            }
        }
        dataFinal.put("yhbm_thisMonth_zqlhb_rise", yhbm_thisMonth_zqlhb_rise);
        dataFinal.put("yhbm_thisMonth_zqlhb_reduce", yhbm_thisMonth_zqlhb_reduce);
        dataFinal.put("yhbm_thisMonth_zqlhb_equal", yhbm_thisMonth_zqlhb_equal);*/

    }


    private void yhbm(Long templateId, Map<String, Object> dataFinal, LocalDate last, ExportDataPackage exportDataPackage) {

        QueryWrapper<ZhiBiaoYhbm> wrapper1 = new QueryWrapper<>();
        LambdaQueryWrapper<ZhiBiaoYhbm> lambda1 = wrapper1.lambda();
        Date firstDayOfYearDate = DateUtil.localDate2Date(last.with(TemporalAdjusters.firstDayOfYear()));
        //LocalDate thisMonthFirstday = LocalDate.of(last.getYear(), last.getMonth(), 1);
        LocalDate lastMonthlastDay = last.with(TemporalAdjusters.lastDayOfMonth());
        Date thisMonthLastdayDate = DateUtil.localDate2Date(lastMonthlastDay);
        lambda1.ge(ZhiBiaoYhbm::getYhjssj, firstDayOfYearDate);
        lambda1.le(ZhiBiaoYhbm::getYhjssj, thisMonthLastdayDate);
        List<ZhiBiaoYhbm> sourceListThis = zZhiBiaoYhbmService.list(wrapper1);
        List<ZhiBiaoYhbm> formatSourceListThis = Optional.ofNullable(sourceListThis).get().stream().map(x -> {
            x.setMonth(DateUtil.format(x.getYhjssj(), "M"));
            return x;
        }).collect(Collectors.toList());

        QueryWrapper<ZhiBiaoYhbm> wrapper2 = new QueryWrapper<>();
        LambdaQueryWrapper<ZhiBiaoYhbm> lambda2 = wrapper2.lambda();
        LocalDate quNian = last.plusYears(-1);
        Date firstDayOfYearDate_quNian = DateUtil.localDate2Date(quNian.with(TemporalAdjusters.firstDayOfYear()));
        LocalDate thisMonthFirstday_quNian = LocalDate.of(quNian.getYear(), last.getMonth(), 1);
        Date thisMonthFirstdayDate_quNian = DateUtil.localDate2Date(thisMonthFirstday_quNian);
        lambda2.ge(ZhiBiaoYhbm::getYhjssj, firstDayOfYearDate_quNian);
        lambda2.le(ZhiBiaoYhbm::getYhjssj, thisMonthFirstdayDate_quNian);
        List<ZhiBiaoYhbm> sourceListLast2 = zZhiBiaoYhbmService.list(wrapper2);
        List<ZhiBiaoYhbm> formatSourceListThis2 = Optional.ofNullable(sourceListLast2).get().stream().map(x -> {
            x.setMonth(DateUtil.format(x.getYhjssj(), "M"));
            return x;
        }).collect(Collectors.toList());

        //今年按月份 原数据
        Map<String, List<ZhiBiaoYhbm>> maplist_month_thisnian = Optional.ofNullable(formatSourceListThis).get().stream().collect(Collectors.groupingBy(ZhiBiaoYhbm::getMonth));
        //去年按月份 原数据
        Map<String, List<ZhiBiaoYhbm>> maplist_month_qunian = Optional.ofNullable(formatSourceListThis2).get().stream().collect(Collectors.groupingBy(ZhiBiaoYhbm::getMonth));

        /////////////////////////////////////按月统计////////////////////////////////////////////
        //计算今年每月总登记数
        Map<String, String> yhbm_everyMonth_djsize_Map = new HashMap<String, String>();
        //计算今年每月摇号数
        Map<String, String> yhbm_everyMonth_yhsize_Map = new HashMap<String, String>();
        //计算今年每月流摇数
        Map<String, String> yhbm_everyMonth_lysize_Map = new HashMap<String, String>();
        //计算今年每月流摇lv
        Map<String, String> yhbm_everyMonth_lylv_Map = new HashMap<String, String>();
        //计算今年每月中签率
        Map<String, String> yhbm_everyMonth_pjzql_Map = new HashMap<String, String>();

        String month = last.getMonthValue() + "";
        String lastmonth = last.getMonthValue() - 1 + "";

        List<String> yhbm_everyMonth_month_List = new ArrayList<String>();
        List<Integer> yhbm_everyMonth_yhsize_List = new ArrayList<Integer>();
        List<Integer> yhbm_everyMonth_lysize_List = new ArrayList<Integer>();
        List<Double> yhbm_everyMonth_lylv_List = new ArrayList<Double>();
        maplist_month_thisnian.forEach((k, v) -> {
            Integer yhbm_thisMonth_djsize = v.size();
            yhbm_everyMonth_djsize_Map.put(k, String.valueOf(yhbm_thisMonth_djsize));
            List<ZhiBiaoYhbm> yhbm_thisMonth = v.stream().filter(x -> x.getBmrs() != null && x.getFys() != null).filter(x -> (x.getBmrs() - x.getFys()) > 0).collect(Collectors.toList());
            Integer yhbm_thisMonth_yhsize = yhbm_thisMonth.size();
            yhbm_everyMonth_yhsize_Map.put(k, String.valueOf(yhbm_thisMonth_yhsize));
            Integer yhbm_thisMonth_lysize = yhbm_thisMonth_djsize - yhbm_thisMonth_yhsize;
            yhbm_everyMonth_lysize_Map.put(k, String.valueOf(yhbm_thisMonth_lysize));
            Double yhbm_thisMonth_lylv = new BigDecimal((float) yhbm_thisMonth_lysize / yhbm_thisMonth_djsize).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            yhbm_everyMonth_lylv_Map.put(k, yhbm_thisMonth_lylv + "");
            Double yhbm_thisMonth_pjzql = v.stream().filter(x -> x.getZql() != null).mapToDouble(ZhiBiaoYhbm::getZql).average().getAsDouble();
            yhbm_everyMonth_pjzql_Map.put(k, yhbm_thisMonth_pjzql + "");

            ///图表
            yhbm_everyMonth_month_List.add(k + "月");
            yhbm_everyMonth_yhsize_List.add(yhbm_thisMonth_yhsize);
            yhbm_everyMonth_lysize_List.add(yhbm_thisMonth_lysize);
            yhbm_everyMonth_lylv_List.add(yhbm_thisMonth_lylv);
        });
        exportDataPackage.setYhbm_everyMonth_month_List(yhbm_everyMonth_month_List);
        exportDataPackage.setYhbm_everyMonth_yhsize_List(yhbm_everyMonth_yhsize_List);
        exportDataPackage.setYhbm_everyMonth_lysize_List(yhbm_everyMonth_lysize_List);
        exportDataPackage.setYhbm_everyMonth_lylv_List(yhbm_everyMonth_lylv_List);
        //这月登记数
        Integer yhbm_thisMonth_djsize = Integer.valueOf(yhbm_everyMonth_djsize_Map.get(month));
        dataFinal.put("yhbm_thisMonth_djsize", yhbm_thisMonth_djsize);
        //上月登记数
        Integer yhbm_lastMonth_djsize = Integer.valueOf(yhbm_everyMonth_djsize_Map.get(lastmonth));
        Integer yhbm_thisMonth_hb = yhbm_thisMonth_djsize - yhbm_lastMonth_djsize;
        //这月登记环比
        if (yhbm_thisMonth_hb > 0) {
            dataFinal.put("yhbm_thisMonth_hb_true", true);
        } else {
            dataFinal.put("yhbm_thisMonth_hb_false", true);
        }
        dataFinal.put("yhbm_thisMonth_hb", Math.abs(yhbm_thisMonth_hb));
        //这月流摇绿
        dataFinal.put("yhbm_thisMonth_lylv", new BigDecimal(yhbm_everyMonth_lylv_Map.get(month)).multiply(new BigDecimal("100")) + "%");
        //这月平均中签率
        dataFinal.put("yhbm_thisMonth_pjzql", new BigDecimal(yhbm_everyMonth_pjzql_Map.get(month)).multiply(new BigDecimal("100")) + "%");
        //上月平均中签率
        Double yhbm_lastMonth_pjzql = Double.valueOf(yhbm_everyMonth_pjzql_Map.get(lastmonth));
        //中签率环比
        Double yhbm_thisMonth_pjzql = Double.valueOf(yhbm_everyMonth_pjzql_Map.get(month));
        BigDecimal yhbm_thisMonth_zqlhb = new BigDecimal((float) ((yhbm_thisMonth_pjzql - yhbm_lastMonth_pjzql) / yhbm_lastMonth_pjzql)).setScale(2, BigDecimal.ROUND_HALF_UP);

        if (yhbm_thisMonth_zqlhb.compareTo(BigDecimal.ZERO) > 0) {
            dataFinal.put("yhbm_thisMonth_zqlhb_true", true);
        } else {
            dataFinal.put("yhbm_thisMonth_zqlhb_false", true);
        }
        dataFinal.put("yhbm_thisMonth_zqlhb", yhbm_thisMonth_zqlhb.abs().multiply(new BigDecimal("100")) + "%");
        //续销
        List<ZhiBiaoYhbm> list_month_this = maplist_month_thisnian.get(month);
        List<ZhiBiaoYhbm> before_thismonth_newList = new ArrayList<ZhiBiaoYhbm>(Arrays.asList(new ZhiBiaoYhbm[formatSourceListThis.size()]));
        Collections.copy(before_thismonth_newList, formatSourceListThis);
        before_thismonth_newList.removeAll(list_month_this);

        StringBuffer yhbm_thisMonth_zqlhb_rise = new StringBuffer();
        StringBuffer yhbm_thisMonth_zqlhb_reduce = new StringBuffer();
        StringBuffer yhbm_thisMonth_zqlhb_equal = new StringBuffer();
        for (ZhiBiaoYhbm thismonth : list_month_this) {
            for (ZhiBiaoYhbm beforemonth : before_thismonth_newList) {
                if (beforemonth.getLpmc().indexOf(thismonth.getLpmc()) > 0) {
                    if (thismonth.getZql() > beforemonth.getZql()) {
                        yhbm_thisMonth_zqlhb_rise.append(thismonth.getLpmc());
                    } else if (thismonth.getZql() < beforemonth.getZql()) {
                        yhbm_thisMonth_zqlhb_reduce.append(thismonth.getLpmc());
                    } else {
                        yhbm_thisMonth_zqlhb_equal.append(thismonth.getLpmc());
                    }
                }
            }
        }
        dataFinal.put("yhbm_thisMonth_zqlhb_rise", yhbm_thisMonth_zqlhb_rise);
        dataFinal.put("yhbm_thisMonth_zqlhb_reduce", yhbm_thisMonth_zqlhb_reduce);
        dataFinal.put("yhbm_thisMonth_zqlhb_equal", yhbm_thisMonth_zqlhb_equal);


        /////////////////////////////////按城区统计///////////////////////////////////////////////////
        //今年按月份 原数据
        Map<String, List<ZhiBiaoYhbm>> maplist_cq_this = Optional.ofNullable(formatSourceListThis).get().stream().collect(Collectors.groupingBy(ZhiBiaoYhbm::getLpcq));
        //计算今年每城区总登记数
        Map<String, String> yhbm_everyCq_djsize_Map = new HashMap<String, String>();
        //计算今年每城区摇号数
        Map<String, String> yhbm_everyCq_yhsize_Map = new HashMap<String, String>();
        //计算今年每城区流摇数
        Map<String, String> yhbm_everyCq_lysize_Map = new HashMap<String, String>();
        //计算今年每城区流摇lv
        Map<String, String> yhbm_everyCq_lylv_Map = new HashMap<String, String>();
        //计算今年每城区流摇lv
        Map<String, String> yhbm_everyCq_pjzql_Map = new HashMap<String, String>();
        //计算城区统计
        List<Yhcqtj> yhcqtjList = new ArrayList<Yhcqtj>();
        maplist_cq_this.forEach((k, v) -> {
            Integer yhbm_thisCq_djsize = v.size();
            yhbm_everyCq_djsize_Map.put(k, String.valueOf(yhbm_thisCq_djsize));
            Integer yhbm_thisCq_yhsize = v.stream().filter(x -> x.getBmrs() != null && x.getFys() != null).filter(x -> (x.getBmrs() - x.getFys()) > 0).collect(Collectors.toList()).size();
            yhbm_everyCq_yhsize_Map.put(k, String.valueOf(yhbm_thisCq_yhsize));
            Integer yhbm_thisCq_lysize = yhbm_thisCq_djsize - yhbm_thisCq_yhsize;
            yhbm_everyCq_lysize_Map.put(k, String.valueOf(yhbm_thisCq_lysize));
            String yhbm_thisCq_lylv = new BigDecimal((float) yhbm_thisCq_lysize / yhbm_thisCq_djsize).setScale(2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")) + "%";
            yhbm_everyCq_lylv_Map.put(k, yhbm_thisCq_lylv + "");
            Double yhbm_thisCq_pjzql_double = v.stream().filter(x -> x.getZql() != null).mapToDouble(ZhiBiaoYhbm::getZql).average().getAsDouble();
            String yhbm_thisCq_pjzql = new BigDecimal(yhbm_thisCq_pjzql_double).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%";
            yhbm_everyCq_pjzql_Map.put(k, yhbm_thisCq_pjzql);

            Yhcqtj yhcqtj = new Yhcqtj();
            yhcqtj.setCq(k);
            yhcqtj.setBmdjcs(yhbm_thisCq_djsize);
            Long fys_all = v.stream().filter(x -> x.getFys() != null).mapToLong(ZhiBiaoYhbm::getFys).sum();
            yhcqtj.setFys(fys_all);
            Long bmrs_all = v.stream().filter(x -> x.getBmrs() != null).mapToLong(ZhiBiaoYhbm::getBmrs).sum();
            yhcqtj.setBmdjrs(bmrs_all);
            yhcqtj.setYhcs(yhbm_thisCq_yhsize);
            yhcqtj.setLycs(yhbm_thisCq_lysize);
            yhcqtj.setLyb(yhbm_thisCq_lylv);
            yhcqtj.setPjzql(yhbm_thisCq_pjzql);
            yhcqtjList.add(yhcqtj);
        });
        exportDataPackage.setYhcqtjList(yhcqtjList);

        //这城区流摇绿
        dataFinal.put("yhbm_zcq_lylv", yhbm_everyCq_lylv_Map.get(zcq));
        dataFinal.put("yhbm_xs_lylv", yhbm_everyCq_lylv_Map.get(xs));
        dataFinal.put("yhbm_yh_lylv", yhbm_everyCq_lylv_Map.get(yh));
        dataFinal.put("yhbm_fy_lylv", yhbm_everyCq_lylv_Map.get(fy));
        dataFinal.put("yhbm_la_lylv", yhbm_everyCq_lylv_Map.get(la));
        //这城区平均中签率
        dataFinal.put("yhbm_zcq_pjzql", yhbm_everyCq_pjzql_Map.get(zcq));
        dataFinal.put("yhbm_xs_pjzql", yhbm_everyCq_pjzql_Map.get(xs));
        dataFinal.put("yhbm_yh_pjzql", yhbm_everyCq_pjzql_Map.get(yh));
        dataFinal.put("yhbm_fy_pjzql", yhbm_everyCq_pjzql_Map.get(fy));
        dataFinal.put("yhbm_la_pjzql", yhbm_everyCq_pjzql_Map.get(la));

        //////////////////////////////总指标/////////////////////////////////////////////////
        //当年总登记数
        Integer yhbm_zdjs = sourceListThis.size();
        dataFinal.put("yhbm_zdjs", yhbm_zdjs);
        //总登记同比
        Integer size_this = maplist_month_thisnian.get(month).size();
        Integer size_qunian = null;
        BigDecimal yhbm_tb = null;
        if (!CollectionUtils.isEmpty(maplist_month_qunian)) {
            maplist_month_qunian.get(month).size();
            yhbm_tb = new BigDecimal((float) size_this / size_qunian).setScale(2, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1"));
            if (yhbm_tb.compareTo(BigDecimal.ZERO) > 0) {
                dataFinal.put("yhbm_tb_true", true);
            } else {
                dataFinal.put("yhbm_tb_false", true);
            }
            dataFinal.put("yhbm_tb", yhbm_tb.abs());
        }
        //当年总房源数
        Long yhbm_zfys = sourceListThis.stream().filter(x -> x.getFys() != null).mapToLong(ZhiBiaoYhbm::getFys).sum();
        dataFinal.put("yhbm_zfys", yhbm_zfys);
        //当年总摇号次数
        Integer yhbm_yhsize = sourceListThis.stream().filter(x -> x.getBmrs() != null && x.getFys() != null).filter(x -> (x.getBmrs() - x.getFys()) > 0).collect(Collectors.toList()).size();
        dataFinal.put("yhbm_yhsize", yhbm_yhsize);
        //当年总摇号占总登记比
        BigDecimal yhbm_yhzb = new BigDecimal((float) yhbm_yhsize / yhbm_zdjs).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP);
        dataFinal.put("yhbm_yhzb", yhbm_yhzb + "%");
        //当年总摇号房源数
        Long yhbm_yhfysize = sourceListThis.stream().filter(x -> x.getBmrs() != null && x.getFys() != null).filter(x -> (x.getBmrs() - x.getFys()) > 0).mapToLong(ZhiBiaoYhbm::getFys).sum();
        dataFinal.put("yhbm_yhfysize", yhbm_yhfysize);
        //当年总摇号房源数占总房源数比
        float yhbm_yhfyzb_float = (float) yhbm_yhsize / yhbm_zfys;
        BigDecimal yhbm_yhfyzb = new BigDecimal(yhbm_yhfyzb_float).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP);
        dataFinal.put("yhbm_yhfyzb", yhbm_yhfyzb + "%");

        //当年总流摇次数
        Long yhbm_lysize = Long.valueOf(yhbm_zdjs - yhbm_yhsize);
        dataFinal.put("yhbm_lysize", yhbm_lysize);
        //流摇占比
        BigDecimal one = new BigDecimal("100");
        BigDecimal yhbm_lyzb = one.subtract(yhbm_yhzb);
        dataFinal.put("yhbm_lyzb", yhbm_lyzb + "%");
        //流摇房源数
        Long yhbm_lyfysize = yhbm_zfys - yhbm_yhfysize;
        dataFinal.put("yhbm_lyfysize", yhbm_lyfysize);
        //流摇房源占比
        BigDecimal yhbm_lyfyzb = one.subtract(yhbm_yhfyzb);
        dataFinal.put("yhbm_lyfyzb", yhbm_lyfyzb + "%");

        //当年总报名人数
        Long yhbm_zbmsize = sourceListThis.stream().filter(x -> x.getBmrs() != null).mapToLong(ZhiBiaoYhbm::getBmrs).sum();
        dataFinal.put("yhbm_zbmsize", yhbm_zbmsize);
        //当年总无房人数
        Long yhbm_wfsize = sourceListThis.stream().filter(x -> x.getWfbmrs() != null).mapToLong(ZhiBiaoYhbm::getWfbmrs).sum();
        dataFinal.put("yhbm_wfsize", yhbm_wfsize);
        //当年无房占比
        BigDecimal yhbm_wfzb = new BigDecimal((float) yhbm_wfsize / yhbm_zbmsize).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP);

        dataFinal.put("yhbm_wfzb", yhbm_wfzb + "%");
    }


    private ReportsWordTemplate jiageZhishu_spf(Long templateId, Map<String, Object> dataFinal, LocalDate last, ExportDataPackage exportDataPackage) {
        Map<String, Object> data_zhiBiaoZzxsjgbdqk = new HashMap<String, Object>();

        // 数据库查询城市指标数据data1 <db,data>
        QueryWrapper<ZhiBiaoZzxsjgbdqk> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<ZhiBiaoZzxsjgbdqk> lambda = wrapper.lambda();
        lambda.in(ZhiBiaoZzxsjgbdqk::getCity, hz);
        lambda.eq(ZhiBiaoZzxsjgbdqk::getLb, "1");
        LocalDate thisMonthFirstday = LocalDate.of(last.getYear(), last.getMonth(), 1);
        Date thisMonthFirstDate = DateUtil.localDate2Date(thisMonthFirstday);
        lambda.eq(ZhiBiaoZzxsjgbdqk::getTitle, thisMonthFirstDate);
        Map<String, Object> datas_Zzxsjgbdqkdata_hz = zhiBiaoZzxsjgbdqkService.getMap(wrapper);
        if (!CollectionUtils.isEmpty(datas_Zzxsjgbdqkdata_hz)) {
            datas_Zzxsjgbdqkdata_hz.forEach((k, v) -> {
                Optional.ofNullable(v).map(u -> data_zhiBiaoZzxsjgbdqk.put("SPF_ODS_PY_ZZXSJGBDQK_MM_" + k, datas_Zzxsjgbdqkdata_hz.get(k)));
            });
        }
        // 数据库查询word模板指标参数zhibiaoMap <db,word>
        ReportsWordTemplate reportsWordTemplate = reportsWordTemplateService.getById(templateId);
        String zhibiaos = reportsWordTemplate.getZhibiaos();
        HashMap<String, String> zhibiaoMap = Optional.ofNullable(zhibiaos).map(u -> JSONObject.parseObject(u, HashMap.class)).get();

        zhibiaoMap.forEach((k, v) -> {
            Optional.ofNullable(v).map(u -> data_zhiBiaoZzxsjgbdqk.put(v, data_zhiBiaoZzxsjgbdqk.get(k)));
        });
        dataFinal.putAll(data_zhiBiaoZzxsjgbdqk);

        //查询22个城市ZhiBiaoZzxsjgbdqk
        QueryWrapper<ZhiBiaoZzxsjgbdqk> wrapper2 = new QueryWrapper<>();
        LambdaQueryWrapper<ZhiBiaoZzxsjgbdqk> lambda2 = wrapper2.lambda();
        List<String> cities = Arrays.asList(city.split(","));
        lambda2.in(ZhiBiaoZzxsjgbdqk::getCity, cities);
        lambda2.eq(ZhiBiaoZzxsjgbdqk::getLb, "1");
        Date firstDayOfYearDate = DateUtil.localDate2Date(last.with(TemporalAdjusters.firstDayOfYear()));
        Date thisMonthFirstdayDate = DateUtil.localDate2Date(thisMonthFirstday);
        lambda2.ge(ZhiBiaoZzxsjgbdqk::getTitle, firstDayOfYearDate);
        lambda2.le(ZhiBiaoZzxsjgbdqk::getTitle, thisMonthFirstdayDate);
        List<ZhiBiaoZzxsjgbdqk> sourceList = zhiBiaoZzxsjgbdqkService.list(wrapper2);
        //todo 指标转换
        //上月22个city集合
        List<ZhiBiaoZzxsjgbdqk> list22 = sourceList.stream().filter(x -> DateUtil.date2LocalDate(x.getTitle())
                .equals(thisMonthFirstday)).collect(Collectors.toList());
        // <city,各月list>
        Map<String, List<ZhiBiaoZzxsjgbdqk>> maplist = sourceList.stream().collect(Collectors.groupingBy(ZhiBiaoZzxsjgbdqk::getCity));

        //计算同比平均
        Map<String, String> dataTbpjMap = new HashMap<String, String>();
        //计算环比累计
        Map<String, String> dataHbljMap = new HashMap<String, String>();
        maplist.forEach((k, v) -> {
            Double tbpj = v.stream().mapToDouble(ZhiBiaoZzxsjgbdqk::getYoy).average().getAsDouble();//同比平均
            String formatTbpj = String.format("%.1f", tbpj);
            Optional.ofNullable(v).map(u -> dataTbpjMap.put(k, formatTbpj));

            Double hblj = v.stream().map(ZhiBiaoZzxsjgbdqk::getMom).reduce((p1, p2) -> p1 * p2 / 100).orElse(0D); //环比累计
            String formatHblj = String.format("%.1f", hblj);
            Optional.ofNullable(v).map(u -> dataHbljMap.put(k, formatHblj));
        });
        List<ZhiBiaoZzxsjgbdqkVo> finalList = list22.stream().map(zb -> {
            ZhiBiaoZzxsjgbdqkVo zbvo = new ZhiBiaoZzxsjgbdqkVo();
            zbvo.setCity(zb.getCity());
            zbvo.setMom(zb.getMom());
            zbvo.setYoy(zb.getYoy());
            zbvo.setYoyPj(Double.valueOf(dataTbpjMap.get(zb.getCity())));
            zbvo.setMomLj(Double.valueOf(dataHbljMap.get(zb.getCity())));
            return zbvo;
        }).collect(Collectors.toList());
        dataFinal.put("spf_tbpj", dataTbpjMap.get(hz));
        dataFinal.put("spf_hblj", dataHbljMap.get(hz));

        //计算排名
        List<ZhiBiaoZzxsjgbdqkVo> sortedMomObjList = finalList.stream()
                .sorted(Comparator.comparingDouble(ZhiBiaoZzxsjgbdqkVo::getMom).reversed().thenComparingDouble(ZhiBiaoZzxsjgbdqkVo::getYoy))
                .collect(Collectors.toList());
        List<String> sortedMomList = finalList.stream().sorted(Comparator.comparingDouble(ZhiBiaoZzxsjgbdqkVo::getMom).reversed().thenComparingDouble(ZhiBiaoZzxsjgbdqkVo::getYoy)).map(x -> x.getCity()).collect(Collectors.toList());
        List<String> sortedYoyList = finalList.stream().sorted(Comparator.comparingDouble(ZhiBiaoZzxsjgbdqkVo::getYoy).reversed()).map(x -> x.getCity()).collect(Collectors.toList());
        List<String> sortedMomLjList = finalList.stream().sorted(Comparator.comparingDouble(ZhiBiaoZzxsjgbdqkVo::getMomLj).reversed()).map(x -> x.getCity()).collect(Collectors.toList());
        List<String> sortedYoyPjList = finalList.stream().sorted(Comparator.comparingDouble(ZhiBiaoZzxsjgbdqkVo::getYoyPj).reversed()).map(x -> x.getCity()).collect(Collectors.toList());


        Map rankMap = new HashMap();
        int momRank = sortedMomList.indexOf(hz) + 1;
        int yoyRank = sortedYoyList.indexOf(hz) + 1;
        int momLjRank = sortedMomLjList.indexOf(hz) + 1;
        int yoyPjank = sortedYoyPjList.indexOf(hz) + 1;
        rankMap.put("spf_momRank", momRank);
        rankMap.put("spf_yoyRank", yoyRank);
        rankMap.put("spf_momLjRank", momLjRank);
        rankMap.put("spf_yoyPjank", yoyPjank);
        dataFinal.putAll(rankMap);

        exportDataPackage.setZhiBiaoZzxsjgbdqkList(sortedMomObjList);
        exportDataPackage.setRankMap(rankMap);


        //渲染 数组
        List<ZhiBiaoZzxsjgbdqk> zhiBiaoZzxsjgbdqks_hz = maplist.get(hz);
        List<Double> zhiBiaoZzxsjgbdqks_hz_sortedList = zhiBiaoZzxsjgbdqks_hz.stream().sorted(Comparator.comparing(ZhiBiaoZzxsjgbdqk::getTitle)).map(x ->
                x.getMom()
        ).collect(Collectors.toList());

        List<Map<String, String>> hbList = new ArrayList<Map<String, String>>();
        for (int i = 0; i < zhiBiaoZzxsjgbdqks_hz_sortedList.size(); i++) {
            Double aDouble = zhiBiaoZzxsjgbdqks_hz_sortedList.get(i);
            Map<String, String> map = new HashMap<String, String>();
            if (i == 0) {
                map.put("spfdate", "" + aDouble);
            } else {
                map.put("spfdate", "," + aDouble);
            }
            hbList.add(map);
        }

        dataFinal.put("spfHbList", hbList);
        return reportsWordTemplate;
    }


    private ReportsWordTemplate jiageZhishu_clf(Long templateId, Map<String, Object> dataFinal, LocalDate last, ExportDataPackage exportDataPackage) {
        Map<String, Object> data_zhiBiaoZzxsjgbdqk = new HashMap<String, Object>();

        // 数据库查询城市指标数据data1 <db,data>
        QueryWrapper<ZhiBiaoZzxsjgbdqk> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<ZhiBiaoZzxsjgbdqk> lambda = wrapper.lambda();
        lambda.in(ZhiBiaoZzxsjgbdqk::getCity, hz);
        lambda.eq(ZhiBiaoZzxsjgbdqk::getLb, "3");
        LocalDate thisMonthFirstday = LocalDate.of(last.getYear(), last.getMonth(), 1);
        Date thisMonthFirstDate = DateUtil.localDate2Date(thisMonthFirstday);
        lambda.eq(ZhiBiaoZzxsjgbdqk::getTitle, thisMonthFirstDate);
        Map<String, Object> datas_Zzxsjgbdqkdata_hz = zhiBiaoZzxsjgbdqkService.getMap(wrapper);
        if (!CollectionUtils.isEmpty(datas_Zzxsjgbdqkdata_hz)) {
            datas_Zzxsjgbdqkdata_hz.forEach((k, v) -> {
                Optional.ofNullable(v).map(u -> data_zhiBiaoZzxsjgbdqk.put("CLF_ODS_PY_ZZXSJGBDQK_MM_" + k, datas_Zzxsjgbdqkdata_hz.get(k)));
            });
        }
        // 数据库查询word模板指标参数zhibiaoMap <db,word>
        ReportsWordTemplate reportsWordTemplate = reportsWordTemplateService.getById(templateId);
        String zhibiaos = reportsWordTemplate.getZhibiaos();
        HashMap<String, String> zhibiaoMap = Optional.ofNullable(zhibiaos).map(u -> JSONObject.parseObject(u, HashMap.class)).get();

        zhibiaoMap.forEach((k, v) -> {
            Optional.ofNullable(v).map(u -> data_zhiBiaoZzxsjgbdqk.put(v, data_zhiBiaoZzxsjgbdqk.get(k)));
        });
        dataFinal.putAll(data_zhiBiaoZzxsjgbdqk);

        //查询22个城市ZhiBiaoZzxsjgbdqk
        QueryWrapper<ZhiBiaoZzxsjgbdqk> wrapper2 = new QueryWrapper<>();
        LambdaQueryWrapper<ZhiBiaoZzxsjgbdqk> lambda2 = wrapper2.lambda();
        List<String> cities = Arrays.asList(city.split(","));
        lambda2.in(ZhiBiaoZzxsjgbdqk::getCity, cities);
        lambda2.eq(ZhiBiaoZzxsjgbdqk::getLb, "3");
        Date firstDayOfYearDate = DateUtil.localDate2Date(last.with(TemporalAdjusters.firstDayOfYear()));
        Date thisMonthFirstdayDate = DateUtil.localDate2Date(thisMonthFirstday);
        lambda2.ge(ZhiBiaoZzxsjgbdqk::getTitle, firstDayOfYearDate);
        lambda2.le(ZhiBiaoZzxsjgbdqk::getTitle, thisMonthFirstdayDate);
        List<ZhiBiaoZzxsjgbdqk> sourceList = zhiBiaoZzxsjgbdqkService.list(wrapper2);
        //todo 指标转换
        //上月22个city集合
        List<ZhiBiaoZzxsjgbdqk> list22 = sourceList.stream().filter(x -> DateUtil.date2LocalDate(x.getTitle())
                .equals(thisMonthFirstday)).collect(Collectors.toList());
        // <city,各月list>
        Map<String, List<ZhiBiaoZzxsjgbdqk>> maplist = sourceList.stream().collect(Collectors.groupingBy(ZhiBiaoZzxsjgbdqk::getCity));

        //计算同比平均
        Map<String, String> dataTbpjMap = new HashMap<String, String>();
        //计算环比累计
        Map<String, String> dataHbljMap = new HashMap<String, String>();
        maplist.forEach((k, v) -> {
            Double tbpj = v.stream().mapToDouble(ZhiBiaoZzxsjgbdqk::getYoy).average().getAsDouble();//同比平均
            String formatTbpj = String.format("%.1f", tbpj);
            Optional.ofNullable(v).map(u -> dataTbpjMap.put(k, formatTbpj));

            Double hblj = v.stream().map(ZhiBiaoZzxsjgbdqk::getMom).reduce((p1, p2) -> p1 * p2 / 100).orElse(0D); //环比累计
            String formatHblj = String.format("%.1f", hblj);
            Optional.ofNullable(v).map(u -> dataHbljMap.put(k, formatHblj));
        });
        List<ZhiBiaoZzxsjgbdqkVo> finalList = list22.stream().map(zb -> {
            ZhiBiaoZzxsjgbdqkVo zbvo = new ZhiBiaoZzxsjgbdqkVo();
            zbvo.setCity(zb.getCity());
            zbvo.setMom(zb.getMom());
            zbvo.setYoy(zb.getYoy());
            zbvo.setYoyPj(Double.valueOf(dataTbpjMap.get(zb.getCity())));
            zbvo.setMomLj(Double.valueOf(dataHbljMap.get(zb.getCity())));
            return zbvo;
        }).collect(Collectors.toList());
        dataFinal.put("clf_tbpj", dataTbpjMap.get(hz));
        dataFinal.put("clf_hblj", dataHbljMap.get(hz));

        //计算排名
        List<ZhiBiaoZzxsjgbdqkVo> sortedMomObjList = finalList.stream()
                .sorted(Comparator.comparingDouble(ZhiBiaoZzxsjgbdqkVo::getMom).reversed().thenComparingDouble(ZhiBiaoZzxsjgbdqkVo::getYoy))
                .collect(Collectors.toList());
        List<String> sortedMomList = finalList.stream().sorted(Comparator.comparingDouble(ZhiBiaoZzxsjgbdqkVo::getMom).reversed().thenComparingDouble(ZhiBiaoZzxsjgbdqkVo::getYoy)).map(x -> x.getCity()).collect(Collectors.toList());
        List<String> sortedYoyList = finalList.stream().sorted(Comparator.comparingDouble(ZhiBiaoZzxsjgbdqkVo::getYoy).reversed()).map(x -> x.getCity()).collect(Collectors.toList());
        List<String> sortedMomLjList = finalList.stream().sorted(Comparator.comparingDouble(ZhiBiaoZzxsjgbdqkVo::getMomLj).reversed()).map(x -> x.getCity()).collect(Collectors.toList());
        List<String> sortedYoyPjList = finalList.stream().sorted(Comparator.comparingDouble(ZhiBiaoZzxsjgbdqkVo::getYoyPj).reversed()).map(x -> x.getCity()).collect(Collectors.toList());


        Map rankMap = new HashMap();
        int momRank = sortedMomList.indexOf(hz) + 1;
        int yoyRank = sortedYoyList.indexOf(hz) + 1;
        int momLjRank = sortedMomLjList.indexOf(hz) + 1;
        int yoyPjank = sortedYoyPjList.indexOf(hz) + 1;
        rankMap.put("clf_momRank", momRank);
        rankMap.put("clf_yoyRank", yoyRank);
        rankMap.put("clf_momLjRank", momLjRank);
        rankMap.put("clf_yoyPjank", yoyPjank);
        dataFinal.putAll(rankMap);

        exportDataPackage.setZhiBiaoZzxsjgbdqkList(sortedMomObjList);
        exportDataPackage.setRankMap(rankMap);

        //渲染 数组
        List<ZhiBiaoZzxsjgbdqk> zhiBiaoZzxsjgbdqks_hz = maplist.get(hz);
        List<Double> zhiBiaoZzxsjgbdqks_hz_sortedList = zhiBiaoZzxsjgbdqks_hz.stream().sorted(Comparator.comparing(ZhiBiaoZzxsjgbdqk::getTitle)).map(x ->
                x.getMom()
        ).collect(Collectors.toList());

        List<Map<String, String>> hbList = new ArrayList<Map<String, String>>();
        for (int i = 0; i < zhiBiaoZzxsjgbdqks_hz_sortedList.size(); i++) {
            Double aDouble = zhiBiaoZzxsjgbdqks_hz_sortedList.get(i);
            Map<String, String> map = new HashMap<String, String>();
            if (i == 0) {
                map.put("clfdate", "" + aDouble);
            } else {
                map.put("clfdate", "," + aDouble);
            }
            hbList.add(map);
        }

        dataFinal.put("clfHbList", hbList);
        return reportsWordTemplate;
    }

    private Map<String, Object> dealTableSpf_jiagezhishu(Map<String, Object> paramMap, ExportDataPackage exportDataPackage) {


        RowRenderData row01 = Rows.of("城市", "环比", "环比累计", "同比", "平均同比")
                .center().horizontalCenter().textFontSize(8).textColor("FFFFFF").bgColor("4472C4").create();

        List<RowRenderData> listCellRenderDatas01 = new ArrayList<RowRenderData>();
        listCellRenderDatas01.add(row01);
        List<ZhiBiaoZzxsjgbdqkVo> zhiBiaoZzxsjgbdqkList = exportDataPackage.getZhiBiaoZzxsjgbdqkList();
        for (ZhiBiaoZzxsjgbdqkVo zhiBiaoZzxsjgbdqk : zhiBiaoZzxsjgbdqkList) {
            listCellRenderDatas01.add(Rows.of(zhiBiaoZzxsjgbdqk.getCity()
                    , zhiBiaoZzxsjgbdqk.getMom() + "", zhiBiaoZzxsjgbdqk.getMomLj() + ""
                    , zhiBiaoZzxsjgbdqk.getYoy() + "", zhiBiaoZzxsjgbdqk.getYoyPj() + "")
                    .center().horizontalCenter().textFontSize(8).create());
        }
        Map rankMap = exportDataPackage.getRankMap();
        listCellRenderDatas01.add(Rows.of("杭州位次"
                , rankMap.get("spf_momRank") + "", rankMap.get("spf_momLjRank") + ""
                , rankMap.get("spf_yoyRank") + "", rankMap.get("spf_yoyPjank") + "")
                .center().horizontalCenter().textFontSize(8).create());
        RowRenderData[] rowRenderData01 = listCellRenderDatas01.stream().toArray(RowRenderData[]::new);
        paramMap.put("spf_jgzs_tableTemplate", Tables.of(rowRenderData01).create());
        return paramMap;
    }

    private Map<String, Object> dealTableSpf_yhbm(Map<String, Object> paramMap, ExportDataPackage exportDataPackage) {


        RowRenderData row0 = Rows.of("城区", "登记项目及登记户数", null, null, "项目流摇情况", null, null, "平均中签率")
                .center().horizontalCenter().textFontSize(8).textColor("FFFFFF").bgColor("4472C4").create();
        RowRenderData row1 = Rows.of(null, "报名登记次数", "房源", "报名登记", "摇号次数", "流摇次数", "流摇比例", null)
                .center().horizontalCenter().textFontSize(8).textColor("FFFFFF").bgColor("4472C4").create();
        RowRenderData row2 = Rows.of(null, null, "(套)", "(户)", null, null, null, null)
                .center().horizontalCenter().textFontSize(8).textColor("FFFFFF").bgColor("4472C4").create();

        MergeCellRule rule = MergeCellRule.builder()
                .map(MergeCellRule.Grid.of(0, 1), MergeCellRule.Grid.of(0, 3))
                .map(MergeCellRule.Grid.of(0, 4), MergeCellRule.Grid.of(0, 6))
                .map(MergeCellRule.Grid.of(0, 0), MergeCellRule.Grid.of(2, 0))
                .map(MergeCellRule.Grid.of(1, 1), MergeCellRule.Grid.of(2, 1))
                .map(MergeCellRule.Grid.of(1, 4), MergeCellRule.Grid.of(2, 4))
                .map(MergeCellRule.Grid.of(1, 5), MergeCellRule.Grid.of(2, 5))
                .map(MergeCellRule.Grid.of(1, 6), MergeCellRule.Grid.of(2, 6))
                .map(MergeCellRule.Grid.of(0, 7), MergeCellRule.Grid.of(2, 7))
                .build();
        List<RowRenderData> listCellRenderDatas = new ArrayList<RowRenderData>();
        listCellRenderDatas.add(row0);
        listCellRenderDatas.add(row1);
        listCellRenderDatas.add(row2);

        List<Yhcqtj> yhcqtjList = exportDataPackage.getYhcqtjList();
        for (Yhcqtj yhcqtj : yhcqtjList) {
            listCellRenderDatas.add(
                    Rows.of(yhcqtj.getCq(), yhcqtj.getBmdjcs() + ""
                            , yhcqtj.getFys() + "", yhcqtj.getBmdjrs() + ""
                            , yhcqtj.getYhcs() + "", yhcqtj.getLycs() + ""
                            , yhcqtj.getLyb() + "", yhcqtj.getPjzql() + "")
                            .center().horizontalCenter().textFontSize(8).create());
        }

        RowRenderData[] rowRenderData = listCellRenderDatas.stream().toArray(RowRenderData[]::new);
        paramMap.put("spf_yhbm_tableTemplate", Tables.of(rowRenderData).mergeRule(rule).create());

        return paramMap;
    }

    private Map<String, Object> dealTableClf_jiagezhishu(Map<String, Object> paramMap, ExportDataPackage exportDataPackage) {


        RowRenderData row01 = Rows.of("城市", "环比", "环比累计", "同比", "平均同比")
                .center().horizontalCenter().textFontSize(8).textColor("FFFFFF").bgColor("4472C4").create();

        List<RowRenderData> listCellRenderDatas01 = new ArrayList<RowRenderData>();
        listCellRenderDatas01.add(row01);
        List<ZhiBiaoZzxsjgbdqkVo> zhiBiaoZzxsjgbdqkList = exportDataPackage.getZhiBiaoZzxsjgbdqkList();
        for (ZhiBiaoZzxsjgbdqkVo zhiBiaoZzxsjgbdqk : zhiBiaoZzxsjgbdqkList) {
            listCellRenderDatas01.add(Rows.of(zhiBiaoZzxsjgbdqk.getCity()
                    , zhiBiaoZzxsjgbdqk.getMom() + "", zhiBiaoZzxsjgbdqk.getMomLj() + ""
                    , zhiBiaoZzxsjgbdqk.getYoy() + "", zhiBiaoZzxsjgbdqk.getYoyPj() + "")
                    .center().horizontalCenter().textFontSize(8).create());
        }
        Map rankMap = exportDataPackage.getRankMap();
        listCellRenderDatas01.add(Rows.of("杭州位次"
                , rankMap.get("clf_momRank") + "", rankMap.get("clf_momLjRank") + ""
                , rankMap.get("clf_yoyRank") + "", rankMap.get("clf_yoyPjank") + "")
                .center().horizontalCenter().textFontSize(8).create());
        RowRenderData[] rowRenderData01 = listCellRenderDatas01.stream().toArray(RowRenderData[]::new);
        paramMap.put("clf_tableTemplate", Tables.of(rowRenderData01).create());

        return paramMap;
    }

    private Map<String, Object> dealChart_Yh(Map<String, Object> paramMap, ExportDataPackage exportDataPackage) {
        List<String> yhbm_everyMonth_month_list = exportDataPackage.getYhbm_everyMonth_month_List();
        List<Integer> yhbm_everyMonth_yhsize_list = exportDataPackage.getYhbm_everyMonth_yhsize_List();
        List<Integer> yhbm_everyMonth_lysize_list = exportDataPackage.getYhbm_everyMonth_lysize_List();
        List<Double> yhbm_everyMonth_lylv_list = exportDataPackage.getYhbm_everyMonth_lylv_List();

        //柱状图、折线图共存
        ChartMultiSeriesRenderData barLine = new ChartMultiSeriesRenderData();
        barLine.setChartTitle("摇号统计");
        barLine.setCategories(yhbm_everyMonth_month_list.toArray(new String[yhbm_everyMonth_month_list.size()]));

        List<SeriesRenderData> seriesRenderDatasList = new ArrayList<SeriesRenderData>();
        SeriesRenderData seriesRenderData1 = new SeriesRenderData();
        seriesRenderData1.setName("摇号数");
        seriesRenderData1.setValues(yhbm_everyMonth_yhsize_list.toArray(new Integer[yhbm_everyMonth_yhsize_list.size()]));
        seriesRenderData1.setComboType(SeriesRenderData.ComboType.BAR);
        seriesRenderDatasList.add(seriesRenderData1);

        SeriesRenderData seriesRenderData2 = new SeriesRenderData();
        seriesRenderData2.setName("流摇数");
        seriesRenderData2.setValues(yhbm_everyMonth_lysize_list.toArray(new Integer[yhbm_everyMonth_lysize_list.size()]));
        seriesRenderData2.setComboType(SeriesRenderData.ComboType.BAR);
        seriesRenderDatasList.add(seriesRenderData2);

        SeriesRenderData seriesRenderData4 = new SeriesRenderData();
        seriesRenderData4.setName("流摇率");
        seriesRenderData4.setValues(yhbm_everyMonth_lylv_list.toArray(new Double[yhbm_everyMonth_lylv_list.size()]));
        seriesRenderData4.setComboType(SeriesRenderData.ComboType.LINE);
        seriesRenderDatasList.add(seriesRenderData4);

        barLine.setSeriesDatas(seriesRenderDatasList);
        paramMap.put("yhbmCharts", barLine);

        return paramMap;
    }


   /* private Map<String, Object> dealChart(Map<String, Object> paramMap) {

        //将数据存储为了后边生成图样式
        List<String> devname = new ArrayList<String>();
        List<Double> useRate = new ArrayList<Double>();
        List<Integer> useYear = new ArrayList<Integer>();

        for (int i = 0; i < 5; i++) {
            //存入list,为了生成图表
            devname.add("电灯_" + i);
            useRate.add(Math.random() * 100);
            useYear.add(i + 1);
        }

        *//* 测试图表的插入-------------------------------------*//*
        //柱状图生成
        ChartMultiSeriesRenderData bar = new ChartMultiSeriesRenderData();
        bar.setChartTitle("柱状图");
        //参数为数组
        bar.setCategories(devname.toArray(new String[devname.size()]));
        List<SeriesRenderData> seriesRenderDatas = new ArrayList<SeriesRenderData>();
        seriesRenderDatas.add(new SeriesRenderData("使用率", useRate.toArray(new Double[useRate.size()])));
        seriesRenderDatas.add(new SeriesRenderData("使用年限", useYear.toArray(new Integer[useYear.size()])));
        bar.setSeriesDatas(seriesRenderDatas);
        paramMap.put("barCharts", bar);

        paramMap.put("qsxjspzfydcjts", bar);

        //折线图生成
        ChartMultiSeriesRenderData line = new ChartMultiSeriesRenderData();
        line.setChartTitle("lineCharts");
        //参数为数组
        line.setCategories(devname.toArray(new String[devname.size()]));
        List<SeriesRenderData> seriesRenderDatas1 = new ArrayList<SeriesRenderData>();
        seriesRenderDatas1.add(new SeriesRenderData("使用率", useRate.toArray(new Double[useRate.size()])));
        seriesRenderDatas1.add(new SeriesRenderData("使用年限", useYear.toArray(new Integer[useYear.size()])));
        line.setSeriesDatas(seriesRenderDatas1);
        paramMap.put("lineCharts", line);

        //柱状图、折线图共存
        ChartMultiSeriesRenderData barLine = new ChartMultiSeriesRenderData();
        barLine.setChartTitle("barLineCharts");
        barLine.setCategories(devname.toArray(new String[devname.size()]));

        List<SeriesRenderData> seriesRenderDatas2 = new ArrayList<SeriesRenderData>();
        SeriesRenderData seriesRenderData1 = new SeriesRenderData();
        seriesRenderData1.setName("使用率bar");
        seriesRenderData1.setValues(useRate.toArray(new Double[useRate.size()]));
        seriesRenderData1.setComboType(SeriesRenderData.ComboType.BAR);
        seriesRenderDatas2.add(seriesRenderData1);

        SeriesRenderData seriesRenderData2 = new SeriesRenderData();
        seriesRenderData2.setName("使用年限line");
        seriesRenderData2.setValues(useYear.toArray(new Integer[useYear.size()]));
        seriesRenderData2.setComboType(SeriesRenderData.ComboType.LINE);
        seriesRenderDatas2.add(seriesRenderData2);

        SeriesRenderData seriesRenderData3 = new SeriesRenderData();
        seriesRenderData3.setName("使用率line");
        seriesRenderData3.setValues(useRate.toArray(new Double[useRate.size()]));
        seriesRenderData3.setComboType(SeriesRenderData.ComboType.LINE);
        seriesRenderDatas2.add(seriesRenderData3);

        SeriesRenderData seriesRenderData4 = new SeriesRenderData();
        seriesRenderData4.setName("使用年限bar");
        seriesRenderData4.setValues(useYear.toArray(new Integer[useYear.size()]));
        seriesRenderData4.setComboType(SeriesRenderData.ComboType.BAR);
        seriesRenderDatas2.add(seriesRenderData4);

        barLine.setSeriesDatas(seriesRenderDatas2);
        paramMap.put("barLineCharts", barLine);

        //饼状图
        ChartSingleSeriesRenderData pie = new ChartSingleSeriesRenderData();
        pie.setChartTitle("饼状图");
        pie.setCategories(devname.toArray(new String[devname.size()]));
        pie.setSeriesData(new SeriesRenderData("电灯数量", new Integer[]{120, 25, 89, 65, 49}));
        paramMap.put("pie", pie);
        return paramMap;
    }
*/

}
