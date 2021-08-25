package com.hzfc.management.wyzj.modules.szwy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzfc.management.wyzj.modules.szwy.model.TpqWyqygx;
import com.hzfc.management.wyzj.modules.szwy.model.TpqZcspb;
import com.hzfc.management.wyzj.modules.szwy.vo.ZcspVo;

/**
 * <p>
 * 注册审批表 服务类
 * </p>
 *
 * @author Yxx
 * @since 2021-08-20
 */
public interface ITpqZcspbService extends IService<TpqZcspb> {

    public boolean saveTpqZcspb(TpqWyqygx tpqWyqygx, String username);

    ZcspVo getzcps(Long ywslid);
}
