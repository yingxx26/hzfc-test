package com.hzfc.management.wyzj.modules.szwy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hzfc.management.wyzj.modules.szwy.model.ActProcinst;

/**
 * 后台管理员管理Service
 * Created by hzfc on 2018/4/26.
 */
public interface SzwyActProcinstService extends IService<ActProcinst> {

    public Page<ActProcinst> list(String username, Integer pageSize, Integer pageNum);


    public Long saveActProcinst(String username);
}
