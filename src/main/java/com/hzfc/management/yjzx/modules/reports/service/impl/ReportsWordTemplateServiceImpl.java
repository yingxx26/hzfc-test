package com.hzfc.management.yjzx.modules.reports.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzfc.management.yjzx.modules.reports.mapper.ReportsWordTemplateMapper;
import com.hzfc.management.yjzx.modules.reports.model.ReportsWordTemplate;
import com.hzfc.management.yjzx.modules.reports.service.ReportsWordTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 后台管理员管理Service实现类
 * Created by hzfc on 2018/4/26.
 */
@Service
public class ReportsWordTemplateServiceImpl extends ServiceImpl<ReportsWordTemplateMapper, ReportsWordTemplate> implements ReportsWordTemplateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportsWordTemplateServiceImpl.class);


    /*@Override
    public UmsAdmin getAdminByUsername(String username) {

        QueryWrapper<UmsAdmin> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsAdmin::getUsername, username);
        List<UmsAdmin> adminList = list(wrapper);
        if (adminList != null && adminList.size() > 0) {
            UmsAdmin admin = adminList.get(0);
            return admin;
        }
        return null;
    }


    */

    /**
     * 根据用户名修改登录时间
     *//*
    private void updateLoginTimeByUsername(String username) {
        UmsAdmin record = new UmsAdmin();
        record.setLoginTime(new Date());
        QueryWrapper<UmsAdmin> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsAdmin::getUsername, username);
        update(record, wrapper);
    }*/
    @Override
    public Page<ReportsWordTemplate> list(String keyword, Integer pageSize, Integer pageNum) {
        Page<ReportsWordTemplate> page = new Page<>(pageNum, pageSize);
        QueryWrapper<ReportsWordTemplate> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<ReportsWordTemplate> lambda = wrapper.lambda();
        if (StrUtil.isNotEmpty(keyword)) {
            lambda.like(ReportsWordTemplate::getCreateuser, keyword);
            lambda.or().like(ReportsWordTemplate::getUsepurpose, keyword);
        }
        return page(page, wrapper);
    }

    @Override
    public boolean update(Long id, ReportsWordTemplate reportsWordTemplate) {
        reportsWordTemplate.setId(id);
        boolean success = updateById(reportsWordTemplate);
        return success;
    }
/*
    @Override
    public boolean delete(Long id) {
        boolean success = removeById(id);
        return success;
    }


    @Override
    public List<UmsRole> getRoleList(Long adminId) {
        return roleMapper.getRoleList(adminId);
    }


    @Override
    public int updatePassword(UpdateAdminPasswordParam param) {
        if (StrUtil.isEmpty(param.getUsername())
                || StrUtil.isEmpty(param.getOldPassword())
                || StrUtil.isEmpty(param.getNewPassword())) {
            return -1;
        }
        QueryWrapper<UmsAdmin> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsAdmin::getUsername, param.getUsername());
        List<UmsAdmin> adminList = list(wrapper);
        if (CollUtil.isEmpty(adminList)) {
            return -2;
        }
        UmsAdmin umsAdmin = adminList.get(0);
        if (!passwordEncoder.matches(param.getOldPassword(), umsAdmin.getPassword())) {
            return -3;
        }
        umsAdmin.setPassword(passwordEncoder.encode(param.getNewPassword()));
        updateById(umsAdmin);
        return 1;
    }*/

}
