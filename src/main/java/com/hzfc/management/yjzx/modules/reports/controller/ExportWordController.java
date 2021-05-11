package com.hzfc.management.yjzx.modules.reports.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.deepoove.poi.data.*;
import com.hzfc.management.yjzx.common.api.CommonResult;
import com.hzfc.management.yjzx.modules.reports.dto.ExportDataPackage;
import com.hzfc.management.yjzx.modules.reports.dto.ExportParam;
import com.hzfc.management.yjzx.modules.reports.model.ReportsWordTemplate;
import com.hzfc.management.yjzx.modules.reports.model.ZhiBiaoZzxsjgbdqk;
import com.hzfc.management.yjzx.modules.reports.service.ReportsWordTemplateService;
import com.hzfc.management.yjzx.modules.reports.service.ZhiBiaoZzxsjgbdqkService;
import com.hzfc.management.yjzx.utils.dateUtils.DateUtil;
import com.hzfc.management.yjzx.utils.fileutils.Base64FileUtil;
import com.hzfc.management.yjzx.utils.fileutils.DeleteFileUtil;
import com.hzfc.management.yjzx.utils.fileutils.SaveFileUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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


    /////////////////////前端做成网格////////////////////////////
    @Value("${hzfc.temporaryfile.word.path}")
    private String temporaryPath;

    @Value("${hzfc.uploadfile.wordTemplate.path}")
    private String templatePath;

    @Value("${hzfc.zhibiao.city}")
    private String city;

    @Autowired
    private ReportsWordTemplateService reportsWordTemplateService;

    @Autowired
    private ZhiBiaoZzxsjgbdqkService zhiBiaoZzxsjgbdqkService;

    @RequestMapping(value = "/exportUserWord/{templateId}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<String> exportUserWord(@PathVariable("templateId") Long templateId, @RequestBody ExportParam exportParam) {

        Map<String, Object> dataFinal = new HashMap<String, Object>();
        List<Date> dates = exportParam.getDates();
        LocalDate last = LocalDate.now();
        if (!CollectionUtils.isEmpty(dates) && dates.size() == 2) {
            List<String> dateList = dates.stream().map(date -> DateUtil.format(date, "yyyy-MM-dd")).collect(Collectors.toList());
            dataFinal.put("firstDate", dateList.get(0));
            dataFinal.put("secondDate", dateList.get(1));
            last = DateUtil.toLocalDate(dates.get(0));
        }
        String year = last.format(DateTimeFormatter.ofPattern("yyyy"));
        String lastMM = last.format(DateTimeFormatter.ofPattern("MM"));
        dataFinal.put("lastMonth", lastMM);
        dataFinal.put("year", year);
        Map<String, Object> data1 = new HashMap<String, Object>();

        // 数据库查询城市指标数据data1 <db,data>
        QueryWrapper<ZhiBiaoZzxsjgbdqk> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<ZhiBiaoZzxsjgbdqk> lambda = wrapper.lambda();
        lambda.in(ZhiBiaoZzxsjgbdqk::getCity, "杭　　州");
        lambda.eq(ZhiBiaoZzxsjgbdqk::getLb, "1");
        LocalDate thisMonthFirstday = LocalDate.of(last.getYear(), last.getMonth(), 1);
        Date thisMonthFirstDate = DateUtil.localDate2Date(thisMonthFirstday);
        lambda.eq(ZhiBiaoZzxsjgbdqk::getTitle, thisMonthFirstDate);
        Map<String, Object> datas_Zzxsjgbdqkdata_hz = zhiBiaoZzxsjgbdqkService.getMap(wrapper);
        if (!CollectionUtils.isEmpty(datas_Zzxsjgbdqkdata_hz)) {
            datas_Zzxsjgbdqkdata_hz.forEach((k, v) -> {
                Optional.ofNullable(v).map(u -> data1.put("ODS_PY_ZZXSJGBDQK_MM_" + k, datas_Zzxsjgbdqkdata_hz.get(k)));
            });
        }
        // 数据库查询word模板指标参数zhibiaoMap <db,word>
        ReportsWordTemplate reportsWordTemplate = reportsWordTemplateService.getById(templateId);
        String zhibiaos = reportsWordTemplate.getZhibiaos();
        HashMap<String, String> zhibiaoMap = Optional.ofNullable(zhibiaos).map(u -> JSONObject.parseObject(u, HashMap.class)).get();

        zhibiaoMap.forEach((k, v) -> {
            Optional.ofNullable(v).map(u -> data1.put(v, data1.get(k)));
        });
        dataFinal.putAll(data1);

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
        List<ZhiBiaoZzxsjgbdqk> list = zhiBiaoZzxsjgbdqkService.list(wrapper2);
        //todo 指标转换
        //上月各city集合
        List<ZhiBiaoZzxsjgbdqk> collect = list.stream().filter(x -> DateUtil.date2LocalDate(x.getTitle())
                .equals(thisMonthFirstday)).collect(Collectors.toList());
        // <city,各月list>
        Map<String, List<ZhiBiaoZzxsjgbdqk>> collect1 = list.stream().collect(Collectors.groupingBy(ZhiBiaoZzxsjgbdqk::getCity));

        ExportDataPackage exportDataPackage = new ExportDataPackage();
        exportDataPackage.setZhiBiaoZzxsjgbdqkList(list);


        //渲染表格
        this.dealTabletest(dataFinal, exportDataPackage);
        //渲染图表
        this.dealChart(dataFinal);

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

    private Map<String, Object> dealTabletest(Map<String, Object> paramMap, ExportDataPackage exportDataPackage) {


        RowRenderData row01 = Rows.of("城市", "环比", "同比", "定基")
                .center().horizontalCenter().textFontSize(8).textColor("FFFFFF").bgColor("4472C4").create();

        List<RowRenderData> listCellRenderDatas01 = new ArrayList<RowRenderData>();
        listCellRenderDatas01.add(row01);
        List<ZhiBiaoZzxsjgbdqk> zhiBiaoZzxsjgbdqkList = exportDataPackage.getZhiBiaoZzxsjgbdqkList();
        for (ZhiBiaoZzxsjgbdqk zhiBiaoZzxsjgbdqk : zhiBiaoZzxsjgbdqkList) {
            listCellRenderDatas01.add(Rows.of(zhiBiaoZzxsjgbdqk.getCity(), zhiBiaoZzxsjgbdqk.getMom() + ""
                    , zhiBiaoZzxsjgbdqk.getYoy() + "", zhiBiaoZzxsjgbdqk.getFixedbase() + "")
                    .center().horizontalCenter().textFontSize(8).create());
        }

        RowRenderData[] rowRenderData01 = listCellRenderDatas01.stream().toArray(RowRenderData[]::new);
        paramMap.put("tableTemplate", Tables.of(rowRenderData01).create());

        //////////////合并///////////////////////
        RowRenderData row0 = Rows.of("城区", "登记项目及登记户数", null, null, "项目流摇情况", null, null, "平均中签率")
                .center().horizontalCenter().textFontSize(8).textColor("FFFFFF").bgColor("4472C4").create();
        RowRenderData row1 = Rows.of(null, "核发预售证次数", "房源", "报名登记", "摇号次数", "流摇次数", "流摇比例", null)
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
        RowRenderData[] rowRenderData = listCellRenderDatas.stream().toArray(RowRenderData[]::new);
        paramMap.put("tableMergeTemplate", Tables.of(rowRenderData).mergeRule(rule).create());

        return paramMap;
    }


    private Map<String, Object> dealChart(Map<String, Object> paramMap) {

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

        /* 测试图表的插入-------------------------------------*/
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

}
