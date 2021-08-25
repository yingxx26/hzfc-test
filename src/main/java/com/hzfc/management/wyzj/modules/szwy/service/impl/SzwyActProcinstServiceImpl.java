package com.hzfc.management.wyzj.modules.szwy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzfc.management.wyzj.generator.SnowFlakeGenerator;
import com.hzfc.management.wyzj.modules.szwy.mapper.SzwyActProcinstMapper;
import com.hzfc.management.wyzj.modules.szwy.model.ActProcinst;
import com.hzfc.management.wyzj.modules.szwy.model.TpqWyqygx;
import com.hzfc.management.wyzj.modules.szwy.service.SzwyActProcinstService;
import com.hzfc.management.wyzj.modules.ums.model.UmsMenu;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


/**
 * 后台管理员管理Service实现类
 * Created by hzfc on 2018/4/26.
 */
@Service
public class SzwyActProcinstServiceImpl extends ServiceImpl<SzwyActProcinstMapper, ActProcinst> implements SzwyActProcinstService {


    public Page<ActProcinst> list(String username, Integer pageSize, Integer pageNum) {
        Page<ActProcinst> page = new Page<>(pageNum, pageSize);
        QueryWrapper<ActProcinst> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .orderByDesc(ActProcinst::getCreatetime);

        return page(page, wrapper);
    }

    @Transactional
    public Long saveActProcinst(String username) {
        Date date = new Date();
        ActProcinst actProcinst = new ActProcinst();
        actProcinst.setCreatetime(date);
        actProcinst.setCurrentuser(username);
        actProcinst.setProcdefid(1L);
        actProcinst.setProcinstid(SnowFlakeGenerator.generateId());
        actProcinst.setYwslid(SnowFlakeGenerator.generateId());
        actProcinst.setYwname(username + "申请注册");
        save(actProcinst);
        return actProcinst.getYwslid();
    }
}
