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

    @Autowired
    private ZhiBiaoSpfPzYsService zhiBiaoSpfPzYsService;

    @Autowired
    private ZhiBiaoSpfjyCqMonthService zhiBiaoSpfjyCqMonthService;

    @Autowired
    private ZhiBiaoEsfJyService zhiBiaoEsfJyService;

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

    private void spfcj(Long templateId, Map<String, Object> dataFinal, LocalDate thisDay, ExportDataPackage exportDataPackage) {
        //////////////////////全市///////////////////////////
        //综合今年数据
        Date firstDayOfYearDate = DateUtil.localDate2Date(thisDay.with(TemporalAdjusters.firstDayOfYear()));
        LocalDate thisMonthlastDay = thisDay.with(TemporalAdjusters.lastDayOfMonth());
        Date thisMonthLastdayDate = DateUtil.localDate2Date(thisMonthlastDay);
        String firstMonth_yyyyMM_thisyear = DateUtil.format(firstDayOfYearDate, "yyyyMM");
        String thisMonth_yyyyMM_thisyear = DateUtil.format(thisMonthLastdayDate, "yyyyMM");
        LocalDate lastMonth = thisDay.minusMonths(1);
        Date lastMonthDate = DateUtil.localDate2Date(lastMonth);
        String lastMonth_yyyyMM_thisyear = DateUtil.format(lastMonthDate, "yyyyMM");

        QueryWrapper<ZhiBiaoSpfjyZhCq> wrapper1 = new QueryWrapper<>();
        LambdaQueryWrapper<ZhiBiaoSpfjyZhCq> lambda1 = wrapper1.lambda();
        lambda1.ge(ZhiBiaoSpfjyZhCq::getTjsj, firstMonth_yyyyMM_thisyear);
        lambda1.le(ZhiBiaoSpfjyZhCq::getTjsj, thisMonth_yyyyMM_thisyear);
        List<ZhiBiaoSpfjyZhCq> zhiBiaoSpfjyZhCqList = zhiBiaoSpfjyZhCqService.list(wrapper1);

        //成交套数
        ZhiBiaoSpfjyZhCq taoShuThisMonth = zhiBiaoSpfjyZhCqList.stream().filter(x -> thisMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "本月套数".equals(x.getZbname())).findFirst().get();
        dataFinal.put("spfcj_taoShu_thisMonth", taoShuThisMonth.getSpfjyZbzTyCq().longValue());
        //成交面积
        ZhiBiaoSpfjyZhCq mianJiThisMonth = zhiBiaoSpfjyZhCqList.stream().filter(x -> thisMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "本月面积".equals(x.getZbname())).findFirst().get();
        dataFinal.put("spfcj_mianJi_thisMonth", mianJiThisMonth.getSpfjyZbzTyCq());
        //成交金额
        ZhiBiaoSpfjyZhCq jinE_thisMonth = zhiBiaoSpfjyZhCqList.stream().filter(x -> thisMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "本月金额".equals(x.getZbname())).findFirst().get();
        dataFinal.put("spfcj_jinE_thisMonth", jinE_thisMonth.getSpfjyZbzTyCq());
        //成交套数同比
        dataFinal.put("spfcj_tb_taoShu_thisMonth", taoShuThisMonth.getSpfjyZbzTyCqTb() + "%");
        //成交套数同比
        dataFinal.put("spfcj_tb_mianJi_thisMonth", mianJiThisMonth.getSpfjyZbzTyCqTb() + "%");
        //成交金额同比
        dataFinal.put("spfcj_tb_jinE_thisMonth", jinE_thisMonth.getSpfjyZbzTyCqTb() + "%");

        ZhiBiaoSpfjyZhCq taoShu_lastMonth = zhiBiaoSpfjyZhCqList.stream().filter(x -> lastMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "本月套数".equals(x.getZbname())).findFirst().get();
        ZhiBiaoSpfjyZhCq mianJi_lastMonth = zhiBiaoSpfjyZhCqList.stream().filter(x -> lastMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "本月面积".equals(x.getZbname())).findFirst().get();
        ZhiBiaoSpfjyZhCq jinE_lastMonth = zhiBiaoSpfjyZhCqList.stream().filter(x -> lastMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "本月金额".equals(x.getZbname())).findFirst().get();
        //成交套数环比
        BigDecimal spfcj_hb_taoShu_thisMonth = new BigDecimal((float) ((taoShuThisMonth.getSpfjyZbzTyCq() - taoShu_lastMonth.getSpfjyZbzTyCq()) / taoShu_lastMonth.getSpfjyZbzTyCq()));
        dataFinal.put("spfcj_hb_taoShu_thisMonth", spfcj_hb_taoShu_thisMonth.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //成交套数环比
        BigDecimal spfcj_hb_mianJi_thisMonth = new BigDecimal((float) ((mianJiThisMonth.getSpfjyZbzTyCq() - mianJi_lastMonth.getSpfjyZbzTyCq()) / mianJi_lastMonth.getSpfjyZbzTyCq()));
        dataFinal.put("spfcj_hb_mianJi_thisMonth", spfcj_hb_mianJi_thisMonth.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //成交金额环比
        BigDecimal spfcj_hb_jinE_thisMonth = new BigDecimal((float) ((jinE_thisMonth.getSpfjyZbzTyCq() - jinE_lastMonth.getSpfjyZbzTyCq()) / jinE_lastMonth.getSpfjyZbzTyCq()));
        dataFinal.put("spfcj_hb_jinE_thisMonth", spfcj_hb_jinE_thisMonth.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");

        ZhiBiaoSpfjyZhCq spfcj_sum_taoShu_everyMonth_obj = zhiBiaoSpfjyZhCqList.stream().filter(x -> thisMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "累计套数".equals(x.getZbname())).findFirst().get();
        ZhiBiaoSpfjyZhCq spfcj_sum_mianJi_everyMonth_obj = zhiBiaoSpfjyZhCqList.stream().filter(x -> thisMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "累计面积".equals(x.getZbname())).findFirst().get();
        ZhiBiaoSpfjyZhCq spfcj_sum_jinE_everyMonth_obj = zhiBiaoSpfjyZhCqList.stream().filter(x -> thisMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "累计金额".equals(x.getZbname())).findFirst().get();

        //全年套数 ——今年
        Long spfcj_sum_taoShu_everyMonth = spfcj_sum_taoShu_everyMonth_obj.getSpfjyZbzTyCq().longValue();
        dataFinal.put("spfcj_sum_taoShu_everyMonth", spfcj_sum_taoShu_everyMonth);
        //全年面积——今年
        Double spfcj_sum_mianJi_everyMonth = spfcj_sum_mianJi_everyMonth_obj.getSpfjyZbzTyCq();
        dataFinal.put("spfcj_sum_mianJi_everyMonth", spfcj_sum_mianJi_everyMonth);
        //全年金额——今年
        Double spfcj_sum_jinE_everyMonth = new BigDecimal(spfcj_sum_jinE_everyMonth_obj.getSpfjyZbzTyCq()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        dataFinal.put("spfcj_sum_jinE_everyMonth", spfcj_sum_jinE_everyMonth);

        /////////////////////去年数据
        LocalDate lastyear = thisDay.minusYears(1);
        Date firstDayOfYearDate_lastyear = DateUtil.localDate2Date(lastyear.with(TemporalAdjusters.firstDayOfYear()));
        LocalDate thisMonthlastDay_lastyear = lastyear.with(TemporalAdjusters.lastDayOfMonth());
        Date thisMonthLastdayDate_lastyear = DateUtil.localDate2Date(thisMonthlastDay_lastyear);
        String firstMonth_yyyyMM_lastyear = DateUtil.format(firstDayOfYearDate_lastyear, "yyyyMM");
        String thisMonth_yyyyMM_lastyear = DateUtil.format(thisMonthLastdayDate_lastyear, "yyyyMM");

        QueryWrapper<ZhiBiaoSpfjyZhCq> wrapper11 = new QueryWrapper<>();
        LambdaQueryWrapper<ZhiBiaoSpfjyZhCq> lambda11 = wrapper11.lambda();
        lambda11.ge(ZhiBiaoSpfjyZhCq::getTjsj, firstMonth_yyyyMM_lastyear);
        lambda11.le(ZhiBiaoSpfjyZhCq::getTjsj, thisMonth_yyyyMM_lastyear);
        List<ZhiBiaoSpfjyZhCq> zhiBiaoSpfjyZhCqList_lastyear = zhiBiaoSpfjyZhCqService.list(wrapper11);

        ZhiBiaoSpfjyZhCq spfcj_sum_taoShu_everyMonth_lastyear = zhiBiaoSpfjyZhCqList_lastyear.stream().filter(x -> thisMonth_yyyyMM_lastyear.equals(x.getTjsj()) && "累计套数".equals(x.getZbname())).findFirst().get();
        ZhiBiaoSpfjyZhCq spfcj_sum_mianJi_everyMonth_lastyear = zhiBiaoSpfjyZhCqList_lastyear.stream().filter(x -> thisMonth_yyyyMM_lastyear.equals(x.getTjsj()) && "累计面积".equals(x.getZbname())).findFirst().get();
        ZhiBiaoSpfjyZhCq spfcj_sum_jinE_everyMonth_lastyear = zhiBiaoSpfjyZhCqList_lastyear.stream().filter(x -> thisMonth_yyyyMM_lastyear.equals(x.getTjsj()) && "累计金额".equals(x.getZbname())).findFirst().get();

        //成交套数同比-全年度
        BigDecimal spfcj_hb_taoShu_thisyear = new BigDecimal((float) ((spfcj_sum_taoShu_everyMonth_obj.getSpfjyZbzTyCq() - spfcj_sum_taoShu_everyMonth_lastyear.getSpfjyZbzTyCq()) / spfcj_sum_taoShu_everyMonth_lastyear.getSpfjyZbzTyCq()));
        dataFinal.put("spfcj_tb_taoShu_thisyear", spfcj_hb_taoShu_thisyear.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //成交套数同比-全年度
        BigDecimal spfcj_hb_mianJi_thisyear = new BigDecimal((float) ((spfcj_sum_mianJi_everyMonth_obj.getSpfjyZbzTyCq() - spfcj_sum_mianJi_everyMonth_lastyear.getSpfjyZbzTyCq()) / spfcj_sum_mianJi_everyMonth_lastyear.getSpfjyZbzTyCq()));
        dataFinal.put("spfcj_tb_mianJi_thisyear", spfcj_hb_mianJi_thisyear.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //成交金额同比-全年度
        BigDecimal spfcj_hb_jinE_thisyear = new BigDecimal((float) ((spfcj_sum_jinE_everyMonth_obj.getSpfjyZbzTyCq() - spfcj_sum_jinE_everyMonth_lastyear.getSpfjyZbzTyCq()) / spfcj_sum_jinE_everyMonth_lastyear.getSpfjyZbzTyCq()));
        dataFinal.put("spfcj_tb_jinE_thisyear", spfcj_hb_jinE_thisyear.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");


        //////////////////////市区///////////////////////////
        //成交套数
        dataFinal.put("spfcj_taoShu_thisMonth_shiqu", taoShuThisMonth.getSpfjyZbzTySq().longValue());
        //成交面积
        dataFinal.put("spfcj_mianJi_thisMonth_shiqu", mianJiThisMonth.getSpfjyZbzTySq());
        //成交金额
        dataFinal.put("spfcj_jinE_thisMonth_shiqu", jinE_thisMonth.getSpfjyZbzTySq());
        //成交套数同比
        dataFinal.put("spfcj_tb_taoShu_thisMonth_shiqu", taoShuThisMonth.getSpfjyZbzTySqTb() + "%");
        //成交面积同比
        dataFinal.put("spfcj_tb_mianJi_thisMonth_shiqu", mianJiThisMonth.getSpfjyZbzTySqTb() + "%");
        //成交金额同比
        dataFinal.put("spfcj_tb_jinE_thisMonth_shiqu", jinE_thisMonth.getSpfjyZbzTySqTb() + "%");

        //成交套数环比
        BigDecimal spfcj_hb_taoShu_thisMonth_shiqu = new BigDecimal((Double) ((taoShuThisMonth.getSpfjyZbzTySq() - taoShu_lastMonth.getSpfjyZbzTySq()) / taoShu_lastMonth.getSpfjyZbzTySq()));
        dataFinal.put("spfcj_hb_taoShu_thisMonth_shiqu", spfcj_hb_taoShu_thisMonth_shiqu.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //成交面积环比
        BigDecimal spfcj_hb_mianJi_thisMonth_shiqu = new BigDecimal((Double) ((mianJiThisMonth.getSpfjyZbzTySq() - mianJi_lastMonth.getSpfjyZbzTySq()) / mianJi_lastMonth.getSpfjyZbzTySq()));
        dataFinal.put("spfcj_hb_mianJi_thisMonth_shiqu", spfcj_hb_mianJi_thisMonth_shiqu.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //成交金额环比
        BigDecimal spfcj_hb_jinE_thisMonth_shiqu = new BigDecimal((Double) ((jinE_thisMonth.getSpfjyZbzTySq() - jinE_lastMonth.getSpfjyZbzTySq()) / jinE_lastMonth.getSpfjyZbzTySq()));
        dataFinal.put("spfcj_hb_jinE_thisMonth_shiqu", spfcj_hb_jinE_thisMonth_shiqu.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");

        //全年套数 ——今年
        Double spfcj_sum_taoShu_everyMonth_shiqu = spfcj_sum_taoShu_everyMonth_obj.getSpfjyZbzTySq();
        dataFinal.put("spfcj_sum_taoShu_everyMonth_shiqu", spfcj_sum_taoShu_everyMonth_shiqu.longValue());
        //全年面积——今年
        Double spfcj_sum_mianJi_everyMonth_shiqu = spfcj_sum_mianJi_everyMonth_obj.getSpfjyZbzTySq();
        dataFinal.put("spfcj_sum_mianJi_everyMonth_shiqu", spfcj_sum_mianJi_everyMonth_shiqu);
        //全年金额——今年
        Double spfcj_sum_jinE_everyMonth_shiqu = spfcj_sum_jinE_everyMonth_obj.getSpfjyZbzTySq();
        dataFinal.put("spfcj_sum_jinE_everyMonth_shiqu", new BigDecimal(spfcj_sum_jinE_everyMonth_shiqu).setScale(2, BigDecimal.ROUND_HALF_UP));

        //去年数据
        //成交套数同比-全年度
        Double spfcj_sum_taoShu_everyMonth_lastyear_shiqu = spfcj_sum_taoShu_everyMonth_lastyear.getSpfjyZbzTySq();
        BigDecimal spfcj_tb_taoShu_thisyear_shiqu = new BigDecimal((Double) ((spfcj_sum_taoShu_everyMonth_shiqu - spfcj_sum_taoShu_everyMonth_lastyear_shiqu) / spfcj_sum_taoShu_everyMonth_lastyear_shiqu));
        dataFinal.put("spfcj_tb_taoShu_thisyear_shiqu", spfcj_tb_taoShu_thisyear_shiqu.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //成交套数同比-全年度
        Double spfcj_sum_mianJi_everyMonth_lastyear_shiqu = spfcj_sum_mianJi_everyMonth_lastyear.getSpfjyZbzTySq();
        BigDecimal spfcj_tb_mianJi_thisyear_shiqu = new BigDecimal((Double) ((spfcj_sum_mianJi_everyMonth_shiqu - spfcj_sum_mianJi_everyMonth_lastyear_shiqu) / spfcj_sum_mianJi_everyMonth_lastyear_shiqu));
        dataFinal.put("spfcj_tb_mianJi_thisyear_shiqu", spfcj_tb_mianJi_thisyear_shiqu.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //成交金额同比-全年度
        Double spfcj_sum_jinE_everyMonth_lastyear_shiqu = spfcj_sum_jinE_everyMonth_lastyear.getSpfjyZbzTySq();
        BigDecimal spfcj_tb_jinE_thisyear_shiqu = new BigDecimal((Double) ((spfcj_sum_jinE_everyMonth_shiqu - spfcj_sum_jinE_everyMonth_lastyear_shiqu) / spfcj_sum_jinE_everyMonth_lastyear_shiqu));
        dataFinal.put("spfcj_tb_jinE_thisyear_shiqu", spfcj_tb_jinE_thisyear_shiqu.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");


        //////////////////////均价-全商品房/////////////////////////////
        //成交均价-全市
        ZhiBiaoSpfjyZhCq jj_thisMonth = zhiBiaoSpfjyZhCqList.stream().filter(x -> thisMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "本月均价".equals(x.getZbname())).findFirst().get();
        dataFinal.put("spfcj_jj_thisMonth", jj_thisMonth.getSpfjyZbzTyCq().longValue());
        //成交均价同比-全市
        dataFinal.put("spfcj_tb_jj_thisMonth", jj_thisMonth.getSpfjyZbzTyCqTb() + "%");
        ZhiBiaoSpfjyZhCq jj_lastMonth = zhiBiaoSpfjyZhCqList.stream().filter(x -> lastMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "本月均价".equals(x.getZbname())).findFirst().get();
        //成交金额环比-全市
        BigDecimal spfcj_hb_jj_thisMonth = new BigDecimal((float) ((jj_thisMonth.getSpfjyZbzTyCq() - jj_lastMonth.getSpfjyZbzTyCq()) / jj_lastMonth.getSpfjyZbzTyCq()));
        dataFinal.put("spfcj_hb_jj_thisMonth", spfcj_hb_jj_thisMonth.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");

        //成交均价-市区
        ZhiBiaoSpfjyZhCq jj_thisMonth_shiqu = zhiBiaoSpfjyZhCqList.stream().filter(x -> thisMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "本月均价".equals(x.getZbname())).findFirst().get();
        dataFinal.put("spfcj_jj_thisMonth_shiqu", jj_thisMonth_shiqu.getSpfjyZbzTySq().longValue());
        //成交均价同比-市区
        dataFinal.put("spfcj_tb_jj_thisMonth_shiqu", jj_thisMonth_shiqu.getSpfjyZbzTySqTb() + "%");
        ZhiBiaoSpfjyZhCq jj_lastMonth_shiqu = zhiBiaoSpfjyZhCqList.stream().filter(x -> lastMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "本月均价".equals(x.getZbname())).findFirst().get();
        //成交金额环比-市区
        BigDecimal spfcj_hb_jj_thisMonth_shiqu = new BigDecimal((float) ((jj_thisMonth_shiqu.getSpfjyZbzTySq() - jj_lastMonth_shiqu.getSpfjyZbzTySq()) / jj_lastMonth_shiqu.getSpfjyZbzTySq()));
        dataFinal.put("spfcj_hb_jj_thisMonth_shiqu", spfcj_hb_jj_thisMonth_shiqu.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");

        //全年均价——今年
        ZhiBiaoSpfjyZhCq spfcj_sum_jj_everyMonth_object = zhiBiaoSpfjyZhCqList.stream().filter(x -> thisMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "累计均价".equals(x.getZbname())).findFirst().get();
        BigDecimal spfcj_sum_jj_everyMonth = new BigDecimal(spfcj_sum_jj_everyMonth_object.getSpfjyZbzTyCq()).setScale(2, BigDecimal.ROUND_HALF_UP);
        dataFinal.put("spfcj_sum_jj_everyMonth", spfcj_sum_jj_everyMonth);
        //成交均价同比-全年度
        ZhiBiaoSpfjyZhCq spfcj_sum_jj_everyMonth_lastyear = zhiBiaoSpfjyZhCqList_lastyear.stream().filter(x -> thisMonth_yyyyMM_lastyear.equals(x.getTjsj()) && "累计均价".equals(x.getZbname())).findFirst().get();
        BigDecimal spfcj_hb_jj_thisyear = new BigDecimal((float) ((spfcj_sum_jj_everyMonth_object.getSpfjyZbzTyCq() - spfcj_sum_jj_everyMonth_lastyear.getSpfjyZbzTyCq()) / spfcj_sum_jj_everyMonth_lastyear.getSpfjyZbzTyCq()));
        dataFinal.put("spfcj_tb_jj_thisyear", spfcj_hb_jj_thisyear.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");

        //全年均价——今年-市区
        dataFinal.put("spfcj_sum_jj_everyMonth_shiqu", new BigDecimal(spfcj_sum_jj_everyMonth_object.getSpfjyZbzTySq()).setScale(2, BigDecimal.ROUND_HALF_UP));
        //成交均价同比-全年度-市区
        BigDecimal spfcj_hb_jj_thisyear_shiqu = new BigDecimal((float) ((spfcj_sum_jj_everyMonth_object.getSpfjyZbzTySq() - spfcj_sum_jj_everyMonth_lastyear.getSpfjyZbzTySq()) / spfcj_sum_jj_everyMonth_lastyear.getSpfjyZbzTySq()));
        dataFinal.put("spfcj_hb_jj_thisyear_shiqu", spfcj_hb_jj_thisyear_shiqu.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");


        /////////////////////商品房中住宅////////////////////////////////
        //综合今年数据
        //成交套数
        dataFinal.put("spfcj_taoShu_thisMonth_zz", taoShuThisMonth.getSpfjyZbzZzTyCq().longValue());
        //成交面积
        dataFinal.put("spfcj_mianJi_thisMonth_zz", mianJiThisMonth.getSpfjyZbzZzTyCq());
        //成交金额
        dataFinal.put("spfcj_jinE_thisMonth_zz", jinE_thisMonth.getSpfjyZbzZzTyCq());
        //成交套数同比
        dataFinal.put("spfcj_tb_taoShu_thisMonth_zz", taoShuThisMonth.getSpfjyZbzZzTyCqTb() + "%");
        //成交面积同比
        dataFinal.put("spfcj_tb_mianJi_thisMonth_zz", mianJiThisMonth.getSpfjyZbzZzTyCqTb() + "%");
        //成交金额同比
        dataFinal.put("spfcj_tb_jinE_thisMonth_zz", jinE_thisMonth.getSpfjyZbzZzTyCqTb() + "%");

        //成交套数环比
        BigDecimal spfcj_hb_taoShu_thisMonth_zz = new BigDecimal((float) ((taoShuThisMonth.getSpfjyZbzZzTyCq() - taoShu_lastMonth.getSpfjyZbzZzTyCq()) / taoShu_lastMonth.getSpfjyZbzZzTyCq()));
        dataFinal.put("spfcj_hb_taoShu_thisMonth_zz", spfcj_hb_taoShu_thisMonth_zz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //成交套数环比
        BigDecimal spfcj_hb_mianJi_thisMonth_zz = new BigDecimal((float) ((mianJiThisMonth.getSpfjyZbzZzTyCq() - mianJi_lastMonth.getSpfjyZbzZzTyCq()) / mianJi_lastMonth.getSpfjyZbzZzTyCq()));
        dataFinal.put("spfcj_hb_mianJi_thisMonth_zz", spfcj_hb_mianJi_thisMonth_zz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //成交金额环比
        BigDecimal spfcj_hb_jinE_thisMonth_zz = new BigDecimal((float) ((jinE_thisMonth.getSpfjyZbzZzTyCq() - jinE_lastMonth.getSpfjyZbzZzTyCq()) / jinE_lastMonth.getSpfjyZbzZzTyCq()));
        dataFinal.put("spfcj_hb_jinE_thisMonth_zz", spfcj_hb_jinE_thisMonth_zz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");

        //全年套数 ——今年
        dataFinal.put("spfcj_sum_taoShu_everyMonth_zz", spfcj_sum_taoShu_everyMonth_obj.getSpfjyZbzZzTyCq().longValue());
        //全年面积——今年
        dataFinal.put("spfcj_sum_mianJi_everyMonth_zz", spfcj_sum_mianJi_everyMonth_obj.getSpfjyZbzZzTyCq());
        //全年金额——今年
        dataFinal.put("spfcj_sum_jinE_everyMonth_zz", new BigDecimal(spfcj_sum_jinE_everyMonth_obj.getSpfjyZbzZzTyCq()).setScale(2, BigDecimal.ROUND_HALF_UP));

        /////////////////////去年数据
        //成交套数同比-全年度
        BigDecimal spfcj_tb_taoShu_thisyear_zz = new BigDecimal((float) ((spfcj_sum_taoShu_everyMonth_obj.getSpfjyZbzZzTyCq() - spfcj_sum_taoShu_everyMonth_lastyear.getSpfjyZbzZzTyCq()) / spfcj_sum_taoShu_everyMonth_lastyear.getSpfjyZbzZzTyCq()));
        dataFinal.put("spfcj_tb_taoShu_thisyear_zz", spfcj_tb_taoShu_thisyear_zz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //成交套数同比-全年度
        BigDecimal spfcj_tb_mianJi_thisyear_zz = new BigDecimal((float) ((spfcj_sum_mianJi_everyMonth_obj.getSpfjyZbzZzTyCq() - spfcj_sum_mianJi_everyMonth_lastyear.getSpfjyZbzZzTyCq()) / spfcj_sum_mianJi_everyMonth_lastyear.getSpfjyZbzZzTyCq()));
        dataFinal.put("spfcj_tb_mianJi_thisyear_zz", spfcj_tb_mianJi_thisyear_zz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //成交金额同比-全年度
        BigDecimal spfcj_tb_jinE_thisyear_zz = new BigDecimal((float) ((spfcj_sum_jinE_everyMonth_obj.getSpfjyZbzZzTyCq() - spfcj_sum_jinE_everyMonth_lastyear.getSpfjyZbzZzTyCq()) / spfcj_sum_jinE_everyMonth_lastyear.getSpfjyZbzZzTyCq()));
        dataFinal.put("spfcj_tb_jinE_thisyear_zz", spfcj_tb_jinE_thisyear_zz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");


        //////////////////////市区///////////////////////////
        //成交套数
        dataFinal.put("spfcj_taoShu_thisMonth_shiqu_zz", taoShuThisMonth.getSpfjyZbzZzTySq().longValue());
        //成交面积
        dataFinal.put("spfcj_mianJi_thisMonth_shiqu_zz", mianJiThisMonth.getSpfjyZbzZzTySq());
        //成交金额
        dataFinal.put("spfcj_jinE_thisMonth_shiqu_zz", jinE_thisMonth.getSpfjyZbzZzTySq());
        //成交金额同比
        dataFinal.put("spfcj_tb_taoShu_thisMonth_shiqu_zz", taoShuThisMonth.getSpfjyZbzZzTySqTb() + "%");
        //成交金额同比
        dataFinal.put("spfcj_tb_mianJi_thisMonth_shiqu_zz", mianJiThisMonth.getSpfjyZbzZzTySqTb() + "%");
        //成交金额同比
        dataFinal.put("spfcj_tb_jinE_thisMonth_shiqu_zz", jinE_thisMonth.getSpfjyZbzZzTySqTb() + "%");

        //成交套数环比
        BigDecimal spfcj_hb_taoShu_thisMonth_shiqu_zz = new BigDecimal((Double) ((taoShuThisMonth.getSpfjyZbzZzTySq() - taoShu_lastMonth.getSpfjyZbzZzTySq()) / taoShu_lastMonth.getSpfjyZbzZzTySq()));
        dataFinal.put("spfcj_hb_taoShu_thisMonth_shiqu_zz", spfcj_hb_taoShu_thisMonth_shiqu_zz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //成交套数环比
        BigDecimal spfcj_hb_mianJi_thisMonth_shiqu_zz = new BigDecimal((Double) ((mianJiThisMonth.getSpfjyZbzZzTySq() - mianJi_lastMonth.getSpfjyZbzZzTySq()) / mianJi_lastMonth.getSpfjyZbzZzTySq()));
        dataFinal.put("spfcj_hb_mianJi_thisMonth_shiqu_zz", spfcj_hb_mianJi_thisMonth_shiqu_zz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //成交金额环比
        BigDecimal spfcj_hb_jinE_thisMonth_shiqu_zz = new BigDecimal((Double) ((jinE_thisMonth.getSpfjyZbzZzTySq() - jinE_lastMonth.getSpfjyZbzZzTySq()) / jinE_lastMonth.getSpfjyZbzZzTySq()));
        dataFinal.put("spfcj_hb_jinE_thisMonth_shiqu_zz", spfcj_hb_jinE_thisMonth_shiqu_zz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");

        //全年套数 ——今年
        Double spfcj_sum_taoShu_everyMonth_shiqu_zz = spfcj_sum_taoShu_everyMonth_obj.getSpfjyZbzZzTySq();
        dataFinal.put("spfcj_sum_taoShu_everyMonth_shiqu_zz", spfcj_sum_taoShu_everyMonth_shiqu_zz.longValue());
        //全年面积——今年
        Double spfcj_sum_mianJi_everyMonth_shiqu_zz = spfcj_sum_mianJi_everyMonth_obj.getSpfjyZbzZzTySq();
        dataFinal.put("spfcj_sum_mianJi_everyMonth_shiqu_zz", spfcj_sum_mianJi_everyMonth_shiqu_zz);
        //全年金额——今年
        Double spfcj_sum_jinE_everyMonth_shiqu_zz = spfcj_sum_jinE_everyMonth_obj.getSpfjyZbzZzTySq();
        dataFinal.put("spfcj_sum_jinE_everyMonth_shiqu_zz", new BigDecimal(spfcj_sum_jinE_everyMonth_shiqu_zz).setScale(2, BigDecimal.ROUND_HALF_UP));

        //去年数据
        //成交套数同比-全年度
        Double spfcj_sum_taoShu_everyMonth_lastyear_shiqu_zz = spfcj_sum_taoShu_everyMonth_lastyear.getSpfjyZbzZzTySq();
        BigDecimal spfcj_tb_taoShu_thisyear_shiqu_zz = new BigDecimal((Double) ((spfcj_sum_taoShu_everyMonth_shiqu_zz - spfcj_sum_taoShu_everyMonth_lastyear_shiqu_zz) / spfcj_sum_taoShu_everyMonth_lastyear_shiqu_zz));
        dataFinal.put("spfcj_tb_taoShu_thisyear_shiqu_zz", spfcj_tb_taoShu_thisyear_shiqu_zz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //成交套数同比-全年度
        Double spfcj_sum_mianJi_everyMonth_lastyear_shiqu_zz = spfcj_sum_mianJi_everyMonth_lastyear.getSpfjyZbzZzTySq();
        BigDecimal spfcj_tb_mianJi_thisyear_shiqu_zz = new BigDecimal((Double) ((spfcj_sum_mianJi_everyMonth_shiqu_zz - spfcj_sum_mianJi_everyMonth_lastyear_shiqu_zz) / spfcj_sum_mianJi_everyMonth_lastyear_shiqu_zz));
        dataFinal.put("spfcj_tb_mianJi_thisyear_shiqu_zz", spfcj_tb_mianJi_thisyear_shiqu_zz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //成交金额同比-全年度
        Double spfcj_sum_jinE_everyMonth_lastyear_shiqu_zz = spfcj_sum_jinE_everyMonth_lastyear.getSpfjyZbzZzTySq();
        BigDecimal spfcj_tb_jinE_thisyear_shiqu_zz = new BigDecimal((Double) ((spfcj_sum_jinE_everyMonth_shiqu_zz - spfcj_sum_jinE_everyMonth_lastyear_shiqu_zz) / spfcj_sum_jinE_everyMonth_lastyear_shiqu_zz));
        dataFinal.put("spfcj_tb_jinE_thisyear_shiqu_zz", spfcj_tb_jinE_thisyear_shiqu_zz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");


        // 成交 按城区 , 月
        QueryWrapper<ZhiBiaoSpfjyCqMonth> wrapper2 = new QueryWrapper<>();
        LambdaQueryWrapper<ZhiBiaoSpfjyCqMonth> lambda2 = wrapper2.lambda();
        lambda2.ge(ZhiBiaoSpfjyCqMonth::getTjsj, firstMonth_yyyyMM_thisyear);
        lambda2.le(ZhiBiaoSpfjyCqMonth::getTjsj, thisMonth_yyyyMM_thisyear);
        List<ZhiBiaoSpfjyCqMonth> zhiBiaoSpfjyCqMonthList_thisyear = zhiBiaoSpfjyCqMonthService.list(wrapper2);
        List<ZhiBiaoSpfjyCqMonth> zhiBiaoSpfjyCqMonthList_thismonth = zhiBiaoSpfjyCqMonthList_thisyear.stream().filter(x -> thisMonth_yyyyMM_thisyear.equals(x.getTjsj())).collect(Collectors.toList());
        //这个月 各套数区总和
        Long sumTaoShu_thismonth = zhiBiaoSpfjyCqMonthList_thismonth.stream().filter(x -> x.getCqmc().equals("全市合计")).map(x -> x.getSpfjyTsCntZzTmCq()).findFirst().get();
        //Long sumTaoShu_thismonth = zhiBiaoSpfjyCqMonthList_thismonth.stream().mapToLong(ZhiBiaoSpfjyCqMonth::getSpfjyTsCntZzTmCq).sum();
        //这个月  套数排名前3的区 
        List<ZhiBiaoSpfjyCqMonth> zhiBiaoSpfjyCqMonthList_thismonth_sortTaoShu_top3 = zhiBiaoSpfjyCqMonthList_thismonth.stream().filter(x -> x.getSpfjyTsCntZzTmCq() != null && x.getCqmc() != null && !"全市合计".equals(x.getCqmc())).sorted(Comparator.comparing(ZhiBiaoSpfjyCqMonth::getSpfjyTsCntZzTmCq).reversed()).limit(3).collect(Collectors.toList());

        List<String> cqList = zhiBiaoSpfjyCqMonthList_thismonth_sortTaoShu_top3.stream().filter(x -> x.getCqmc() != null).map(x -> x.getCqmc()).collect(Collectors.toList());
        dataFinal.put("spfcj_maxCqtop3_cq_shiqu_zz", String.join(",", cqList));
        //这个月  套数排名1的区 
        dataFinal.put("spfcj_maxCq1_cq_shiqu_zz", zhiBiaoSpfjyCqMonthList_thismonth_sortTaoShu_top3.get(0).getCqmc());
        Long spfcj_maxCq1TaoShu_cq_shiqu_zz = zhiBiaoSpfjyCqMonthList_thismonth_sortTaoShu_top3.get(0).getSpfjyTsCntFzzTmCq();
        dataFinal.put("spfcj_maxCq1TaoShu_cq_shiqu_zz", spfcj_maxCq1TaoShu_cq_shiqu_zz);
        //BigDecimal spfcj_maxCq1TaoShu_zb_cq_shiqu_zz = new BigDecimal((float) (spfcj_maxCq1TaoShu_cq_shiqu_zz.floatValue() / sumTaoShu_thismonth.floatValue()));
        //dataFinal.put("spfcj_maxCq1TaoShu_zb_cq_shiqu_zz", spfcj_maxCq1TaoShu_zb_cq_shiqu_zz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //这个月  套数排名2的区 
        dataFinal.put("spfcj_maxCq2_cq_shiqu_zz", zhiBiaoSpfjyCqMonthList_thismonth_sortTaoShu_top3.get(1).getCqmc());
        Long spfcj_maxCq2TaoShu_cq_shiqu_zz = zhiBiaoSpfjyCqMonthList_thismonth_sortTaoShu_top3.get(1).getSpfjyTsCntFzzTmCq();
        dataFinal.put("spfcj_maxCq2TaoShu_cq_shiqu_zz", spfcj_maxCq2TaoShu_cq_shiqu_zz);
        //BigDecimal spfcj_maxCq2TaoShu_zb_cq_shiqu_zz = new BigDecimal((float) (spfcj_maxCq2TaoShu_cq_shiqu_zz.floatValue() / sumTaoShu_thismonth.floatValue()));
        //dataFinal.put("spfcj_maxCq2TaoShu_zb_cq_shiqu_zz", spfcj_maxCq2TaoShu_zb_cq_shiqu_zz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //这个月  套数排名3的区 
        dataFinal.put("spfcj_maxCq3_cq_shiqu_zz", zhiBiaoSpfjyCqMonthList_thismonth_sortTaoShu_top3.get(2).getCqmc());
        Long spfcj_maxCq3TaoShu_cq_shiqu_zz = zhiBiaoSpfjyCqMonthList_thismonth_sortTaoShu_top3.get(2).getSpfjyTsCntFzzTmCq();
        dataFinal.put("spfcj_maxCq3TaoShu_cq_shiqu_zz", spfcj_maxCq3TaoShu_cq_shiqu_zz);
        BigDecimal spfcj_maxCq3TaoShu_zb_cq_shiqu_zz = new BigDecimal((float) (spfcj_maxCq3TaoShu_cq_shiqu_zz.floatValue() / sumTaoShu_thismonth.floatValue()));
        dataFinal.put("spfcj_maxCq3TaoShu_zb_cq_shiqu_zz", spfcj_maxCq3TaoShu_zb_cq_shiqu_zz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //淳安 建德 桐庐
        List qxList = new ArrayList();
        qxList.add("淳安县");
        qxList.add("建德市");
        qxList.add("桐庐县");
        List<ZhiBiaoSpfjyCqMonth> qxcollect = zhiBiaoSpfjyCqMonthList_thismonth.stream().filter(x -> qxList.contains(x.getCqmc())).collect(Collectors.toList());
        Long sumqx = qxcollect.stream().mapToLong(ZhiBiaoSpfjyCqMonth::getSpfjyTsCntZzTmCq).sum();
        dataFinal.put("spfcj_sum_qx_cq_shiqu_zz", sumqx);
        BigDecimal spfcj_zb_qx_cq_shiqu_zz = new BigDecimal((float) (sumqx.floatValue() / sumTaoShu_thismonth.floatValue()));
        dataFinal.put("spfcj_zb_qx_cq_shiqu_zz", spfcj_zb_qx_cq_shiqu_zz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");


        ////////////////////////商品房预售
        QueryWrapper<ZhiBiaoSpfPzYs> wrapper3 = new QueryWrapper<>();
        LambdaQueryWrapper<ZhiBiaoSpfPzYs> lambda3 = wrapper3.lambda();
        lambda3.ge(ZhiBiaoSpfPzYs::getTjsj, firstMonth_yyyyMM_thisyear);
        lambda3.le(ZhiBiaoSpfPzYs::getTjsj, thisMonth_yyyyMM_thisyear);
        List<ZhiBiaoSpfPzYs> zhiBiaoSpfPzYsCqList = zhiBiaoSpfPzYsService.list(wrapper3);

        //预售套数
        ZhiBiaoSpfPzYs pzys_taoshu_thisMonth = zhiBiaoSpfPzYsCqList.stream().filter(x -> thisMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "批准预售套数".equals(x.getZbname())).findFirst().get();
        dataFinal.put("pzys_taoshu_thisMonth", pzys_taoshu_thisMonth.getYsPzysZbzTmAll().longValue());
        //预售mj
        ZhiBiaoSpfPzYs pzys_mj_thisMonth = zhiBiaoSpfPzYsCqList.stream().filter(x -> thisMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "批准预售面积".equals(x.getZbname())).findFirst().get();
        dataFinal.put("pzys_mj_thisMonth", pzys_mj_thisMonth.getYsPzysZbzTmAll());

        //预售套数tongbi
        BigDecimal pzys_tb_taoShu_thisMonth = new BigDecimal((float) ((pzys_taoshu_thisMonth.getYsPzysZbzTmAll().floatValue() - pzys_taoshu_thisMonth.getYsPzysZbzTmLastyAll().floatValue()) / pzys_taoshu_thisMonth.getYsPzysZbzTmLastyAll().floatValue()));
        dataFinal.put("pzys_tb_taoShu_thisMonth", pzys_tb_taoShu_thisMonth.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //预售面积tongbi
        BigDecimal pzys_hb_mj_thisMonth = new BigDecimal((float) ((pzys_mj_thisMonth.getYsPzysZbzTmAll().floatValue() - pzys_mj_thisMonth.getYsPzysZbzTmLastyAll().floatValue()) / pzys_mj_thisMonth.getYsPzysZbzTmLastyAll().floatValue()));
        dataFinal.put("pzys_tb_mj_thisMonth", pzys_hb_mj_thisMonth.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");

        ZhiBiaoSpfPzYs pzys_taoshu_lastMonth = zhiBiaoSpfPzYsCqList.stream().filter(x -> lastMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "批准预售套数".equals(x.getZbname())).findFirst().get();
        ZhiBiaoSpfPzYs pzys_mj_lastMonth = zhiBiaoSpfPzYsCqList.stream().filter(x -> lastMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "批准预售面积".equals(x.getZbname())).findFirst().get();
        //预售套数环比
        BigDecimal pzys_hb_taoShu_thisMonth = new BigDecimal((float) ((pzys_taoshu_thisMonth.getYsPzysZbzTmAll() - pzys_taoshu_lastMonth.getYsPzysZbzTmAll()) / pzys_taoshu_lastMonth.getYsPzysZbzTmAll()));
        dataFinal.put("pzys_hb_taoShu_thisMonth", pzys_hb_taoShu_thisMonth.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //预售套数环比
        BigDecimal pzys_hb_mianJi_thisMonth = new BigDecimal((float) ((pzys_mj_thisMonth.getYsPzysZbzTmAll() - pzys_mj_lastMonth.getYsPzysZbzTmAll()) / pzys_mj_lastMonth.getYsPzysZbzTmAll()));
        dataFinal.put("pzys_hb_mianJi_thisMonth", pzys_hb_mianJi_thisMonth.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");

        //全年套数 ——今年
        dataFinal.put("pzys_sum_taoShu_everyMonth", pzys_taoshu_thisMonth.getYsPzysZbzTyAll().longValue());
        //全年面积——今年
        dataFinal.put("pzys_sum_mj_everyMonth", pzys_mj_thisMonth.getYsPzysZbzTyAll());
        //预售套数tongbi 全年
        BigDecimal pzys_tb_taoShu_everyMonth = new BigDecimal((float) ((pzys_taoshu_thisMonth.getYsPzysZbzTyAll().floatValue() - pzys_taoshu_thisMonth.getYsPzysZbzTyLastyAll().floatValue()) / pzys_taoshu_thisMonth.getYsPzysZbzTyLastyAll().floatValue()));
        dataFinal.put("pzys_tb_taoShu_everyMonth", pzys_tb_taoShu_everyMonth.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //预售面积tongbi 全年
        BigDecimal pzys_tb_mj_everyMonth = new BigDecimal((float) ((pzys_mj_thisMonth.getYsPzysZbzTyAll().floatValue() - pzys_mj_thisMonth.getYsPzysZbzTyLastyAll().floatValue()) / pzys_mj_thisMonth.getYsPzysZbzTyLastyAll().floatValue()));
        dataFinal.put("pzys_tb_mj_everyMonth", pzys_tb_mj_everyMonth.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");

        ////////////////////////商品房预售-市区
        //预售套数
        dataFinal.put("pzys_taoshu_thisMonth_shiqu", pzys_taoshu_thisMonth.getYsPzysZbzTmSq().longValue());
        //预售mj
        dataFinal.put("pzys_mj_thisMonth_shiqu", pzys_mj_thisMonth.getYsPzysZbzTmSq());

        //预售套数tongbi
        BigDecimal pzys_tb_taoShu_thisMonth_shiqu = new BigDecimal((float) ((pzys_taoshu_thisMonth.getYsPzysZbzTmSq().floatValue() - pzys_taoshu_thisMonth.getYsPzysZbzTmLastySq().floatValue()) / pzys_taoshu_thisMonth.getYsPzysZbzTmLastySq().floatValue()));
        dataFinal.put("pzys_tb_taoShu_thisMonth_shiqu", pzys_tb_taoShu_thisMonth_shiqu.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //预售面积tongbi
        BigDecimal pzys_tb_mj_thisMonth_shiqu = new BigDecimal((float) ((pzys_mj_thisMonth.getYsPzysZbzTmSq().floatValue() - pzys_mj_thisMonth.getYsPzysZbzTmLastySq().floatValue()) / pzys_mj_thisMonth.getYsPzysZbzTmLastySq().floatValue()));
        dataFinal.put("pzys_tb_mj_thisMonth_shiqu", pzys_tb_taoShu_thisMonth_shiqu.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");

        //预售套数环比
        BigDecimal pzys_hb_taoShu_thisMonth_shiqu = new BigDecimal((float) ((pzys_taoshu_thisMonth.getYsPzysZbzTmSq() - pzys_taoshu_lastMonth.getYsPzysZbzTmSq()) / pzys_taoshu_lastMonth.getYsPzysZbzTmSq()));
        dataFinal.put("pzys_hb_taoShu_thisMonth_shiqu", pzys_hb_taoShu_thisMonth_shiqu.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //预售mj环比
        BigDecimal pzys_hb_mianJi_thisMonth_shiqu = new BigDecimal((float) ((pzys_mj_thisMonth.getYsPzysZbzTmSq() - pzys_mj_lastMonth.getYsPzysZbzTmSq()) / pzys_mj_lastMonth.getYsPzysZbzTmSq()));
        dataFinal.put("pzys_hb_mianJi_thisMonth_shiqu", pzys_hb_mianJi_thisMonth_shiqu.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");

        //全年套数 ——今年
        dataFinal.put("pzys_sum_taoShu_everyMonth_shiqu", pzys_taoshu_thisMonth.getYsPzysZbzTySq().longValue());
        //全年面积——今年
        dataFinal.put("pzys_sum_mj_everyMonth_shiqu", pzys_mj_thisMonth.getYsPzysZbzTySq());
        //预售套数tongbi 全年
        BigDecimal pzys_tb_taoShu_everyMonth_shiqu = new BigDecimal((float) ((pzys_taoshu_thisMonth.getYsPzysZbzTySq().floatValue() - pzys_taoshu_thisMonth.getYsPzysZbzTyLastySq().floatValue()) / pzys_taoshu_thisMonth.getYsPzysZbzTyLastySq().floatValue()));
        dataFinal.put("pzys_tb_taoShu_everyMonth_shiqu", pzys_tb_taoShu_everyMonth_shiqu.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //预售面积tongbi 全年
        BigDecimal pzys_tb_mj_everyMonth_shiqu = new BigDecimal((float) ((pzys_mj_thisMonth.getYsPzysZbzTySq().floatValue() - pzys_mj_thisMonth.getYsPzysZbzTyLastySq().floatValue()) / pzys_mj_thisMonth.getYsPzysZbzTyLastySq().floatValue()));
        dataFinal.put("pzys_tb_mj_everyMonth_shiqu", pzys_tb_mj_everyMonth_shiqu.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");


        ///////商品房预售-市区-住宅

        //预售套数
        ZhiBiaoSpfPzYs pzys_taoshu_thisMonth_shiqu_zz = zhiBiaoSpfPzYsCqList.stream().filter(x -> thisMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "批准预售套数-住宅".equals(x.getZbname())).findFirst().get();
        dataFinal.put("pzys_taoshu_thisMonth_shiqu_zz", pzys_taoshu_thisMonth_shiqu_zz.getYsPzysZbzTmSq().longValue());
        //预售mj
        ZhiBiaoSpfPzYs pzys_mj_thisMonth_shiqu_zz = zhiBiaoSpfPzYsCqList.stream().filter(x -> thisMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "批准预售面积-住宅".equals(x.getZbname())).findFirst().get();
        dataFinal.put("pzys_mj_thisMonth_shiqu_zz", pzys_mj_thisMonth_shiqu_zz.getYsPzysZbzTmSq());


        //预售套数tongbi
        BigDecimal pzys_tb_taoShu_thisMonth_shiqu_zz = new BigDecimal((float) ((pzys_taoshu_thisMonth_shiqu_zz.getYsPzysZbzTmSq().floatValue() - pzys_taoshu_thisMonth_shiqu_zz.getYsPzysZbzTmLastySq().floatValue()) / pzys_taoshu_thisMonth_shiqu_zz.getYsPzysZbzTmLastySq().floatValue()));
        dataFinal.put("pzys_tb_taoShu_thisMonth_shiqu_zz", pzys_tb_taoShu_thisMonth_shiqu_zz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //预售面积tongbi
        BigDecimal pzys_tb_mj_thisMonth_shiqu_zz = new BigDecimal((float) ((pzys_mj_thisMonth_shiqu_zz.getYsPzysZbzTmSq().floatValue() - pzys_mj_thisMonth_shiqu_zz.getYsPzysZbzTmLastySq().floatValue()) / pzys_mj_thisMonth_shiqu_zz.getYsPzysZbzTmLastySq().floatValue()));
        dataFinal.put("pzys_tb_mj_thisMonth_shiqu_zz", pzys_tb_mj_thisMonth_shiqu_zz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");

        ZhiBiaoSpfPzYs pzys_taoshu_lastMonth_shiqu_zz = zhiBiaoSpfPzYsCqList.stream().filter(x -> lastMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "批准预售套数-住宅".equals(x.getZbname())).findFirst().get();
        ZhiBiaoSpfPzYs pzys_mj_lastMonth_shiqu_zz = zhiBiaoSpfPzYsCqList.stream().filter(x -> lastMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "批准预售面积-住宅".equals(x.getZbname())).findFirst().get();

        //预售套数环比
        BigDecimal pzys_hb_taoShu_thisMonth_shiqu_zz = new BigDecimal((float) ((pzys_taoshu_thisMonth_shiqu_zz.getYsPzysZbzTmSq() - pzys_taoshu_lastMonth_shiqu_zz.getYsPzysZbzTmSq()) / pzys_taoshu_lastMonth_shiqu_zz.getYsPzysZbzTmSq()));
        dataFinal.put("pzys_hb_taoShu_thisMonth_shiqu_zz", pzys_hb_taoShu_thisMonth_shiqu_zz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //预售mj环比
        BigDecimal pzys_hb_mianJi_thisMonth_shiqu_zz = new BigDecimal((float) ((pzys_mj_thisMonth_shiqu_zz.getYsPzysZbzTmSq() - pzys_mj_lastMonth_shiqu_zz.getYsPzysZbzTmSq()) / pzys_mj_lastMonth_shiqu_zz.getYsPzysZbzTmSq()));
        dataFinal.put("pzys_hb_mianJi_thisMonth_shiqu_zz", pzys_hb_mianJi_thisMonth_shiqu_zz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");

       /*  shuju数据不全
       //按城区 , 月
        QueryWrapper<ZhiBiaoSpfjyCqMonth> wrapper2 = new QueryWrapper<>();
        LambdaQueryWrapper<ZhiBiaoSpfjyCqMonth> lambda2 = wrapper2.lambda();
        lambda2.ge(ZhiBiaoSpfjyCqMonth::getTjsj, firstMonth_yyyyMM_thisyear);
        lambda2.le(ZhiBiaoSpfjyCqMonth::getTjsj, thisMonth_yyyyMM_thisyear);
        List<ZhiBiaoSpfjyCqMonth> zhiBiaoSpfjyCqMonthList_thisyear = zhiBiaoSpfjyCqMonthService.list(wrapper2);
        List<ZhiBiaoSpfjyCqMonth> zhiBiaoSpfjyCqMonthList_thismonth = zhiBiaoSpfjyCqMonthList_thisyear.stream().filter(x -> thisMonth_yyyyMM_thisyear.equals(x.getTjsj())).collect(Collectors.toList());
        //这个月 各套数区总和
        Long sumTaoShu_thismonth = zhiBiaoSpfjyCqMonthList_thismonth.stream().filter(x -> x.getCqmc().equals("全市合计")).map(x -> x.getSpfjyTsCntZzTmCq()).findFirst().get();
        //Long sumTaoShu_thismonth = zhiBiaoSpfjyCqMonthList_thismonth.stream().mapToLong(ZhiBiaoSpfjyCqMonth::getSpfjyTsCntZzTmCq).sum();
        //这个月  套数排名前3的区
        List<ZhiBiaoSpfjyCqMonth> zhiBiaoSpfjyCqMonthList_thismonth_sortTaoShu_top3 = zhiBiaoSpfjyCqMonthList_thismonth.stream().filter(x -> x.getSpfjyTsCntZzTmCq() != null && x.getCqmc() != null && !"全市合计".equals(x.getCqmc())).sorted(Comparator.comparing(ZhiBiaoSpfjyCqMonth::getSpfjyTsCntZzTmCq).reversed()).limit(3).collect(Collectors.toList());

        List<String> cqList = zhiBiaoSpfjyCqMonthList_thismonth_sortTaoShu_top3.stream().filter(x -> x.getCqmc() != null).map(x -> x.getCqmc()).collect(Collectors.toList());
        dataFinal.put("spfcj_maxCqtop3_cq_shiqu_zz", String.join(",", cqList));
        //这个月  套数排名1的区
        dataFinal.put("spfcj_maxCq1_cq_shiqu_zz", zhiBiaoSpfjyCqMonthList_thismonth_sortTaoShu_top3.get(0).getCqmc());
        Long spfcj_maxCq1TaoShu_cq_shiqu_zz = zhiBiaoSpfjyCqMonthList_thismonth_sortTaoShu_top3.get(0).getSpfjyTsCntFzzTmCq();
        dataFinal.put("spfcj_maxCq1TaoShu_cq_shiqu_zz", spfcj_maxCq1TaoShu_cq_shiqu_zz);
        BigDecimal spfcj_maxCq1TaoShu_zb_cq_shiqu_zz = new BigDecimal((float) (spfcj_maxCq1TaoShu_cq_shiqu_zz.floatValue() / sumTaoShu_thismonth.floatValue()));
        dataFinal.put("spfcj_maxCq1TaoShu_zb_cq_shiqu_zz", spfcj_maxCq1TaoShu_zb_cq_shiqu_zz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //这个月  套数排名2的区
        dataFinal.put("spfcj_maxCq2_cq_shiqu_zz", zhiBiaoSpfjyCqMonthList_thismonth_sortTaoShu_top3.get(1).getCqmc());
        Long spfcj_maxCq2TaoShu_cq_shiqu_zz = zhiBiaoSpfjyCqMonthList_thismonth_sortTaoShu_top3.get(1).getSpfjyTsCntFzzTmCq();
        dataFinal.put("spfcj_maxCq2TaoShu_cq_shiqu_zz", spfcj_maxCq2TaoShu_cq_shiqu_zz);
        BigDecimal spfcj_maxCq2TaoShu_zb_cq_shiqu_zz = new BigDecimal((float) (spfcj_maxCq2TaoShu_cq_shiqu_zz.floatValue() / sumTaoShu_thismonth.floatValue()));
        dataFinal.put("spfcj_maxCq2TaoShu_zb_cq_shiqu_zz", spfcj_maxCq2TaoShu_zb_cq_shiqu_zz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //这个月  套数排名3的区
        dataFinal.put("spfcj_maxCq3_cq_shiqu_zz", zhiBiaoSpfjyCqMonthList_thismonth_sortTaoShu_top3.get(2).getCqmc());
        Long spfcj_maxCq3TaoShu_cq_shiqu_zz = zhiBiaoSpfjyCqMonthList_thismonth_sortTaoShu_top3.get(2).getSpfjyTsCntFzzTmCq();
        dataFinal.put("spfcj_maxCq3TaoShu_cq_shiqu_zz", spfcj_maxCq3TaoShu_cq_shiqu_zz);
        BigDecimal spfcj_maxCq3TaoShu_zb_cq_shiqu_zz = new BigDecimal((float) (spfcj_maxCq3TaoShu_cq_shiqu_zz.floatValue() / sumTaoShu_thismonth.floatValue()));
        dataFinal.put("spfcj_maxCq3TaoShu_zb_cq_shiqu_zz", spfcj_maxCq3TaoShu_zb_cq_shiqu_zz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //淳安 建德 桐庐
        List qxList = new ArrayList();
        qxList.add("淳安县");
        qxList.add("建德市");
        qxList.add("桐庐县");
        List<ZhiBiaoSpfjyCqMonth> qxcollect = zhiBiaoSpfjyCqMonthList_thismonth.stream().filter(x -> qxList.contains(x.getCqmc())).collect(Collectors.toList());
        Long sumqx = qxcollect.stream().mapToLong(ZhiBiaoSpfjyCqMonth::getSpfjyTsCntZzTmCq).sum();
        dataFinal.put("spfcj_sum_qx_cq_shiqu_zz", sumqx);
        BigDecimal spfcj_zb_qx_cq_shiqu_zz = new BigDecimal((float) (sumqx.floatValue() / sumTaoShu_thismonth.floatValue()));

*/

        //全年套数 ——今年
        dataFinal.put("pzys_sum_taoShu_everyMonth_shiqu_zz", pzys_taoshu_thisMonth_shiqu_zz.getYsPzysZbzTySq().longValue());
        //全年面积——今年
        dataFinal.put("pzys_sum_mj_everyMonth_shiqu_zz", pzys_mj_thisMonth_shiqu_zz.getYsPzysZbzTySq());
        //预售套数tongbi 全年
        BigDecimal pzys_tb_taoShu_everyMonth_shiqu_zz = new BigDecimal((float) ((pzys_taoshu_thisMonth_shiqu_zz.getYsPzysZbzTySq().floatValue() - pzys_taoshu_thisMonth_shiqu_zz.getYsPzysZbzTyLastySq().floatValue()) / pzys_taoshu_thisMonth_shiqu_zz.getYsPzysZbzTyLastySq().floatValue()));
        dataFinal.put("pzys_tb_taoShu_everyMonth_shiqu_zz", pzys_tb_taoShu_everyMonth_shiqu_zz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //预售面积tongbi 全年
        BigDecimal pzys_tb_mj_everyMonth_shiqu_zz = new BigDecimal((float) ((pzys_mj_thisMonth_shiqu_zz.getYsPzysZbzTySq().floatValue() - pzys_mj_thisMonth_shiqu_zz.getYsPzysZbzTyLastySq().floatValue()) / pzys_mj_thisMonth_shiqu_zz.getYsPzysZbzTyLastySq().floatValue()));
        dataFinal.put("pzys_tb_mj_everyMonth_shiqu_zz", pzys_tb_mj_everyMonth_shiqu_zz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");


        //////////////////////均价-住宅/////////////////////////////
        //成交均价-全市
        dataFinal.put("spfcj_jj_thisMonth_zz", jj_thisMonth.getSpfjyZbzZzTyCq().longValue());
        //成交均价同比-全市
        dataFinal.put("spfcj_tb_jj_thisMonth_zz", jj_thisMonth.getSpfjyZbzZzTyCqTb() + "%");
        //成交金额环比-全市
        BigDecimal spfcj_hb_jj_thisMonth_zz = new BigDecimal((float) ((jj_thisMonth.getSpfjyZbzZzTyCq() - jj_lastMonth.getSpfjyZbzZzTyCq()) / jj_lastMonth.getSpfjyZbzZzTyCq()));
        dataFinal.put("spfcj_hb_jj_thisMonth_zz", spfcj_hb_jj_thisMonth_zz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");

        //成交均价-市区
        dataFinal.put("spfcj_jj_thisMonth_shiqu_zz", jj_thisMonth_shiqu.getSpfjyZbzZzTySq().longValue());
        //成交均价同比-市区
        dataFinal.put("spfcj_tb_jj_thisMonth_shiqu_zz", jj_thisMonth_shiqu.getSpfjyZbzZzTySqTb() + "%");
        //成交金额环比-市区
        BigDecimal spfcj_hb_jj_thisMonth_shiqu_zz = new BigDecimal((float) ((jj_thisMonth_shiqu.getSpfjyZbzZzTySq() - jj_lastMonth_shiqu.getSpfjyZbzZzTySq()) / jj_lastMonth_shiqu.getSpfjyZbzZzTySq()));
        dataFinal.put("spfcj_hb_jj_thisMonth_shiqu_zz", spfcj_hb_jj_thisMonth_shiqu_zz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");

        //全年均价——今年
        BigDecimal spfcj_sum_jj_everyMonth_zz = new BigDecimal(spfcj_sum_jj_everyMonth_object.getSpfjyZbzZzTyCq()).setScale(2, BigDecimal.ROUND_HALF_UP);
        dataFinal.put("spfcj_sum_jj_everyMonth_zz", spfcj_sum_jj_everyMonth_zz);
        //成交均价同比-全年度
        BigDecimal spfcj_hb_jj_thisyear_zz = new BigDecimal((float) ((spfcj_sum_jj_everyMonth_object.getSpfjyZbzZzTyCq() - spfcj_sum_jj_everyMonth_lastyear.getSpfjyZbzZzTyCq()) / spfcj_sum_jj_everyMonth_lastyear.getSpfjyZbzZzTyCq()));
        dataFinal.put("spfcj_tb_jj_thisyear_zz", spfcj_hb_jj_thisyear_zz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");

        //全年均价——今年-市区
        dataFinal.put("spfcj_sum_jj_everyMonth_shiqu_zz", new BigDecimal(spfcj_sum_jj_everyMonth_object.getSpfjyZbzZzTySq()).setScale(2, BigDecimal.ROUND_HALF_UP));
        //成交均价同比-全年度-市区
        BigDecimal spfcj_hb_jj_thisyear_shiqu_zz = new BigDecimal((float) ((spfcj_sum_jj_everyMonth_object.getSpfjyZbzZzTySq() - spfcj_sum_jj_everyMonth_lastyear.getSpfjyZbzZzTySq()) / spfcj_sum_jj_everyMonth_lastyear.getSpfjyZbzZzTySq()));
        dataFinal.put("spfcj_hb_jj_thisyear_shiqu_zz", spfcj_hb_jj_thisyear_shiqu_zz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");


        ///////////////////////////批准可售
        QueryWrapper<ZhiBiaoSpfPzks> wrapper5 = new QueryWrapper<>();
        LambdaQueryWrapper<ZhiBiaoSpfPzks> lambda5 = wrapper5.lambda();
        lambda5.ge(ZhiBiaoSpfPzks::getTjsj, firstMonth_yyyyMM_thisyear);
        lambda5.le(ZhiBiaoSpfPzks::getTjsj, thisMonth_yyyyMM_thisyear);
        List<ZhiBiaoSpfPzks> zhiBiaoSpfPzksCqList = zhiBiaoSpfPzksService.list(wrapper5);

        //可售套数
        ZhiBiaoSpfPzks pzks_taoShu_thisMonth = zhiBiaoSpfPzksCqList.stream().filter(x -> thisMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "批准可售套数-住宅".equals(x.getZbname())).findFirst().get();
        dataFinal.put("pzks_taoShu_thisMonth", pzks_taoShu_thisMonth.getSpfPzksZbzTmAll().longValue());
        //可售面积
        ZhiBiaoSpfPzks pzks_mianJi_thisMonth = zhiBiaoSpfPzksCqList.stream().filter(x -> thisMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "批准可售面积-住宅".equals(x.getZbname())).findFirst().get();
        dataFinal.put("pzks_mianJi_thisMonth", pzks_mianJi_thisMonth.getSpfPzksZbzTmAll());

        ZhiBiaoSpfPzks pzks_taoShu_lastMonth = zhiBiaoSpfPzksCqList.stream().filter(x -> lastMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "批准可售套数-住宅".equals(x.getZbname())).findFirst().get();
        ZhiBiaoSpfPzks pzks_mianJi_lastMonth = zhiBiaoSpfPzksCqList.stream().filter(x -> lastMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "批准可售面积-住宅".equals(x.getZbname())).findFirst().get();
        //可售套数环比
        BigDecimal pzks_hb_taoShu_thisMonth = new BigDecimal((float) ((pzks_taoShu_thisMonth.getSpfPzksZbzTmAll() - pzks_taoShu_lastMonth.getSpfPzksZbzTmAll()) / pzks_taoShu_lastMonth.getSpfPzksZbzTmAll()));
        dataFinal.put("pzks_hb_taoShu_thisMonth", pzks_hb_taoShu_thisMonth.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //可售套数环比
        BigDecimal pzks_hb_mianJi_thisMonth = new BigDecimal((float) ((pzks_mianJi_thisMonth.getSpfPzksZbzTmAll() - pzks_mianJi_lastMonth.getSpfPzksZbzTmAll()) / pzks_mianJi_lastMonth.getSpfPzksZbzTmAll()));
        dataFinal.put("pzks_hb_mianJi_thisMonth", pzks_hb_mianJi_thisMonth.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");

        //当月去化周期
        QueryWrapper<ZhiBiaoSpfjyZhCq> wrapper111 = new QueryWrapper<>();
        LambdaQueryWrapper<ZhiBiaoSpfjyZhCq> lambda111 = wrapper111.lambda();
        lambda111.ge(ZhiBiaoSpfjyZhCq::getTjsj, thisMonth_yyyyMM_lastyear);
        lambda111.le(ZhiBiaoSpfjyZhCq::getTjsj, thisMonth_yyyyMM_thisyear);
        List<ZhiBiaoSpfjyZhCq> zhiBiaoSpfjyZhCqList_last12month = zhiBiaoSpfjyZhCqService.list(wrapper111);
        List<ZhiBiaoSpfjyZhCq> spfcj_mj_last12month = zhiBiaoSpfjyZhCqList_last12month.stream().filter(x -> "本月面积".equals(x.getZbname())).collect(Collectors.toList());
        Double spfcj_average_last12month = spfcj_mj_last12month.stream().mapToDouble(ZhiBiaoSpfjyZhCq::getSpfjyZbzZzTyCq).average().getAsDouble();
        BigDecimal pzks_qhzq_thisMonth = new BigDecimal((float) (pzks_mianJi_thisMonth.getSpfPzksZbzTmAll() / spfcj_average_last12month)).setScale(1, BigDecimal.ROUND_HALF_UP);
        dataFinal.put("pzks_qhzq_thisMonth", pzks_qhzq_thisMonth);

        //上月去化周期
        QueryWrapper<ZhiBiaoSpfjyZhCq> wrapper1111 = new QueryWrapper<>();
        LambdaQueryWrapper<ZhiBiaoSpfjyZhCq> lambda1111 = wrapper1111.lambda();
        LocalDate lastMonth_lastyear = thisMonthlastDay_lastyear.minusMonths(1);
        Date lastMonth_lastyearDate = DateUtil.localDate2Date(lastMonth_lastyear);
        String lastMonth_yyyyMM_lastyear = DateUtil.format(lastMonth_lastyearDate, "yyyyMM");
        lambda1111.ge(ZhiBiaoSpfjyZhCq::getTjsj, lastMonth_yyyyMM_lastyear);
        lambda1111.le(ZhiBiaoSpfjyZhCq::getTjsj, lastMonth_yyyyMM_thisyear);
        List<ZhiBiaoSpfjyZhCq> zhiBiaoSpfjyZhCqList_lastMonth_last12month = zhiBiaoSpfjyZhCqService.list(lambda1111);
        List<ZhiBiaoSpfjyZhCq> spfcj_mj_lastMonth_last12month = zhiBiaoSpfjyZhCqList_lastMonth_last12month.stream().filter(x -> "本月面积".equals(x.getZbname())).collect(Collectors.toList());
        Double spfcj_average_lastMonth_last12month = spfcj_mj_lastMonth_last12month.stream().mapToDouble(ZhiBiaoSpfjyZhCq::getSpfjyZbzZzTyCq).average().getAsDouble();
        BigDecimal pzks_qhzq_lastMonth = new BigDecimal((float) (pzks_mianJi_lastMonth.getSpfPzksZbzTmAll() / spfcj_average_lastMonth_last12month)).setScale(1, BigDecimal.ROUND_HALF_UP);
        dataFinal.put("pzks_qhzq_lastMonth_subtractthisMonth", pzks_qhzq_lastMonth.subtract(pzks_qhzq_thisMonth));


/////////////////////////////////////
/////////////////////商品房中非住宅////////////////////////////////
        //综合今年数据
        //成交套数
        Long spfcj_taoShu_thisMonth_fzz = taoShuThisMonth.getSpfjyZbzTyCq().longValue() - taoShuThisMonth.getSpfjyZbzZzTyCq().longValue();
        dataFinal.put("spfcj_taoShu_thisMonth_fzz", spfcj_taoShu_thisMonth_fzz);
        //成交面积
        Double spfcj_mianJi_thisMonth_fzz = new BigDecimal(mianJiThisMonth.getSpfjyZbzTyCq() - mianJiThisMonth.getSpfjyZbzZzTyCq()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        dataFinal.put("spfcj_mianJi_thisMonth_fzz", spfcj_mianJi_thisMonth_fzz);
        //成交金额
        Double spfcj_jinE_thisMonth_fzz = new BigDecimal(jinE_thisMonth.getSpfjyZbzTyCq() - jinE_thisMonth.getSpfjyZbzZzTyCq()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        dataFinal.put("spfcj_jinE_thisMonth_fzz", spfcj_jinE_thisMonth_fzz);

        ZhiBiaoSpfjyZhCq spfcj_taoShu_thisMonth_lastyear = zhiBiaoSpfjyZhCqList_lastyear.stream().filter(x -> thisMonth_yyyyMM_lastyear.equals(x.getTjsj()) && "本月套数".equals(x.getZbname())).findFirst().get();
        ZhiBiaoSpfjyZhCq spfcj_mianJi_thisMonth_lastyear = zhiBiaoSpfjyZhCqList_lastyear.stream().filter(x -> thisMonth_yyyyMM_lastyear.equals(x.getTjsj()) && "本月面积".equals(x.getZbname())).findFirst().get();
        ZhiBiaoSpfjyZhCq spfcj_jinE_thisMonth_lastyear = zhiBiaoSpfjyZhCqList_lastyear.stream().filter(x -> thisMonth_yyyyMM_lastyear.equals(x.getTjsj()) && "本月金额".equals(x.getZbname())).findFirst().get();
        ZhiBiaoSpfjyZhCq spfcj_jj_thisMonth_lastyear = zhiBiaoSpfjyZhCqList_lastyear.stream().filter(x -> thisMonth_yyyyMM_lastyear.equals(x.getTjsj()) && "本月均价".equals(x.getZbname())).findFirst().get();

        //成交套数同比
        Long spfcj_taoShu_thisMonth_lastyear_fzz = spfcj_taoShu_thisMonth_lastyear.getSpfjyZbzTyCq().longValue() - spfcj_taoShu_thisMonth_lastyear.getSpfjyZbzZzTyCq().longValue();
        BigDecimal spfcj_tb_taoShu_thisMonth_lastyear_fzz = new BigDecimal((float) ((spfcj_taoShu_thisMonth_fzz.floatValue() - spfcj_taoShu_thisMonth_lastyear_fzz.floatValue()) / spfcj_taoShu_thisMonth_lastyear_fzz.floatValue()));
        dataFinal.put("spfcj_tb_taoShu_thisMonth_fzz", spfcj_tb_taoShu_thisMonth_lastyear_fzz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //成交面积同比
        Double spfcj_mianJi_thisMonth_lastyear_fzz = spfcj_mianJi_thisMonth_lastyear.getSpfjyZbzTyCq() - spfcj_mianJi_thisMonth_lastyear.getSpfjyZbzZzTyCq();
        BigDecimal spfcj_tb_mianJi_thisMonth_lastyear_fzz = new BigDecimal((float) ((spfcj_mianJi_thisMonth_fzz - spfcj_mianJi_thisMonth_lastyear_fzz) / spfcj_mianJi_thisMonth_lastyear_fzz));
        dataFinal.put("spfcj_tb_mianJi_thisMonth_fzz", spfcj_tb_mianJi_thisMonth_lastyear_fzz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //成交金额同比
        Double spfcj_jinE_thisMonth_lastyear_fzz = spfcj_jinE_thisMonth_lastyear.getSpfjyZbzTyCq() - spfcj_jinE_thisMonth_lastyear.getSpfjyZbzZzTyCq();
        BigDecimal spfcj_tb_jinE_thisMonth_lastyear_fzz = new BigDecimal((float) ((spfcj_jinE_thisMonth_fzz - spfcj_jinE_thisMonth_lastyear_fzz) / spfcj_jinE_thisMonth_lastyear_fzz));
        dataFinal.put("spfcj_tb_mianJi_thisMonth_fzz", spfcj_tb_jinE_thisMonth_lastyear_fzz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");


        //成交套数上月
        Long spfcj_taoShu_lastMonth_fzz = taoShu_lastMonth.getSpfjyZbzTyCq().longValue() - taoShu_lastMonth.getSpfjyZbzZzTyCq().longValue();
        //成交面积上月
        Double spfcj_mianJi_lastMonth_fzz = mianJi_lastMonth.getSpfjyZbzTyCq() - mianJi_lastMonth.getSpfjyZbzZzTyCq();
        //成交金额上月
        Double spfcj_jinE_lastMonth_fzz = jinE_lastMonth.getSpfjyZbzTyCq() - jinE_lastMonth.getSpfjyZbzZzTyCq();
        //成交套数环比
        BigDecimal spfcj_hb_taoShu_thisMonth_fzz = new BigDecimal((float) ((spfcj_taoShu_thisMonth_fzz.floatValue() - spfcj_taoShu_lastMonth_fzz.floatValue()) / spfcj_taoShu_lastMonth_fzz.floatValue()));
        dataFinal.put("spfcj_hb_taoShu_thisMonth_fzz", spfcj_hb_taoShu_thisMonth_fzz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //成交套数环比
        BigDecimal spfcj_hb_mianJi_thisMonth_fzz = new BigDecimal((float) ((spfcj_mianJi_thisMonth_fzz - spfcj_mianJi_lastMonth_fzz) / spfcj_mianJi_lastMonth_fzz));
        dataFinal.put("spfcj_hb_mianJi_thisMonth_fzz", spfcj_hb_mianJi_thisMonth_fzz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //成交金额环比
        BigDecimal spfcj_hb_jinE_thisMonth_fzz = new BigDecimal((float) ((spfcj_jinE_thisMonth_fzz - spfcj_jinE_lastMonth_fzz) / spfcj_jinE_lastMonth_fzz));
        dataFinal.put("spfcj_hb_jinE_thisMonth_fzz", spfcj_hb_jinE_thisMonth_fzz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");

        //全年套数 ——今年
        Long spfcj_sum_taoShu_everyMonth_fzz = spfcj_sum_taoShu_everyMonth_obj.getSpfjyZbzTyCq().longValue() - spfcj_sum_taoShu_everyMonth_obj.getSpfjyZbzZzTyCq().longValue();
        dataFinal.put("spfcj_sum_taoShu_everyMonth_fzz", spfcj_sum_taoShu_everyMonth_fzz);
        //全年面积——今年
        Double spfcj_sum_mianJi_everyMonth_fzz = new BigDecimal(spfcj_sum_mianJi_everyMonth_obj.getSpfjyZbzTyCq() - spfcj_sum_mianJi_everyMonth_obj.getSpfjyZbzZzTyCq()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        dataFinal.put("spfcj_sum_mianJi_everyMonth_fzz", spfcj_sum_mianJi_everyMonth_fzz);
        //全年金额——今年
        Double spfcj_sum_jinE_everyMonth_fzz = new BigDecimal(spfcj_sum_jinE_everyMonth_obj.getSpfjyZbzTyCq() - spfcj_sum_jinE_everyMonth_obj.getSpfjyZbzZzTyCq()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        dataFinal.put("spfcj_sum_jinE_everyMonth_fzz", spfcj_sum_jinE_everyMonth_fzz);

        //全年套数 ——去年
        Long spfcj_sum_taoShu_everyMonth_lastyear_fzz = spfcj_sum_taoShu_everyMonth_lastyear.getSpfjyZbzTyCq().longValue() - spfcj_sum_taoShu_everyMonth_lastyear.getSpfjyZbzZzTyCq().longValue();
        dataFinal.put("spfcj_sum_taoShu_everyMonth_lastyear_fzz", spfcj_sum_taoShu_everyMonth_lastyear_fzz);
        //全年面积——去年
        Double spfcj_sum_mianJi_everyMonth_lastyear_fzz = new BigDecimal(spfcj_sum_mianJi_everyMonth_lastyear.getSpfjyZbzTyCq() - spfcj_sum_mianJi_everyMonth_lastyear.getSpfjyZbzZzTyCq()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        dataFinal.put("spfcj_sum_mianJi_everyMonth_fzz", spfcj_sum_mianJi_everyMonth_lastyear_fzz);
        //全年金额——去年
        Double spfcj_sum_jinE_everyMonth_lastyear_fzz = new BigDecimal(spfcj_sum_jinE_everyMonth_lastyear.getSpfjyZbzTyCq() - spfcj_sum_jinE_everyMonth_lastyear.getSpfjyZbzZzTyCq()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        dataFinal.put("spfcj_sum_jinE_everyMonth_lastyear_fzz", spfcj_sum_jinE_everyMonth_lastyear_fzz);

        /////////////////////去年数据
        //成交套数同比-全年度
        BigDecimal spfcj_tb_taoShu_thisyear_fzz = new BigDecimal((float) ((spfcj_sum_taoShu_everyMonth_fzz.floatValue() - spfcj_sum_taoShu_everyMonth_lastyear_fzz.floatValue()) / spfcj_sum_taoShu_everyMonth_lastyear_fzz.floatValue()));
        dataFinal.put("spfcj_tb_taoShu_thisyear_fzz", spfcj_tb_taoShu_thisyear_fzz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //成交套数同比-全年度
        BigDecimal spfcj_tb_mianJi_thisyear_fzz = new BigDecimal((float) ((spfcj_sum_mianJi_everyMonth_fzz - spfcj_sum_mianJi_everyMonth_lastyear_fzz) / spfcj_sum_mianJi_everyMonth_lastyear_fzz));
        dataFinal.put("spfcj_tb_mianJi_thisyear_fzz", spfcj_tb_mianJi_thisyear_fzz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //成交金额同比-全年度
        BigDecimal spfcj_tb_jinE_thisyear_fzz = new BigDecimal((float) ((spfcj_sum_jinE_everyMonth_fzz - spfcj_sum_jinE_everyMonth_lastyear_fzz) / spfcj_sum_jinE_everyMonth_lastyear_fzz));
        dataFinal.put("spfcj_tb_jinE_thisyear_fzz", spfcj_tb_jinE_thisyear_fzz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");


        //////////////////////市区///////////////////////////
        //综合今年数据
        //成交套数
        Long spfcj_taoShu_thisMonth_shiqu_fzz = taoShuThisMonth.getSpfjyZbzTySq().longValue() - taoShuThisMonth.getSpfjyZbzZzTySq().longValue();
        dataFinal.put("spfcj_taoShu_thisMonth_shiqu_fzz", spfcj_taoShu_thisMonth_shiqu_fzz);
        //成交面积
        Double spfcj_mianJi_thisMonth_shiqu_fzz = new BigDecimal(mianJiThisMonth.getSpfjyZbzTySq() - mianJiThisMonth.getSpfjyZbzZzTySq()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        dataFinal.put("spfcj_mianJi_thisMonth_shiqu_fzz", spfcj_mianJi_thisMonth_shiqu_fzz);
        //成交金额
        Double spfcj_jinE_thisMonth_shiqu_fzz = new BigDecimal(jinE_thisMonth.getSpfjyZbzTySq() - jinE_thisMonth.getSpfjyZbzZzTySq()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        dataFinal.put("spfcj_jinE_thisMonth_shiqu_fzz", spfcj_jinE_thisMonth_shiqu_fzz);


        //成交套数同比
        Long spfcj_taoShu_thisMonth_lastyear_shiqu_fzz = spfcj_taoShu_thisMonth_lastyear.getSpfjyZbzTySq().longValue() - spfcj_taoShu_thisMonth_lastyear.getSpfjyZbzZzTySq().longValue();
        BigDecimal spfcj_tb_taoShu_thisMonth_lastyear_shiqu_fzz = new BigDecimal((float) ((spfcj_taoShu_thisMonth_shiqu_fzz.floatValue() - spfcj_taoShu_thisMonth_lastyear_shiqu_fzz.floatValue()) / spfcj_taoShu_thisMonth_lastyear_shiqu_fzz.floatValue()));
        dataFinal.put("spfcj_tb_taoShu_thisMonth_shiqu_fzz", spfcj_tb_taoShu_thisMonth_lastyear_shiqu_fzz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //成交面积同比
        Long spfcj_mianJi_thisMonth_lastyear_shiqu_fzz = spfcj_mianJi_thisMonth_lastyear.getSpfjyZbzTySq().longValue() - spfcj_mianJi_thisMonth_lastyear.getSpfjyZbzZzTySq().longValue();
        BigDecimal spfcj_tb_mianJi_thisMonth_lastyear_shiqu_fzz = new BigDecimal((float) ((spfcj_mianJi_thisMonth_shiqu_fzz - spfcj_mianJi_thisMonth_lastyear_shiqu_fzz) / spfcj_mianJi_thisMonth_lastyear_shiqu_fzz));
        dataFinal.put("spfcj_tb_mianJi_thisMonth_shiqu_fzz", spfcj_tb_mianJi_thisMonth_lastyear_shiqu_fzz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //成交金额同比
        Long spfcj_jinE_thisMonth_lastyear_shiqu_fzz = spfcj_jinE_thisMonth_lastyear.getSpfjyZbzTySq().longValue() - spfcj_jinE_thisMonth_lastyear.getSpfjyZbzZzTySq().longValue();
        BigDecimal spfcj_tb_jinE_thisMonth_lastyear_shiqu_fzz = new BigDecimal((float) ((spfcj_jinE_thisMonth_shiqu_fzz - spfcj_jinE_thisMonth_lastyear_shiqu_fzz) / spfcj_jinE_thisMonth_lastyear_shiqu_fzz));
        dataFinal.put("spfcj_tb_jinE_thisMonth_shiqu_fzz", spfcj_tb_jinE_thisMonth_lastyear_shiqu_fzz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");

        //成交套数上月
        Long spfcj_taoShu_lastMonth_shiqu_fzz = taoShu_lastMonth.getSpfjyZbzTySq().longValue() - taoShu_lastMonth.getSpfjyZbzZzTySq().longValue();
        //成交面积上月
        Double spfcj_mianJi_lastMonth_shiqu_fzz = new BigDecimal(mianJi_lastMonth.getSpfjyZbzTySq() - mianJi_lastMonth.getSpfjyZbzZzTySq()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        //成交金额上月
        Double spfcj_jinE_lastMonth_shiqu_fzz = new BigDecimal(jinE_lastMonth.getSpfjyZbzTySq() - jinE_lastMonth.getSpfjyZbzZzTySq()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        //成交套数环比
        BigDecimal spfcj_hb_taoShu_thisMonth_shiqu_fzz = new BigDecimal((float) ((spfcj_taoShu_thisMonth_shiqu_fzz.floatValue() - spfcj_taoShu_lastMonth_shiqu_fzz.floatValue()) / spfcj_taoShu_lastMonth_shiqu_fzz.floatValue()));
        dataFinal.put("spfcj_hb_taoShu_thisMonth_shiqu_fzz", spfcj_hb_taoShu_thisMonth_shiqu_fzz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //成交套数环比
        BigDecimal spfcj_hb_mianJi_thisMonth_shiqu_fzz = new BigDecimal((float) ((spfcj_mianJi_thisMonth_shiqu_fzz - spfcj_mianJi_lastMonth_shiqu_fzz) / spfcj_mianJi_lastMonth_shiqu_fzz));
        dataFinal.put("spfcj_hb_mianJi_thisMonth_shiqu_fzz", spfcj_hb_mianJi_thisMonth_shiqu_fzz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //成交金额环比
        BigDecimal spfcj_hb_jinE_thisMonth_shiqu_fzz = new BigDecimal((float) ((spfcj_jinE_thisMonth_shiqu_fzz - spfcj_jinE_lastMonth_shiqu_fzz) / spfcj_jinE_lastMonth_shiqu_fzz));
        dataFinal.put("spfcj_hb_jinE_thisMonth_shiqu_fzz", spfcj_hb_jinE_thisMonth_shiqu_fzz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");

        //全年套数 ——今年
        Long spfcj_sum_taoShu_everyMonth_shiqu_fzz = spfcj_sum_taoShu_everyMonth_obj.getSpfjyZbzTySq().longValue() - spfcj_sum_taoShu_everyMonth_obj.getSpfjyZbzZzTySq().longValue();
        dataFinal.put("spfcj_sum_taoShu_everyMonth_shiqu_fzz", spfcj_sum_taoShu_everyMonth_shiqu_fzz);
        //全年面积——今年
        Double spfcj_sum_mianJi_everyMonth_shiqu_fzz = new BigDecimal(spfcj_sum_mianJi_everyMonth_obj.getSpfjyZbzTySq() - spfcj_sum_mianJi_everyMonth_obj.getSpfjyZbzZzTySq()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        dataFinal.put("spfcj_sum_mianJi_everyMonth_shiqu_fzz", spfcj_sum_mianJi_everyMonth_shiqu_fzz);
        //全年金额——今年
        Double spfcj_sum_jinE_everyMonth_shiqu_fzz = new BigDecimal(spfcj_sum_jinE_everyMonth_obj.getSpfjyZbzTySq() - spfcj_sum_jinE_everyMonth_obj.getSpfjyZbzZzTySq()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        dataFinal.put("spfcj_sum_jinE_everyMonth_shiqu_fzz", spfcj_sum_jinE_everyMonth_shiqu_fzz);

        //全年套数 ——去年
        Long spfcj_sum_taoShu_everyMonth_lastyear_shiqu_fzz = spfcj_sum_taoShu_everyMonth_lastyear.getSpfjyZbzTySq().longValue() - spfcj_sum_taoShu_everyMonth_lastyear.getSpfjyZbzZzTySq().longValue();
        dataFinal.put("spfcj_sum_taoShu_everyMonth_lastyear_fzz", spfcj_sum_taoShu_everyMonth_lastyear_fzz);
        //全年面积——去年
        Double spfcj_sum_mianJi_everyMonth_lastyear_shiqu_fzz = new BigDecimal(spfcj_sum_mianJi_everyMonth_lastyear.getSpfjyZbzTySq() - spfcj_sum_mianJi_everyMonth_lastyear.getSpfjyZbzZzTySq()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        dataFinal.put("spfcj_sum_mianJi_everyMonth_lastyear_shiqu_fzz", spfcj_sum_mianJi_everyMonth_lastyear_shiqu_fzz);
        //全年金额——去年
        Double spfcj_sum_jinE_everyMonth_lastyear_shiqu_fzz = new BigDecimal(spfcj_sum_jinE_everyMonth_lastyear.getSpfjyZbzTySq() - spfcj_sum_jinE_everyMonth_lastyear.getSpfjyZbzZzTySq()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        dataFinal.put("spfcj_sum_jinE_everyMonth_lastyear_shiqu_fzz", spfcj_sum_jinE_everyMonth_lastyear_shiqu_fzz);

        /////////////////////去年数据
        //成交套数同比-全年度
        BigDecimal spfcj_tb_taoShu_thisyear_shiqu_fzz = new BigDecimal((float) ((spfcj_sum_taoShu_everyMonth_shiqu_fzz.floatValue() - spfcj_sum_taoShu_everyMonth_lastyear_shiqu_fzz.floatValue()) / spfcj_sum_taoShu_everyMonth_lastyear_shiqu_fzz.floatValue()));
        dataFinal.put("spfcj_tb_taoShu_thisyear_shiqu_fzz", spfcj_tb_taoShu_thisyear_shiqu_fzz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //成交套数同比-全年度
        BigDecimal spfcj_tb_mianJi_thisyear_shiqu_fzz = new BigDecimal((float) ((spfcj_sum_mianJi_everyMonth_shiqu_fzz - spfcj_sum_mianJi_everyMonth_lastyear_shiqu_fzz) / spfcj_sum_mianJi_everyMonth_lastyear_shiqu_fzz));
        dataFinal.put("spfcj_tb_mianJi_thisyear_shiqu_fzz", spfcj_tb_mianJi_thisyear_shiqu_fzz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //成交金额同比-全年度
        BigDecimal spfcj_tb_jinE_thisyear_shiqu_fzz = new BigDecimal((float) ((spfcj_sum_jinE_everyMonth_shiqu_fzz - spfcj_sum_jinE_everyMonth_lastyear_shiqu_fzz) / spfcj_sum_jinE_everyMonth_lastyear_shiqu_fzz));
        dataFinal.put("spfcj_tb_jinE_thisyear_shiqu_fzz", spfcj_tb_jinE_thisyear_shiqu_fzz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");

        //////////////////////均价-非住宅/////////////////////////////
        //成交均价-全市
        Double spfcj_jj_thisMonth_fzz = new BigDecimal(spfcj_jinE_thisMonth_fzz / spfcj_mianJi_thisMonth_fzz).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        dataFinal.put("spfcj_jj_thisMonth_fzz", spfcj_jj_thisMonth_fzz);
        //成交均价同比-全市
        Double spfcj_jj_thisMonth_lastyear_fzz = new BigDecimal(spfcj_jinE_thisMonth_lastyear_fzz / spfcj_mianJi_thisMonth_lastyear_fzz).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        BigDecimal spfcj_tb_jj_thisMonth_lastyear_fzz = new BigDecimal((float) ((spfcj_jj_thisMonth_fzz - spfcj_jj_thisMonth_lastyear_fzz) / spfcj_jj_thisMonth_lastyear_fzz));
        dataFinal.put("spfcj_tb_jj_thisMonth_fzz", spfcj_tb_jj_thisMonth_lastyear_fzz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");

        //成交金额环比-全市
        Double spfcj_jj_lastMonth_fzz = new BigDecimal(spfcj_jinE_lastMonth_fzz / spfcj_mianJi_lastMonth_fzz).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        BigDecimal spfcj_hb_jj_thisMonth_fzz = new BigDecimal(((spfcj_jj_thisMonth_fzz - spfcj_jj_lastMonth_fzz) / spfcj_jj_lastMonth_fzz));
        dataFinal.put("spfcj_hb_jj_thisMonth_fzz", spfcj_hb_jj_thisMonth_fzz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");

        //成交均价-市区
        Double spfcj_jj_thisMonth_shiqu_fzz = new BigDecimal(spfcj_jinE_thisMonth_shiqu_fzz / spfcj_mianJi_thisMonth_shiqu_fzz).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        dataFinal.put("spfcj_jj_thisMonth_shiqu_fzz", spfcj_jj_thisMonth_shiqu_fzz);
        //成交均价比-市区
        Double spfcj_jj_thisMonth_lastyear_shiqu_fzz = new BigDecimal(spfcj_jinE_thisMonth_lastyear_shiqu_fzz / spfcj_mianJi_thisMonth_lastyear_shiqu_fzz).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        BigDecimal spfcj_tb_jj_thisMonth_lastyear_shiqu_fzz = new BigDecimal((float) ((spfcj_jj_thisMonth_shiqu_fzz - spfcj_jj_thisMonth_lastyear_shiqu_fzz) / spfcj_jj_thisMonth_lastyear_shiqu_fzz));
        dataFinal.put("spfcj_tb_jj_thisMonth_shiqu_fzz", spfcj_tb_jj_thisMonth_lastyear_shiqu_fzz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");

        //成交金额环比-市区
        Double spfcj_jj_lastMonth_shiqu_fzz = new BigDecimal(spfcj_jinE_lastMonth_shiqu_fzz / spfcj_mianJi_lastMonth_shiqu_fzz).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        BigDecimal spfcj_hb_jj_thisMonth_shiqu_fzz = new BigDecimal((float) ((spfcj_jj_thisMonth_shiqu_fzz - spfcj_jj_lastMonth_shiqu_fzz) / spfcj_jj_lastMonth_shiqu_fzz));
        dataFinal.put("spfcj_hb_jj_thisMonth_shiqu_fzz", spfcj_hb_jj_thisMonth_shiqu_fzz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");

        //全年均价——今年
        Double spfcj_sum_jj_everyMonth_fzz = new BigDecimal(spfcj_sum_jinE_everyMonth_fzz / spfcj_sum_mianJi_everyMonth_fzz).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        dataFinal.put("spfcj_sum_jj_everyMonth_fzz", spfcj_sum_jj_everyMonth_fzz);
        Double spfcj_sum_jj_everyMonth_lastyear_fzz = new BigDecimal(spfcj_sum_jinE_everyMonth_lastyear_fzz / spfcj_sum_mianJi_everyMonth_lastyear_fzz).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        //成交均价同比-全年度
        BigDecimal spfcj_hb_jj_thisyear_fzz = new BigDecimal((float) ((spfcj_sum_jj_everyMonth_fzz - spfcj_sum_jj_everyMonth_lastyear_fzz) / spfcj_sum_jj_everyMonth_lastyear_fzz));
        dataFinal.put("spfcj_tb_jj_thisyear_fzz", spfcj_hb_jj_thisyear_fzz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");

        //全年均价——今年-市区
        Double spfcj_sum_jj_everyMonth_shiqu_fzz = new BigDecimal(spfcj_sum_jinE_everyMonth_shiqu_fzz / spfcj_sum_mianJi_everyMonth_shiqu_fzz).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        dataFinal.put("spfcj_sum_jj_everyMonth_shiqu_fzz", spfcj_sum_jj_everyMonth_shiqu_fzz);
        Double spfcj_sum_jj_everyMonth_shiqu_lastyear_fzz = new BigDecimal(spfcj_sum_jinE_everyMonth_lastyear_shiqu_fzz / spfcj_sum_mianJi_everyMonth_lastyear_shiqu_fzz).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        //成交均价同比-全年度-市区
        BigDecimal spfcj_tb_jj_thisyear_shiqu_fzz = new BigDecimal((float) ((spfcj_sum_jj_everyMonth_shiqu_fzz - spfcj_sum_jj_everyMonth_shiqu_lastyear_fzz) / spfcj_sum_jj_everyMonth_shiqu_lastyear_fzz));
        dataFinal.put("spfcj_tb_jj_thisyear_shiqu_fzz", spfcj_tb_jj_thisyear_shiqu_fzz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");

/////////批准可售-住宅

        //可售套数
        ZhiBiaoSpfPzks pzks_taoShu_thisMonth_fzz = zhiBiaoSpfPzksCqList.stream().filter(x -> thisMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "批准可售套数-非住宅".equals(x.getZbname())).findFirst().get();
        dataFinal.put("pzks_taoShu_thisMonth_fzz", pzks_taoShu_thisMonth_fzz.getSpfPzksZbzTmAll().longValue());
        //可售面积
        ZhiBiaoSpfPzks pzks_mianJi_thisMonth_fzz = zhiBiaoSpfPzksCqList.stream().filter(x -> thisMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "批准可售面积-非住宅".equals(x.getZbname())).findFirst().get();
        dataFinal.put("pzks_mianJi_thisMonth_fzz", pzks_mianJi_thisMonth_fzz.getSpfPzksZbzTmAll());

        ZhiBiaoSpfPzks pzks_taoShu_lastMonth_fzz = zhiBiaoSpfPzksCqList.stream().filter(x -> lastMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "批准可售套数-非住宅".equals(x.getZbname())).findFirst().get();
        ZhiBiaoSpfPzks pzks_mianJi_lastMonth_fzz = zhiBiaoSpfPzksCqList.stream().filter(x -> lastMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "批准可售面积-非住宅".equals(x.getZbname())).findFirst().get();
        //可售套数环比
        BigDecimal pzks_hb_taoShu_thisMonth_fzz = new BigDecimal((float) ((pzks_taoShu_thisMonth_fzz.getSpfPzksZbzTmAll() - pzks_taoShu_lastMonth_fzz.getSpfPzksZbzTmAll()) / pzks_taoShu_lastMonth_fzz.getSpfPzksZbzTmAll()));
        dataFinal.put("pzks_hb_taoShu_thisMonth_fzz", pzks_hb_taoShu_thisMonth_fzz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //可售套数环比
        BigDecimal pzks_hb_mianJi_thisMonth_fzz = new BigDecimal((float) ((pzks_mianJi_thisMonth_fzz.getSpfPzksZbzTmAll() - pzks_mianJi_lastMonth_fzz.getSpfPzksZbzTmAll()) / pzks_mianJi_lastMonth_fzz.getSpfPzksZbzTmAll()));
        dataFinal.put("pzks_hb_mianJi_thisMonth_fzz", pzks_hb_mianJi_thisMonth_fzz.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");

        //当月去化周期
        Double spfcj_average_last12month_all = spfcj_mj_last12month.stream().mapToDouble(ZhiBiaoSpfjyZhCq::getSpfjyZbzTyCq).average().getAsDouble();
        Double spfcj_average_last12month_fzz = spfcj_average_last12month_all - spfcj_average_last12month;
        BigDecimal pzks_qhzq_thisMonth_fzz = new BigDecimal((float) (pzks_mianJi_thisMonth_fzz.getSpfPzksZbzTmAll() / spfcj_average_last12month_fzz)).setScale(1, BigDecimal.ROUND_HALF_UP);
        dataFinal.put("pzks_qhzq_thisMonth_fzz", pzks_qhzq_thisMonth_fzz);

        //上月去化周期
        Double spfcj_average_lastMonth_last12month_all = spfcj_mj_lastMonth_last12month.stream().mapToDouble(ZhiBiaoSpfjyZhCq::getSpfjyZbzTyCq).average().getAsDouble();
        Double spfcj_average_lastMonth_last12month_fzz = spfcj_average_lastMonth_last12month_all - spfcj_average_lastMonth_last12month;

        BigDecimal pzks_qhzq_lastMonth_fzz = new BigDecimal((float) (pzks_mianJi_lastMonth_fzz.getSpfPzksZbzTmAll() / spfcj_average_lastMonth_last12month_fzz)).setScale(1, BigDecimal.ROUND_HALF_UP);
        dataFinal.put("pzks_qhzq_lastMonth_subtractthisMonth_fzz", pzks_qhzq_lastMonth_fzz.subtract(pzks_qhzq_thisMonth_fzz));

///////////////////////二手房////////////////////////
        //综合今年数据

        QueryWrapper<ZhiBiaoEsfJy> wrapper4 = new QueryWrapper<>();
        LambdaQueryWrapper<ZhiBiaoEsfJy> lambda4 = wrapper4.lambda();
        lambda4.ge(ZhiBiaoEsfJy::getTjsj, firstMonth_yyyyMM_thisyear);
        lambda4.le(ZhiBiaoEsfJy::getTjsj, thisMonth_yyyyMM_thisyear);
        List<ZhiBiaoEsfJy> zhiBiaoEsfJyList = zhiBiaoEsfJyService.list(wrapper4);

        //成交套数
        ZhiBiaoEsfJy esf_taoShu_thisMonth = zhiBiaoEsfJyList.stream().filter(x -> thisMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "本月套数".equals(x.getZbname())).findFirst().get();
        dataFinal.put("esf_taoShu_thisMonth", esf_taoShu_thisMonth.getEsfjyZbzZzCntTm().longValue());
        //成交面积
        ZhiBiaoEsfJy esf_mianJi_thisMonth = zhiBiaoEsfJyList.stream().filter(x -> thisMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "本月面积".equals(x.getZbname())).findFirst().get();
        dataFinal.put("esf_mianJi_thisMonth", esf_mianJi_thisMonth.getEsfjyZbzZzCntTm());
        //成交金额
        ZhiBiaoEsfJy esf_jinE_thisMonth = zhiBiaoEsfJyList.stream().filter(x -> thisMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "本月金额".equals(x.getZbname())).findFirst().get();
        dataFinal.put("esf_jinE_thisMonth", esf_jinE_thisMonth.getEsfjyZbzZzCntTm());
        //成交套数同比
        dataFinal.put("esf_tb_taoShu_thisMonth", esf_taoShu_thisMonth.getEsfjyZbzZzCntTyTb() + "%");
        //成交套数同比
        dataFinal.put("esf_tb_mianJi_thisMonth", esf_mianJi_thisMonth.getEsfjyZbzZzCntTyTb() + "%");
        //成交金额同比
        dataFinal.put("esf_tb_jinE_thisMonth", esf_jinE_thisMonth.getEsfjyZbzZzCntTyTb() + "%");

        ZhiBiaoEsfJy esf_taoShu_lastMonth = zhiBiaoEsfJyList.stream().filter(x -> lastMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "本月套数".equals(x.getZbname())).findFirst().get();
        ZhiBiaoEsfJy esf_mianJi_lastMonth = zhiBiaoEsfJyList.stream().filter(x -> lastMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "本月面积".equals(x.getZbname())).findFirst().get();
        ZhiBiaoEsfJy esf_jinE_lastMonth = zhiBiaoEsfJyList.stream().filter(x -> lastMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "本月金额".equals(x.getZbname())).findFirst().get();
        //成交套数环比
        BigDecimal esf_hb_taoShu_thisMonth = new BigDecimal((float) ((esf_taoShu_thisMonth.getEsfjyZbzZzCntTm() - esf_taoShu_lastMonth.getEsfjyZbzZzCntTm()) / esf_taoShu_lastMonth.getEsfjyZbzZzCntTm()));
        dataFinal.put("esf_hb_taoShu_thisMonth", esf_hb_taoShu_thisMonth.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //成交套数环比
        BigDecimal esf_hb_mianJi_thisMonth = new BigDecimal((float) ((esf_mianJi_thisMonth.getEsfjyZbzZzCntTm() - esf_mianJi_lastMonth.getEsfjyZbzZzCntTm()) / esf_mianJi_lastMonth.getEsfjyZbzZzCntTm()));
        dataFinal.put("esf_hb_mianJi_thisMonth", esf_hb_mianJi_thisMonth.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //成交金额环比
        BigDecimal esf_hb_jinE_thisMonth = new BigDecimal((float) ((esf_jinE_thisMonth.getEsfjyZbzZzCntTm() - esf_jinE_lastMonth.getEsfjyZbzZzCntTm()) / esf_jinE_lastMonth.getEsfjyZbzZzCntTm()));
        dataFinal.put("esf_hb_jinE_thisMonth", esf_hb_jinE_thisMonth.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");


        //////////////////////均价-全二手房/////////////////////////////
        //成交均价-全市
        ZhiBiaoEsfJy esf_jj_thisMonth = zhiBiaoEsfJyList.stream().filter(x -> thisMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "本月均价".equals(x.getZbname())).findFirst().get();
        dataFinal.put("esf_jj_thisMonth", esf_jj_thisMonth.getEsfjyZbzZzCntTm().longValue());
        //成交均价同比-全市
        dataFinal.put("esf_tb_jj_thisMonth", esf_jj_thisMonth.getEsfjyZbzZzCntTyTb() + "%");
        ZhiBiaoEsfJy esf_jj_lastMonth = zhiBiaoEsfJyList.stream().filter(x -> lastMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "本月均价".equals(x.getZbname())).findFirst().get();
        //成交金额环比-全市
        BigDecimal esf_hb_jj_thisMonth = new BigDecimal((float) ((esf_jj_thisMonth.getEsfjyZbzZzCntTm() - esf_jj_lastMonth.getEsfjyZbzZzCntTm()) / esf_jj_lastMonth.getEsfjyZbzZzCntTm()));
        dataFinal.put("esf_hb_jj_thisMonth", esf_hb_jj_thisMonth.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");

       /* //成交均价-市区
        ZhiBiaoSpfjyZhCq jj_thisMonth_shiqu = zhiBiaoSpfjyZhCqList.stream().filter(x -> thisMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "本月均价".equals(x.getZbname())).findFirst().get();
        dataFinal.put("spfcj_jj_thisMonth_shiqu", jj_thisMonth_shiqu.getSpfjyZbzTySq().longValue());
        //成交均价同比-市区
        dataFinal.put("spfcj_tb_jj_thisMonth_shiqu", jj_thisMonth_shiqu.getSpfjyZbzTySqTb() + "%");
        ZhiBiaoSpfjyZhCq jj_lastMonth_shiqu = zhiBiaoSpfjyZhCqList.stream().filter(x -> lastMonth_yyyyMM_thisyear.equals(x.getTjsj()) && "本月均价".equals(x.getZbname())).findFirst().get();
        //成交金额环比-市区
        BigDecimal spfcj_hb_jj_thisMonth_shiqu = new BigDecimal((float) ((jj_thisMonth_shiqu.getSpfjyZbzTySq() - jj_lastMonth_shiqu.getSpfjyZbzTySq()) / jj_lastMonth_shiqu.getSpfjyZbzTySq()));
        dataFinal.put("spfcj_hb_jj_thisMonth_shiqu", spfcj_hb_jj_thisMonth_shiqu.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
 */

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
            Double yhbm_thisMonth_lylv = new BigDecimal((float) yhbm_thisMonth_lysize / yhbm_thisMonth_djsize).doubleValue();
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
        dataFinal.put("yhbm_thisMonth_lylv", new BigDecimal(yhbm_everyMonth_lylv_Map.get(month)).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //这月平均中签率
        dataFinal.put("yhbm_thisMonth_pjzql", new BigDecimal(yhbm_everyMonth_pjzql_Map.get(month)).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        //上月平均中签率
        Double yhbm_lastMonth_pjzql = Double.valueOf(yhbm_everyMonth_pjzql_Map.get(lastmonth));
        //中签率环比
        Double yhbm_thisMonth_pjzql = Double.valueOf(yhbm_everyMonth_pjzql_Map.get(month));
        BigDecimal yhbm_thisMonth_zqlhb = new BigDecimal((float) ((yhbm_thisMonth_pjzql - yhbm_lastMonth_pjzql) / yhbm_lastMonth_pjzql)).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP);

        if (yhbm_thisMonth_zqlhb.compareTo(BigDecimal.ZERO) > 0) {
            dataFinal.put("yhbm_thisMonth_zqlhb_true", true);
        } else {
            dataFinal.put("yhbm_thisMonth_zqlhb_false", true);
        }
        dataFinal.put("yhbm_thisMonth_zqlhb", yhbm_thisMonth_zqlhb.abs() + "%");
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
            String yhbm_thisCq_lylv = new BigDecimal((float) yhbm_thisCq_lysize / yhbm_thisCq_djsize).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%";
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
