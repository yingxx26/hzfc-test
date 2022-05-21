package com.hzfc.management.jsbsb.modules.testparallestreamJx.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author yxx
 * @Date 2022/2/20 12:26
 */
//结息账号资金变动
public class TprJxzhzjbd {

    private String zhcode;
    private Integer jxlx;       //结息利息
    private BigDecimal bdje;    //变动金额
    private Integer bdlx;       //变动金额
    private Date bdsj;          //变动时间
    private BigDecimal bdhzhye; //变动后账户余额
    private Integer jxbz;       //结息标志
    private Integer jxpc;       //结息批次
    private Integer zhbdlx;     //账户变动明细变动类型  实际对应账户变动信息表的BDLX
    private String bz;

    public String getZhcode() {
        return zhcode;
    }

    public void setZhcode(String zhcode) {
        this.zhcode = zhcode;
    }

    public Integer getJxlx() {
        return jxlx;
    }

    public void setJxlx(Integer jxlx) {
        this.jxlx = jxlx;
    }

    public BigDecimal getBdje() {
        return bdje;
    }

    public void setBdje(BigDecimal bdje) {
        this.bdje = bdje;
    }

    public Integer getBdlx() {
        return bdlx;
    }

    public void setBdlx(Integer bdlx) {
        this.bdlx = bdlx;
    }

    public Date getBdsj() {
        return bdsj;
    }

    public void setBdsj(Date bdsj) {
        this.bdsj = bdsj;
    }

    public BigDecimal getBdhzhye() {
        return bdhzhye;
    }

    public void setBdhzhye(BigDecimal bdhzhye) {
        this.bdhzhye = bdhzhye;
    }

    public Integer getJxbz() {
        return jxbz;
    }

    public void setJxbz(Integer jxbz) {
        this.jxbz = jxbz;
    }

    public Integer getJxpc() {
        return jxpc;
    }

    public void setJxpc(Integer jxpc) {
        this.jxpc = jxpc;
    }

    public Integer getZhbdlx() {
        return zhbdlx;
    }

    public void setZhbdlx(Integer zhbdlx) {
        this.zhbdlx = zhbdlx;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }
}
