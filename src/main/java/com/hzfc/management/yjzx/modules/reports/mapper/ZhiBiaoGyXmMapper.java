package com.hzfc.management.yjzx.modules.reports.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzfc.management.yjzx.modules.reports.dto.Gyxm;
import com.hzfc.management.yjzx.modules.reports.model.ZhiBiaoYhbm;

import java.util.List;

/**
 * <p>
 * 后台用户表 Mapper 接口
 * </p>
 *
 * @author hzfc
 * @since 2020-08-21
 */
public interface ZhiBiaoGyXmMapper extends BaseMapper<Gyxm> {

    List<Gyxm> getGyxmList(String yyyymm);
}
