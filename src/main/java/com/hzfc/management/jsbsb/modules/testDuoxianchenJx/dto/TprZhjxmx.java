package com.hzfc.management.jsbsb.modules.testDuoxianchenJx.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author yxx
 * @Date 2022/4/22 15:16
 */
//结息明细
public class TprZhjxmx {

    private Date jxr;
    private Date czr;
    private String zhcode;
    private BigDecimal glfy;
    private BigDecimal yzhye;
    private BigDecimal zhlx;
    private BigDecimal jxzlx;
    private BigDecimal zhdqlx;
    private BigDecimal zhhqlx;
    private Integer jxlx;
    private BigDecimal dqye;
    private String nd;
    private BigDecimal gdlv;
    private Integer jxzq;
    private Integer jxbz;
    private String bz;
    private Integer jxpc;//结息批次

    public Date getJxr() {
        return jxr;
    }

    public void setJxr(Date jxr) {
        this.jxr = jxr;
    }

    public Date getCzr() {
        return czr;
    }

    public void setCzr(Date czr) {
        this.czr = czr;
    }

    public String getZhcode() {
        return zhcode;
    }

    public void setZhcode(String zhcode) {
        this.zhcode = zhcode;
    }

    public BigDecimal getGlfy() {
        return glfy;
    }

    public void setGlfy(BigDecimal glfy) {
        this.glfy = glfy;
    }

    public BigDecimal getYzhye() {
        return yzhye;
    }

    public void setYzhye(BigDecimal yzhye) {
        this.yzhye = yzhye;
    }

    public BigDecimal getZhlx() {
        return zhlx;
    }

    public void setZhlx(BigDecimal zhlx) {
        this.zhlx = zhlx;
    }

    public BigDecimal getJxzlx() {
        return jxzlx;
    }

    public void setJxzlx(BigDecimal jxzlx) {
        this.jxzlx = jxzlx;
    }

    public BigDecimal getZhdqlx() {
        return zhdqlx;
    }

    public void setZhdqlx(BigDecimal zhdqlx) {
        this.zhdqlx = zhdqlx;
    }

    public BigDecimal getZhhqlx() {
        return zhhqlx;
    }

    public void setZhhqlx(BigDecimal zhhqlx) {
        this.zhhqlx = zhhqlx;
    }

    public Integer getJxlx() {
        return jxlx;
    }

    public void setJxlx(Integer jxlx) {
        this.jxlx = jxlx;
    }

    public BigDecimal getDqye() {
        return dqye;
    }

    public void setDqye(BigDecimal dqye) {
        this.dqye = dqye;
    }

    public String getNd() {
        return nd;
    }

    public void setNd(String nd) {
        this.nd = nd;
    }

    public BigDecimal getGdlv() {
        return gdlv;
    }

    public void setGdlv(BigDecimal gdlv) {
        this.gdlv = gdlv;
    }

    public Integer getJxpc() {
        return jxpc;
    }

    public void setJxpc(Integer jxpc) {
        this.jxpc = jxpc;
    }

    public Integer getJxzq() {
        return jxzq;
    }

    public void setJxzq(Integer jxzq) {
        this.jxzq = jxzq;
    }

    public Integer getJxbz() {
        return jxbz;
    }

    public void setJxbz(Integer jxbz) {
        this.jxbz = jxbz;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

}
