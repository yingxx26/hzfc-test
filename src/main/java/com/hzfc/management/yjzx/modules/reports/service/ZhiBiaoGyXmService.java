package com.hzfc.management.yjzx.modules.reports.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzfc.management.yjzx.modules.reports.dto.Gyxm;
import com.hzfc.management.yjzx.modules.reports.model.ZhiBiaoGyDwDxxCqDf;

/**
 * 后台管理员管理Service
 * Created by hzfc on 2018/4/26.
 */
public interface ZhiBiaoGyXmService extends IService<Gyxm> {

    public Gyxm getGyxmList(String yyyymm);

}
