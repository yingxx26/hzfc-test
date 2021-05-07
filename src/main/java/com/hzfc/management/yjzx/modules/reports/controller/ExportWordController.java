package com.hzfc.management.yjzx.modules.reports.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.*;
import com.deepoove.poi.data.style.TableStyle;
import com.hzfc.management.yjzx.common.api.CommonResult;
import com.hzfc.management.yjzx.modules.reports.dto.ExportParam;
import com.hzfc.management.yjzx.modules.reports.model.ReportsWordTemplate;
import com.hzfc.management.yjzx.modules.reports.model.ZhiBiaoZzxsjgbdqk;
import com.hzfc.management.yjzx.modules.reports.service.ReportsWordTemplateService;
import com.hzfc.management.yjzx.modules.reports.service.ZhiBiaoZzxsjgbdqkService;
import com.hzfc.management.yjzx.utils.dateUtils.DateUtil;
import com.hzfc.management.yjzx.utils.fileutils.Base64FileUtil;
import com.hzfc.management.yjzx.utils.fileutils.DeleteFileUtil;
import com.hzfc.management.yjzx.utils.fileutils.SaveFileUtil;
import com.hzfc.management.yjzx.utils.wordutils.mergeCell3.DetailData3;
import com.hzfc.management.yjzx.utils.wordutils.mergeCell3.DetailTablePolicy3;
import com.hzfc.management.yjzx.utils.wordutils.mergeCell3.DetailTablePolicy4;
import io.swagger.annotations.ApiOperation;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
    @Value("${hzfc.tempfile.word.path}")
    private String tempfilePath;

    @Value("${hzfc.uploadfile.wordTemplate.path}")
    private String filePath;


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
        String yyyy = DateUtil.format(new Date(), "yyyy");
        String mm = DateUtil.format(new Date(), "MM");
        dataFinal.put("REPORT_YYYY", yyyy);
        dataFinal.put("REPORT_MM", mm);

        QueryWrapper<ZhiBiaoZzxsjgbdqk> wrapper = new QueryWrapper<>();
        // 数据库查询指标数据datas <db,data>
        Map<String, Object> datas_Zzxsjgbdqkdatas = zhiBiaoZzxsjgbdqkService.getMap(wrapper);
        Map<String, Object> data1 = new HashMap<String, Object>();
        datas_Zzxsjgbdqkdatas.forEach((k, v) -> {
            Optional.ofNullable(v).map(u -> data1.put("ODS_PY_ZZXSJGBDQK_MM_" + k, datas_Zzxsjgbdqkdatas.get(k)));
        });
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
        this.dealChart(dataFinal);
        String fileName = null;
        try {
            String fullpath = filePath + reportsWordTemplate.getTemplatepath();
            fileName = SaveFileUtil.savePoiFile(dataFinal, fullpath, tempfilePath);
        } catch (Exception e) {
            return CommonResult.failed("文件异常");
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

    private Map<String, Object> dealChart(Map<String, Object> paramMap) {
        /* 测试表格插入---------------------------------------*/
        //定义表格的头
        //方式一
        //RowRenderData headerData = RowRenderData.build("电灯名称","使用率");
        //设置样式
        TableStyle tStyle = new TableStyle();
        tStyle.setBackgroundColor("87CEEB");
        //表头方式二
		/*RowRenderData headerData = RowRenderData.build(
				new TextRenderData("FFFFFF","仪器名称"),new TextRenderData("FFFFFF","使用率"));*/
        //表头方式三

        List<CellRenderData> listCellRenderDatas = new ArrayList<CellRenderData>();
        CellRenderData cellRenderData1 = new CellRenderData();
        cellRenderData1.setCellText(new TextRenderData("000000", "电灯名称"));
        listCellRenderDatas.add(cellRenderData1);
        CellRenderData cellRenderData2 = new CellRenderData();
        cellRenderData2.setCellText(new TextRenderData("000000", "使用率"));
        listCellRenderDatas.add(cellRenderData2);
        CellRenderData cellRenderData3 = new CellRenderData();
        cellRenderData3.setCellText(new TextRenderData("000000", "使用年限"));
        listCellRenderDatas.add(cellRenderData3);
        RowRenderData headerData = new RowRenderData(listCellRenderDatas);
        headerData.setRowStyle(tStyle);
        headerData.setCells(listCellRenderDatas);

        List<RowRenderData> listRowList = new ArrayList<RowRenderData>();

        //将数据存储为了后边生成图样式
        List<String> devname = new ArrayList<String>();
        List<Double> useRate = new ArrayList<Double>();
        List<Integer> useYear = new ArrayList<Integer>();

        for (int i = 0; i < 5; i++) {

            //生成一行数据
            listRowList.add(RowRenderData.build("电灯_" + i, String.valueOf(Math.random() * 100) + "%", String.valueOf(i + 1)));

            //存入list,为了生成图表
            devname.add("电灯_" + i);
            useRate.add(Math.random() * 100);
            useYear.add(i + 1);
        }
        paramMap.put("table", new MiniTableRenderData(headerData, listRowList));

        /* 测试图表的插入-------------------------------------*/
        //柱状图生成
        ChartMultiSeriesRenderData bar = new ChartMultiSeriesRenderData();
        bar.setChartTitle("barCharts");
        //参数为数组
        bar.setCategories(devname.toArray(new String[devname.size()]));
        List<SeriesRenderData> seriesRenderDatas = new ArrayList<SeriesRenderData>();
        seriesRenderDatas.add(new SeriesRenderData("使用率", useRate.toArray(new Double[useRate.size()])));
        seriesRenderDatas.add(new SeriesRenderData("使用年限", useYear.toArray(new Integer[useYear.size()])));
        bar.setSeriesDatas(seriesRenderDatas);
        paramMap.put("barCharts", bar);

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

    /**
     * 信息导出word --- poi-tl（合并单元格（循环列表下的合并行）--商品订单明细、另加一个动态行表格）
     *
     * @throws IOException
     */
    @RequestMapping("/exportDataWord8")
    public void exportDataWord8(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Map<String, Object> params = new HashMap<>();

            // TODO 渲染其他类型的数据请参考官方文档

            String basePath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/template/";
            String resource = basePath + "order6.docx";//word模板地址

            // PaymentData3 datas = new PaymentData3();

            Map datas = new HashMap();

            TableStyle rowStyle = new TableStyle();
            rowStyle = new TableStyle();
            rowStyle.setAlign(STJc.CENTER);
            //组装循环体
            List<Map<String, Object>> typeLists = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> typeTotalList = new ArrayList<Map<String, Object>>();
            for (int x = 0; x < 3; x++) {
                DetailData3 detailTable = new DetailData3();
                List<RowRenderData> plists = new ArrayList<RowRenderData>();
                for (int i = 0; i < 6; i++) {
                    String typeName = "二级分类1" + x;
                    if (i == 3 || i == 4) {
                        typeName = "二级分类2" + x;
                    } else if (i == 5) {
                        typeName = "二级分类3" + x;
                    }
                    String index = String.valueOf(i + 1);
                    RowRenderData plist = RowRenderData.build(index, typeName, "商品" + i, "套", "2", "100", "技术参数" + i);
                    plist.setRowStyle(rowStyle);
                    plists.add(plist);

                }
                //二级分类 分组统计   商品个数
                List<Map<String, Object>> tlists = new ArrayList<Map<String, Object>>();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("typeName", "二级分类1" + x);
                map.put("listSize", "3");
                tlists.add(map);
                map = new HashMap<String, Object>();
                map.put("typeName", "二级分类2" + x);
                map.put("listSize", "2");
                tlists.add(map);
                map = new HashMap<String, Object>();
                map.put("typeName", "二级分类3" + x);
                map.put("listSize", "1");
                tlists.add(map);


                detailTable.setPlists(plists);
                detailTable.setTlists(tlists);
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("detail_table", detailTable);
                data.put("index", x + 1);
                data.put("sub_type", "大类" + x);
                data.put("total_price", 100 + x);
                typeLists.add(data);
            }
            /*datas.setTypeLists(typeLists);
            datas.setOrder_money("100");
            datas.setMoney_total("壹佰元整");*/
            datas.put("typeLists", typeLists);
            datas.put("order_money", "100");
            datas.put("money_total", "壹佰元整");

            Configure config = Configure.newBuilder().bind("detail_table", new DetailTablePolicy3())
                    .bind("typeLists", new DetailTablePolicy4()).build();

            XWPFTemplate template = XWPFTemplate.compile(resource, config).render(datas);

            //=================生成文件保存在本地D盘某目录下=================
            String temDir = "G:/wordTest/" + File.separator + "file/word/";
            ;//生成临时文件存放地址
            //生成文件名
            Long time = new Date().getTime();
            // 生成的word格式
            String formatSuffix = ".docx";
            // 拼接后的文件名
            String fileName = time + formatSuffix;//文件名  带后缀

            FileOutputStream fos = new FileOutputStream(temDir + fileName);
            template.write(fos);
            //=================生成word到设置浏览默认下载地址=================
            // 设置强制下载不打开
            response.setContentType("application/force-download");
            // 设置文件名
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
            OutputStream out = response.getOutputStream();
            template.write(out);
            out.flush();
            out.close();
            template.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
