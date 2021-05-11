package com.hzfc.management.yjzx.modules.reports.controller;


import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.policy.HackLoopTableRenderPolicy;
import com.hzfc.management.yjzx.utils.wordutils.MoneyUtils;
import com.hzfc.management.yjzx.utils.wordutils.WordUtil;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 导出Word
 *
 * @author Administrator
 */
@RequestMapping("/auth/exportWord/")
@RestController
public class ExportWordControllerTemp {

    /**
     * 导出word首页
     */
    @RequestMapping(value = "/index")
    public ModelAndView toIndex(HttpServletRequest request) {
        return new ModelAndView("export/index");
    }

    /**
     * 用户信息导出word --- poi-tl
     *
     * @throws IOException
     */
    @RequestMapping("/exportUserWord")
    public void exportUserWord(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		response.setContentType("application/vnd.ms-excel");  
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + new SimpleDateFormat("yyyyMMddHHmm").format(new Date()) + ".docx");
        Map<String, Object> params = new HashMap<>();
        // 渲染文本
        params.put("name", "张三");
        params.put("position", "开发工程师");
        params.put("entry_time", "2020-07-30");
        params.put("province", "江苏省");
        params.put("city", "南京市");
        // 渲染图片
        params.put("picture", new PictureRenderData(120, 120, "G:\\wordTest\\square.png"));
        // TODO 渲染其他类型的数据请参考官方文档

        //word模板地址放在src/main/webapp/下
        //表示到项目的根目录（webapp）下，要是想到目录下的子文件夹，修改"/"即可
        String path = request.getSession().getServletContext().getRealPath("/");
        String templatePath = path + "template/user.docx";

