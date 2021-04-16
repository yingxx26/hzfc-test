package com.hzfc.management.yjzx.modules.reports.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzfc.management.yjzx.common.api.CommonPage;
import com.hzfc.management.yjzx.common.api.CommonResult;
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
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
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


    /**
     * 文件预览，需注意有些文件(比如word文档)是无法在浏览器预览的，这里演示图片的预览
     *
     * @param id
     * @param response
     * @return
     */
    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<String> view(@PathVariable("id") Long id, HttpServletResponse response) {
        /**
         * 关于文件的信息，实际中应该是根据某个字段去数据库查询得到的，比如根据该文件记录在DB中的id
         * 这里方便演示，我直接写死了文件的路径filePath
         */
      /*  String filePath = "G://wordTest/file/word/1618484727876.docx"; // 本该根据ID去数据库查，这里是hard code
        // 设返回的contentType
        response.setContentType("application/msword"); // 不同文件的MimeType参考后续链接
        // 读取路径下面的文件
        try {
            FileCopyUtils.copy(new FileInputStream(filePath), response.getOutputStream());
        } catch (IOException e) {

        }
        LOGGER.info("");*/

        String serverUrl = "G://wordTest/file/word/";
        String url = serverUrl + "1618484727876.docx";
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

    @ApiOperation("修改帐号状态")
    @RequestMapping(value = "/updateStatus/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateStatus(@PathVariable Long id, @RequestParam(value = "status") Integer status) {
        UmsAdmin umsAdmin = new UmsAdmin();
        umsAdmin.setStatus(status);
        boolean success = adminService.update(id, umsAdmin);
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
