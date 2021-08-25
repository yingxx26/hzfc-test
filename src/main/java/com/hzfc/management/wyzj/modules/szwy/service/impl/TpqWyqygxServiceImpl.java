package com.hzfc.management.wyzj.modules.szwy.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzfc.management.wyzj.modules.szwy.mapper.TpqWyqygxMapper;
import com.hzfc.management.wyzj.modules.szwy.model.TpqWyqygx;
import com.hzfc.management.wyzj.modules.szwy.service.ITpqWyqygxService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 物业企业关系 服务实现类
 * </p>
 *
 * @author Yxx
 * @since 2021-08-19
 */
@Service
public class TpqWyqygxServiceImpl extends ServiceImpl<TpqWyqygxMapper, TpqWyqygx> implements ITpqWyqygxService {


    @Transactional
    public boolean saveTpqWyqygx(TpqWyqygx tpqWyqygx) {
        return save(tpqWyqygx);
    }
}