        WordUtil.downloadWord(response.getOutputStream(), templatePath, params);
    }


    /**
     * 销售订单信息导出word --- poi-tl（包含动态行表格）
     *
     * @throws IOException
     */
    @RequestMapping("/exportDataWord3")
    public void exportDataWord3(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Map<String, Object> params = new HashMap<>();

            // TODO 渲染其他类型的数据请参考官方文档
            DecimalFormat df = new DecimalFormat("######0.00");
            Calendar now = Calendar.getInstance();
            double money = 0;//总金额
            //组装表格列表数据
            List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < 6; i++) {
                Map<String, Object> detailMap = new HashMap<String, Object>();
                detailMap.put("index", i + 1);//序号
                detailMap.put("title", "商品" + i);//商品名称
                detailMap.put("product_description", "套");//商品规格
                detailMap.put("buy_num", 3 + i);//销售数量
                detailMap.put("saleprice", 100 + i);//销售价格

                double saleprice = Double.valueOf(String.valueOf(100 + i));
                Integer buy_num = Integer.valueOf(String.valueOf(3 + i));
                String buy_price = df.format(saleprice * buy_num);
                detailMap.put("buy_price", buy_price);//单个商品总价格
                money = money + Double.valueOf(buy_price);

                detailList.add(detailMap);
            }
            //总金额
            String order_money = String.valueOf(money);
            //金额中文大写
            String money_total = MoneyUtils.change(money);

            String basePath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/template/";
            String resource = basePath + "order1.docx";//word模板地址
            //渲染表格
            HackLoopTableRenderPolicy policy = new HackLoopTableRenderPolicy();
            Configure config = Configure.newBuilder().bind("detailList", policy).build();
            XWPFTemplate template = XWPFTemplate.compile(resource, config).render(
                    new HashMap<String, Object>() {{
                        put("detailList", detailList);
                        put("order_number", "2356346346645");
                        put("y", now.get(Calendar.YEAR));//当前年
                        put("m", (now.get(Calendar.MONTH) + 1));//当前月
                        put("d", now.get(Calendar.DAY_OF_MONTH));//当前日
                        put("order_money", order_money);//总金额
                        put("money_total", money_total);//金额中文大写
                    }}
            );
            //=================生成文件保存在本地D盘某目录下=================
            String temDir = "G:/wordTest/" + File.separator + "file/word/";
            ;//生成临时文件存放地址
            //生成文件名
            Long time = new Date().getTime();
            // 生成的word格式
            String formatSuffix = ".docx";
            // 拼接后的文件名
            String fileName = time + formatSuffix;//文件名  带后缀

            File file = new File(temDir);
            if (!file.exists()) file.mkdirs();


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

    /**
     * 销售订单信息导出word --- poi-tl（包含两个动态行表格）
     *
     * @throws IOException
     */
    @RequestMapping("/exportDataWordD4")
    public void exportDataWordD4(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Map<String, Object> params = new HashMap<>();

            // TODO 渲染其他类型的数据请参考官方文档
            DecimalFormat df = new DecimalFormat("######0.00");
            Calendar now = Calendar.getInstance();
            double money = 0;//总金额
            //组装表格列表数据
            List<Map<String, Object>> typeList = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < 2; i++) {
                Map<String, Object> detailMap = new HashMap<String, Object>();
                detailMap.put("index", i + 1);//序号
                if (i == 0) {
                    detailMap.put("sub_type", "监督技术装备");//商品所属大类名称
                } else if (i == 1) {
                    detailMap.put("sub_type", "火灾调查装备");//商品所属大类名称
                } else if (i == 2) {
                    detailMap.put("sub_type", "工程验收装备");//商品所属大类名称
                }

                double saleprice = Double.valueOf(String.valueOf(100 + i));
                Integer buy_num = Integer.valueOf(String.valueOf(3 + i));
                String buy_price = df.format(saleprice * buy_num);
                detailMap.put("buy_price", buy_price);//所属大类总价格
                money = money + Double.valueOf(buy_price);
                typeList.add(detailMap);
            }
            //组装表格列表数据
            List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < 3; i++) {
                Map<String, Object> detailMap = new HashMap<String, Object>();
                detailMap.put("index", i + 1);//序号
                if (i == 0 || i == 1) {
                    detailMap.put("product_type", "二级分类1");//商品二级分类
                } else {
                    detailMap.put("product_type", "二级分类2");//商品二级分类
                }
                detailMap.put("title", "商品" + i);//商品名称
                detailMap.put("product_description", "套");//商品规格
                detailMap.put("buy_num", 3 + i);//销售数量
                detailMap.put("saleprice", 100 + i);//销售价格
                detailMap.put("technical_parameter", "技术参数" + i);//技术参数
                detailList.add(detailMap);
            }

            //总金额
            String order_money = String.valueOf(money);
            //金额中文大写
            String money_total = MoneyUtils.change(money);

            String basePath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/template/";
            String resource = basePath + "orderD2.docx";//word模板地址
            //渲染表格  动态行
            HackLoopTableRenderPolicy policy = new HackLoopTableRenderPolicy();
            Configure config = Configure.newBuilder()
                    .bind("typeList", policy).bind("detailList", policy).build();

            XWPFTemplate template = XWPFTemplate.compile(resource, config).render(
                    new HashMap<String, Object>() {{
                        put("typeList", typeList);
                        put("detailList", detailList);
                        put("order_number", "2356346346645");
                        put("y", now.get(Calendar.YEAR));//当前年
                        put("m", (now.get(Calendar.MONTH) + 1));//当前月
                        put("d", now.get(Calendar.DAY_OF_MONTH));//当前日
                        put("order_money", order_money);//总金额
                        put("money_total", money_total);//金额中文大写
                    }}
            );
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

    /**
     * 销售订单信息导出word --- poi-tl（包含动态行表格、循环列表中的动态行表格）
     *
     * @throws IOException
     */
    @RequestMapping("/exportDataWord4")
    public void exportDataWord4(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Map<String, Object> params = new HashMap<>();

            // TODO 渲染其他类型的数据请参考官方文档
            DecimalFormat df = new DecimalFormat("######0.00");
            Calendar now = Calendar.getInstance();
            double money = 0;//总金额
            //组装表格列表数据
            List<Map<String, Object>> typeList = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < 2; i++) {
                Map<String, Object> detailMap = new HashMap<String, Object>();
                detailMap.put("index", i + 1);//序号
                if (i == 0) {
                    detailMap.put("sub_type", "监督技术装备");//商品所属大类名称
                } else if (i == 1) {
                    detailMap.put("sub_type", "火灾调查装备");//商品所属大类名称
                } else if (i == 2) {
                    detailMap.put("sub_type", "工程验收装备");//商品所属大类名称
                }

                double saleprice = Double.valueOf(String.valueOf(100 + i));
                Integer buy_num = Integer.valueOf(String.valueOf(3 + i));
                String buy_price = df.format(saleprice * buy_num);
                detailMap.put("buy_price", buy_price);//所属大类总价格
                money = money + Double.valueOf(buy_price);
                typeList.add(detailMap);
            }
            //组装表格列表数据
            List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < 3; i++) {
                Map<String, Object> detailMap = new HashMap<String, Object>();
                detailMap.put("index", i + 1);//序号
                if (i == 0 || i == 1) {
                    detailMap.put("product_type", "二级分类1");//商品二级分类
                } else {
                    detailMap.put("product_type", "二级分类2");//商品二级分类
                }
                detailMap.put("title", "商品" + i);//商品名称
                detailMap.put("product_description", "套");//商品规格
                detailMap.put("buy_num", 3 + i);//销售数量
                detailMap.put("saleprice", 100 + i);//销售价格
                detailMap.put("technical_parameter", "技术参数" + i);//技术参数
                detailList.add(detailMap);
            }


            List<Map<String, Object>> tList = new ArrayList<Map<String, Object>>();
            Map<String, Object> tMap = new HashMap<String, Object>();
            tMap.put("index", 1);
            tMap.put("sub_type", "监督技术装备");
            tMap.put("detailList", detailList);
            tMap.put("buy_price", 100);
            tList.add(tMap);

            tMap = new HashMap<String, Object>();
            tMap.put("index", 2);
            tMap.put("sub_type", "火灾调查装备");
            tMap.put("detailList", detailList);
            tMap.put("buy_price", 200);
            tList.add(tMap);

            tMap = new HashMap<String, Object>();
            tMap.put("index", 3);
            tMap.put("sub_type", "工程验收装备");
            tMap.put("detailList", detailList);
            tMap.put("buy_price", 300);
            tList.add(tMap);


            //总金额
            String order_money = String.valueOf(money);
            //金额中文大写
            String money_total = MoneyUtils.change(money);

            String basePath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/template/";
            String resource = basePath + "order2.docx";//word模板地址
            //渲染表格  动态行
            HackLoopTableRenderPolicy policy = new HackLoopTableRenderPolicy();
            Configure config = Configure.newBuilder()
                    .bind("typeList", policy).bind("detailList", policy).build();

            XWPFTemplate template = XWPFTemplate.compile(resource, config).render(
                    new HashMap<String, Object>() {{
                        put("typeList", typeList);
                        put("typeProducts", tList);
                        put("order_number", "2356346346645");
                        put("y", now.get(Calendar.YEAR));//当前年
                        put("m", (now.get(Calendar.MONTH) + 1));//当前月
                        put("d", now.get(Calendar.DAY_OF_MONTH));//当前日
                        put("order_money", order_money);//总金额
                        put("money_total", money_total);//金额中文大写
                    }}
            );
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
