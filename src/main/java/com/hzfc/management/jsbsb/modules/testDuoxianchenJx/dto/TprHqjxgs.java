package com.hzfc.management.jsbsb.modules.testDuoxianchenJx.dto;


import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 14-4-21
 * Time: 下午6:26
 * To change this template use File | Settings | File Templates.
 */
public class TprHqjxgs  {

    private String zhcode;
    private Integer sfdy;
    private BigDecimal dyje;
    private BigDecimal wdyje;
    private Date dysj;
    private Date dyjejcsj;
    private Date wdyjejcsj;
    private Integer jxbz;
    private Integer bxjwxj;
    private String bz;
    private Integer jxpc;//结息批次

    public Integer getBxjwxj() {
        return bxjwxj;
    }

    public void setBxjwxj(Integer bxjwxj) {
        this.bxjwxj = bxjwxj;
    }

    public String getZhcode() {
        return zhcode;
    }

    public void setZhcode(String zhcode) {
        this.zhcode = zhcode;
    }

    public Integer getSfdy() {
        return sfdy;
    }

    public void setSfdy(Integer sfdy) {
        this.sfdy = sfdy;
    }

    public BigDecimal getDyje() {
        return dyje;
    }

    public void setDyje(BigDecimal dyje) {
        this.dyje = dyje;
    }

    public BigDecimal getWdyje() {
        return wdyje;
    }

    public void setWdyje(BigDecimal wdyje) {
        this.wdyje = wdyje;
    }

    public Date getDysj() {
        return dysj;
    }

    public void setDysj(Date dysj) {
        this.dysj = dysj;
    }

    public Date getDyjejcsj() {
        return dyjejcsj;
    }

    public void setDyjejcsj(Date dyjejcsj) {
        this.dyjejcsj = dyjejcsj;
    }

    public Date getWdyjejcsj() {
        return wdyjejcsj;
    }

    public void setWdyjejcsj(Date wdyjejcsj) {
        this.wdyjejcsj = wdyjejcsj;
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

    public Integer getJxpc() {
        return jxpc;
    }

    public void setJxpc(Integer jxpc) {
        this.jxpc = jxpc;
    }
}
