package com.hzfc.management.yjzx.modules.reports.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hzfc.management.yjzx.modules.reports.model.ReportsWordTemplate;

/**
 * 后台管理员管理Service
 * Created by hzfc on 2018/4/26.
 */
public interface ReportsWordTemplateService extends IService<ReportsWordTemplate> {

    /**
     * 根据用户名或昵称分页查询模板
     */
    Page<ReportsWordTemplate> list(String keyword, String categoryids, Integer pageSize, Integer pageNum);

    /**
     * 修改指定模板
     */
    boolean update(Long id, ReportsWordTemplate reportsWordTemplate);

    boolean create(ReportsWordTemplate reportsWordTemplate, String wordBase64);

    /**
     * 删除指定用户
     */
    boolean delete(Long id);

    public boolean updateAll(Long id, ReportsWordTemplate reportsWordTemplate, String wordBase64);

}
