package com.hzfc.management.yjzx.modules.reports.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzfc.management.yjzx.common.api.CommonPage;
import com.hzfc.management.yjzx.common.api.CommonResult;
import com.hzfc.management.yjzx.modules.reports.dto.ReportsWordTemplateParam;
import com.hzfc.management.yjzx.modules.reports.model.ReportsWordTemplate;
import com.hzfc.management.yjzx.modules.reports.service.ReportsWordTemplateService;
import com.hzfc.management.yjzx.utils.fileutils.Base64FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;

/**
 * word模板管理
 * Created by hzfc on 2018/4/26.
 */
@Controller
@Api(tags = "ReportsWordTemplateController", description = "word模板管理")
@RequestMapping("reports/wordtemplate")
public class ReportsWordTemplateController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportsWordTemplateController.class);

    // 将 yml 中的自定义配置注入到这里
    @Value("${hzfc.uploadfile.wordTemplate.path}")
    private String filePath;


    @Autowired
    private ReportsWordTemplateService reportsWordTemplateService;

    @ApiOperation("根据用途或模板名分页获取模板列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<ReportsWordTemplate>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                              @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<ReportsWordTemplate> reportsWordTemplateList = reportsWordTemplateService.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(reportsWordTemplateList));
    }

    @ApiOperation("修改模板状态")
    @RequestMapping(value = "/updateStatus/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateStatus(@PathVariable Long id, @RequestParam(value = "inuse") Integer inuse) {
        ReportsWordTemplate reportsWordTemplate = new ReportsWordTemplate();
        reportsWordTemplate.setInuse(inuse);
        boolean success = reportsWordTemplateService.update(id, reportsWordTemplate);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation(value = "新增模板")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<ReportsWordTemplate> create(@Validated @RequestBody ReportsWordTemplateParam wordTemplate, Principal principal) {
        String wordBase64 = wordTemplate.getWordBase64();
        if (StringUtils.isEmpty(wordBase64)) {
            return CommonResult.failed("请先上传图片");
        }
        ReportsWordTemplate reportsWordTemplate = new ReportsWordTemplate();
        BeanUtils.copyProperties(wordTemplate, reportsWordTemplate);
        String name = principal.getName();
        reportsWordTemplate.setCreateuser(name);
        reportsWordTemplate.setCreateTime(new Date());
        boolean success = reportsWordTemplateService.create(reportsWordTemplate, wordBase64);
        if (success) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 文件预览，需注意有些文件(比如word文档)是无法在浏览器预览的，这里演示图片的预览
     *
     * @param id
     * @param
     * @return
     */
    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<String> view(@PathVariable("id") Long id) {
        ReportsWordTemplate reportsWordTemplate = reportsWordTemplateService.getById(id);
        if (reportsWordTemplate == null) {
            return CommonResult.failed("文件不存在");
        }
        String templatepath = reportsWordTemplate.getTemplatepath();

        if (StringUtils.isEmpty(templatepath)) {
            return CommonResult.failed("文件不存在");
        }
        String base64 = null;
        try {
            base64 = Base64FileUtil.fileToBase64(templatepath);
        } catch (Exception e) {
            return CommonResult.failed("文件异常");
        }
        return CommonResult.success(base64);
    }


    @ApiOperation("删除指定模板")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {

        boolean success = reportsWordTemplateService.delete(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    // 文件上传 （可以多文件上传）
    /*@PostMapping("/upload")
    @ResponseBody
    public CommonResult fileUploads(@RequestBody WordTemplateBase64 wordBase64) {
        ZonedDateTime now = ZonedDateTime.now();
        // ISO_LOCAL_DATE 2020-03-25
        String format1 = DateTimeFormatter.ofPattern("yyyy-MM-dd-ss").format(now);
        MultipartFile multipartFile = Base64FileUtil.base64ToMultipart(wordBase64.getWordBase64());

        // 时间 和 日期拼接
        String newFileName = format1 + "wordTemplate.docx";
        String path = filePath + newFileName;
        // 得到文件保存的位置以及新文件名
        File dest = new File(path);
        try {
            if (!dest.exists()) {
                //先得到文件的上级目录，并创建上级目录，在创建文件
                dest.getParentFile().mkdirs();
                //创建文件
                dest.createNewFile();
            }
            // 上传的文件被保存了
            multipartFile.transferTo(dest);
            // 打印日志
            LOGGER.info("上传成功，当前上传的文件保存在 {}", filePath + newFileName);
            // 自定义返回的统一的 JSON 格式的数据，可以直接返回这个字符串也是可以的。
            return CommonResult.success(newFileName);
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
        // 待完成 —— 文件类型校验工作
        return CommonResult.failed("上传错误");
    }*/
}
