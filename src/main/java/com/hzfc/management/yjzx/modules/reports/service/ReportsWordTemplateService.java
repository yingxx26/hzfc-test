package com.hzfc.management.yjzx.modules.reports.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hzfc.management.yjzx.modules.reports.model.ReportsWordTemplate;
import com.hzfc.management.yjzx.modules.ums.dto.UpdateAdminPasswordParam;
import com.hzfc.management.yjzx.modules.ums.model.UmsAdmin;
import com.hzfc.management.yjzx.modules.ums.model.UmsResource;
import com.hzfc.management.yjzx.modules.ums.model.UmsRole;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 后台管理员管理Service
 * Created by hzfc on 2018/4/26.
 */
public interface ReportsWordTemplateService extends IService<ReportsWordTemplate> {

    /**
     * 根据用户名或昵称分页查询模板
     */
    Page<ReportsWordTemplate> list(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 修改指定模板
     */
    boolean update(Long id, ReportsWordTemplate reportsWordTemplate);

    /* *//**
     * 删除指定用户
     *//*
    boolean delete(Long id);


    *//**
     * 修改密码
     *//*
    int updatePassword(UpdateAdminPasswordParam updatePasswordParam);

    *//**
     * 获取用户信息
     *//*
    UserDetails loadUserByUsername(String username);*/
}
