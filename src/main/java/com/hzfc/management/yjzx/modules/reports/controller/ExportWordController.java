package com.hzfc.management.yjzx.modules.reports.controller;


import cn.hutool.core.util.StrUtil;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 导出Word
 *
 * @author Administrator
 */
@RequestMapping("/reports/exportWord/")
@RestController
public class ExportWordController {


    /////////////////////前端做成网格////////////////////////////
    @Value("${hzfc.tempfile.word.path}")
    private String tempfilePath;

    @Value("${hzfc.uploadfile.wordTemplate.path}")
    private String filePath;

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
        if (!CollectionUtils.isEmpty(dates) && dates.size() == 2) {
            List<String> dateList = dates.stream().map(date -> DateUtil.format(date, "yyyy-MM-dd")).collect(Collectors.toList());
            dataFinal.put("firstDate", dateList.get(0));
            dataFinal.put("secondDate", dateList.get(1));
        }

        LocalDateTime localDateTime = LocalDateTime.now().plusDays(1);
        String lastMM = localDateTime.format(DateTimeFormatter.ofPattern("MM"));
        dataFinal.put("lastMonth", lastMM);
        Map<String, Object> data1 = new HashMap<String, Object>();

        // 数据库查询指标数据datas <db,data>
        QueryWrapper<ZhiBiaoZzxsjgbdqk> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<ZhiBiaoZzxsjgbdqk> lambda = wrapper.lambda();
        List<String> cities = Arrays.asList(city.split(","));
        lambda.in((ZhiBiaoZzxsjgbdqk::getCity), cities);
        List<Map<String, Object>> listmaps = zhiBiaoZzxsjgbdqkService.listMaps(wrapper);
        Optional<Map<String, Object>> first = listmaps.stream().filter(city -> city.get("CITY").equals("杭　　州")).findFirst();

        if (first.isPresent()) {
            Map<String, Object> datas_Zzxsjgbdqkdata_hz = first.get();
            datas_Zzxsjgbdqkdata_hz.forEach((k, v) -> {
                Optional.ofNullable(v).map(u -> data1.put("ODS_PY_ZZXSJGBDQK_MM_" + k, datas_Zzxsjgbdqkdata_hz.get(k)));
            });
        }

        List<ZhiBiaoZzxsjgbdqk> list = zhiBiaoZzxsjgbdqkService.list();
        ExportDataPackage exportDataPackage = new ExportDataPackage();
        exportDataPackage.setZhiBiaoZzxsjgbdqkList(list);
        // 数据库查询指标参数zhibiaoMap <db,word>
        ReportsWordTemplate reportsWordTemplate = reportsWordTemplateService.getById(templateId);
        String zhibiaos = reportsWordTemplate.getZhibiaos();
        HashMap<String, String> zhibiaoMap = Optional.ofNullable(zhibiaos).map(u -> JSONObject.parseObject(u, HashMap.class)).get();

        zhibiaoMap.forEach((k, v) -> {
            Optional.ofNullable(v).map(u -> data1.put(v, data1.get(k)));
        });

        // 渲染图片
        //params.put("picture", new PictureRenderData(100, 120, "G:\\wordTest\\square.jpeg"));
        dataFinal.putAll(data1);
        this.dealTabletest(dataFinal, exportDataPackage);
        this.dealChart(dataFinal);
        String fileName = null;
        try {
            String fullpath = filePath + reportsWordTemplate.getTemplatepath();
            fileName = SaveFileUtil.savePoiFile(dataFinal, fullpath, tempfilePath);
        } catch (Exception e) {
            return CommonResult.failed("");
        }

        String fullpath = tempfilePath + fileName;
        String base64 = null;
        try {
            base64 = Base64FileUtil.fileToBase64(fullpath);
        } catch (Exception e) {
            return CommonResult.failed("文件异常");
        }

        //  这里再删除文件
        DeleteFileUtil.delete(fullpath);
        return CommonResult.success(base64);
    }

    private Map<String, Object> dealTabletest(Map<String, Object> paramMap, ExportDataPackage exportDataPackage) {

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
        paramMap.put("tableTemplate", Tables.of(rowRenderData).mergeRule(rule).create());

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

        String fullpath = tempfilePath + filename;
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
