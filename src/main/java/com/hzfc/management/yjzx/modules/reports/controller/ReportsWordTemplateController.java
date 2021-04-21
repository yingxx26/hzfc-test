package com.hzfc.management.yjzx.modules.reports.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzfc.management.yjzx.common.api.CommonPage;
import com.hzfc.management.yjzx.common.api.CommonResult;
import com.hzfc.management.yjzx.modules.reports.dto.WordTemplateBase64;
import com.hzfc.management.yjzx.modules.reports.model.ReportsWordTemplate;
import com.hzfc.management.yjzx.modules.reports.service.ReportsWordTemplateService;
import com.hzfc.management.yjzx.modules.ums.dto.UpdateAdminPasswordParam;
import com.hzfc.management.yjzx.modules.ums.model.UmsAdmin;
import com.hzfc.management.yjzx.modules.ums.model.UmsRole;
import com.hzfc.management.yjzx.modules.ums.service.UmsAdminService;
import com.hzfc.management.yjzx.utils.fileutils.Base64FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

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
    @Value("${hzfc.uploadfile.word.path}")
    private String filePath;

    @Autowired
    private UmsAdminService adminService;

    @Autowired
    private ReportsWordTemplateService reportsWordTemplateService;

    @ApiOperation("根据用户名或姓名分页获取用户列表")
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

    // 文件上传 （可以多文件上传）
    @PostMapping("/upload")
    @ResponseBody
    public CommonResult fileUploads(@RequestBody WordTemplateBase64 wordBase64) {
        ZonedDateTime now = ZonedDateTime.now();
        // ISO_LOCAL_DATE 2020-03-25
        String format1 = DateTimeFormatter.ISO_LOCAL_DATE.format(now);
        MultipartFile multipartFile = Base64FileUtil.base64ToMultipart(wordBase64.getWordBase64());
        // 获取上传的文件名称
        // String fileName = multipartFile.
        // 时间 和 日期拼接
        String newFileName = format1 + ".docx";
        // 得到文件保存的位置以及新文件名
        File dest = new File(filePath + newFileName);
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
            return CommonResult.success("上传成功");
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
        // 待完成 —— 文件类型校验工作
        return CommonResult.failed("上传错误");
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
        String serverUrl = "G://wordTest/file/word/";
        String url = serverUrl + "hhh.docx";
        File file = new File(url);
        //判断文件是否存在如果不存在就返回默认图标
        if (!(file.exists() && file.canRead())) {
            /*file = new File(request.getSession().getServletContext().getRealPath("/")
                    + "resource/icons/auth/root.png");*/
        }
        String base64 = null;
        try {
            base64 = Base64FileUtil.fileToBase64(url);
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.success(base64);
    }


    @ApiOperation("获取指定用户信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<UmsAdmin> getItem(@PathVariable Long id) {
        UmsAdmin admin = adminService.getById(id);
        return CommonResult.success(admin);
    }

    @ApiOperation("修改指定用户信息")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @RequestBody UmsAdmin admin) {
        boolean success = adminService.update(id, admin);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改指定用户密码")
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updatePassword(@Validated @RequestBody UpdateAdminPasswordParam updatePasswordParam) {
        int status = adminService.updatePassword(updatePasswordParam);
        if (status > 0) {
            return CommonResult.success(status);
        } else if (status == -1) {
            return CommonResult.failed("提交参数不合法");
        } else if (status == -2) {
            return CommonResult.failed("找不到该用户");
        } else if (status == -3) {
            return CommonResult.failed("旧密码错误");
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("删除指定用户信息")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        boolean success = adminService.delete(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }


    @ApiOperation("给用户分配角色")
    @RequestMapping(value = "/role/update", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateRole(@RequestParam("adminId") Long adminId,
                                   @RequestParam("roleIds") List<Long> roleIds) {
        int count = adminService.updateRole(adminId, roleIds);
        if (count >= 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("获取指定用户的角色")
    @RequestMapping(value = "/role/{adminId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<UmsRole>> getRoleList(@PathVariable Long adminId) {
        List<UmsRole> roleList = adminService.getRoleList(adminId);
        return CommonResult.success(roleList);
    }
}
