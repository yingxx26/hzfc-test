package com.hzfc.management.yjzx.modules.reports.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzfc.management.yjzx.modules.reports.dto.Gyxm;
import com.hzfc.management.yjzx.modules.reports.mapper.ZhiBiaoGyDwDxxCqDfMapper;
import com.hzfc.management.yjzx.modules.reports.mapper.ZhiBiaoGyXmMapper;
import com.hzfc.management.yjzx.modules.reports.model.ZhiBiaoGyDwDxxCqDf;
import com.hzfc.management.yjzx.modules.reports.service.ZhiBiaoGyDwDxxCqDfService;
import com.hzfc.management.yjzx.modules.reports.service.ZhiBiaoGyXmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 后台管理员管理Service实现类
 * Created by hzfc on 2018/4/26.
 */
@Service
public class ZhiBiaoGyXmServicempl extends ServiceImpl<ZhiBiaoGyXmMapper, Gyxm> implements ZhiBiaoGyXmService {

    @Autowired
    private ZhiBiaoGyXmMapper zhiBiaoGyXmMapper;

    @Override
    public Gyxm getGyxmList(String yyyymm) {
        List<Gyxm> gyxmList = zhiBiaoGyXmMapper.getGyxmList(yyyymm);
        if (CollectionUtils.isEmpty(gyxmList)) {
            return new Gyxm();
        }
        return gyxmList.get(0);
    }
}
