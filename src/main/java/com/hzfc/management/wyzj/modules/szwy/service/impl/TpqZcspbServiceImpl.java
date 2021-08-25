package com.hzfc.management.wyzj.modules.szwy.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzfc.management.wyzj.modules.szwy.mapper.TpqZcspbMapper;
import com.hzfc.management.wyzj.modules.szwy.model.ActProcinst;
import com.hzfc.management.wyzj.modules.szwy.model.TpqWyqygx;
import com.hzfc.management.wyzj.modules.szwy.model.TpqZcspb;
import com.hzfc.management.wyzj.modules.szwy.service.ITpqWyqygxService;
import com.hzfc.management.wyzj.modules.szwy.service.ITpqZcspbService;
import com.hzfc.management.wyzj.modules.szwy.service.SzwyActProcinstService;
import com.hzfc.management.wyzj.modules.szwy.vo.ActProcinstVo;
import com.hzfc.management.wyzj.modules.szwy.vo.TpqWyqygxVo;
import com.hzfc.management.wyzj.modules.szwy.vo.TpqZcspbVo;
import com.hzfc.management.wyzj.modules.szwy.vo.ZcspVo;
import com.hzfc.management.wyzj.utils.dateUtils.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 注册审批表 服务实现类
 * </p>
 *
 * @author Yxx
 * @since 2021-08-20
 */
@Service
public class TpqZcspbServiceImpl extends ServiceImpl<TpqZcspbMapper, TpqZcspb> implements ITpqZcspbService {

    @Autowired
    private SzwyActProcinstService szwyActProcinstService;

    @Autowired
    private ITpqWyqygxService iTpqWyqygxService;

    @Transactional
    public boolean saveTpqZcspb(TpqWyqygx tpqWyqygx, String username) {

        Long ywslid = szwyActProcinstService.saveActProcinst(username);
        boolean save = iTpqWyqygxService.save(tpqWyqygx);
        tpqWyqygx.setCode(tpqWyqygx.getId());
        iTpqWyqygxService.saveOrUpdate(tpqWyqygx);
        TpqZcspb tpqZcspb = new TpqZcspb();
        tpqZcspb.setWyqyid(tpqWyqygx.getId());
        tpqZcspb.setWyqycode(tpqWyqygx.getCode());
        tpqZcspb.setYwid(ywslid);
        boolean save1 = save(tpqZcspb);
        return save && save1;
    }

    @Override
    public ZcspVo getzcps(Long ywslid) {
        QueryWrapper<TpqZcspb> wrapper1 = new QueryWrapper<>();
        wrapper1.lambda().eq(TpqZcspb::getYwid, ywslid);
        TpqZcspb tpqZcspb = getOne(wrapper1);

        QueryWrapper<ActProcinst> wrapper2 = new QueryWrapper<>();
        wrapper2.lambda().eq(ActProcinst::getYwslid, ywslid);
        ActProcinst actProcinst = szwyActProcinstService.getOne(wrapper2);

        Integer wyqycode = tpqZcspb.getWyqycode();
        QueryWrapper<TpqWyqygx> wrapper3 = new QueryWrapper<>();
        wrapper3.lambda().eq(TpqWyqygx::getCode, wyqycode);
        TpqWyqygx tpqWyqygx = iTpqWyqygxService.getOne(wrapper3);


        ActProcinstVo actProcinstVo = new ActProcinstVo();
        BeanUtils.copyProperties(actProcinstVo, actProcinst);
        actProcinstVo.setCreatetime(DateUtil.format(actProcinst.getCreatetime(), "yyyy-MM-dd"));

        TpqWyqygxVo tpqWyqygxVo = new TpqWyqygxVo();
        BeanUtils.copyProperties(tpqWyqygxVo, tpqWyqygx);

        TpqZcspbVo tpqZcspbVo = new TpqZcspbVo();
        BeanUtils.copyProperties(tpqZcspbVo, tpqZcspb);

        ZcspVo zcspvo = new ZcspVo();
        zcspvo.setActProcinstVo(actProcinstVo);
        zcspvo.setTpqWyqygxVo(tpqWyqygxVo);
        zcspvo.setTpqZcspbVo(tpqZcspbVo);
        return zcspvo;
    }
}
