package com.hzfc.management.yjzx.modules.reports.controller;


import com.alibaba.fastjson.JSONObject;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.style.TableStyle;
import com.hzfc.management.yjzx.common.api.CommonResult;
import com.hzfc.management.yjzx.modules.reports.model.ReportsWordTemplate;
import com.hzfc.management.yjzx.modules.reports.service.ReportsWordTemplateService;
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
// 将 yml 中的自定义配置注入到这里
    @Value("${hzfc.tempfile.word.path}")
    private String tempfilePath;

    // 将 yml 中的自定义配置注入到这里
    @Value("${hzfc.uploadfile.wordTemplate.path}")
    private String filePath;


    @Autowired
    private ReportsWordTemplateService reportsWordTemplateService;

    @RequestMapping("/exportUserWord/{templateId}")
    @ResponseBody
    public CommonResult<String> exportUserWord(@PathVariable("templateId") Long templateId) {
        Map<String, String> datas = new HashMap<>();
        // 数据库查询指标数据
        datas.put("zhibiao1", "南京市");
        /*datas.put("namedb", "张三");
        datas.put("positiondb", "开发工程师");
        datas.put("entry_timedb", "2020-07-30");
        datas.put("provincedb", "江苏省");
        datas.put("citydb", "南京市");*/
        // 数据库查询指标参数
        ReportsWordTemplate reportsWordTemplate = reportsWordTemplateService.getById(templateId);
        String zhibiaos = reportsWordTemplate.getZhibiaos();
        HashMap<String, String> zhibiaoMap = Optional.ofNullable(zhibiaos).map(u -> JSONObject.parseObject(u, HashMap.class)).get();

        Map<String, String> params = new HashMap<>();
        zhibiaoMap.forEach((k, v) -> {
            params.put(v, datas.get(k));
        });

        // 渲染图片
        //params.put("picture", new PictureRenderData(100, 120, "G:\\wordTest\\square.jpeg"));
        String fileName = null;
        try {
            String fullpath = filePath + reportsWordTemplate.getTemplatepath();
            fileName = SaveFileUtil.savePoiFile(params, fullpath, tempfilePath);
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
     * 销售订单信息导出word --- poi-tl（合并单元格（循环列表下的合并行）--商品订单明细、另加一个动态行表格）
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
