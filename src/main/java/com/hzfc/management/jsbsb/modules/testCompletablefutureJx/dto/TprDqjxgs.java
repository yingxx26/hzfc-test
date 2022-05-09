package com.hzfc.management.jsbsb.modules.testCompletablefutureJx.dto;


import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 14-4-21
 * Time: 下午6:26
 * To change this template use File | Settings | File Templates.
 */
public class TprDqjxgs {

    private String zhcode;
    private BigDecimal snjyje;  //上年活期结息金额
    private BigDecimal bndqjxje;
    private Date jcsj;
    private Date jxsj;
    private BigDecimal dqcklv;
    private Integer jxbz;
    private Integer bxjwxj;
    private String bz;
    private Integer jxpc;//结息批次

    public String getZhcode() {
        return zhcode;
    }

    public void setZhcode(String zhcode) {
        this.zhcode = zhcode;
    }

    public BigDecimal getSnjyje() {
        return snjyje;
    }

    public void setSnjyje(BigDecimal snjyje) {
        this.snjyje = snjyje;
    }

    public BigDecimal getBndqjxje() {
        return bndqjxje;
    }

    public void setBndqjxje(BigDecimal bndqjxje) {
        this.bndqjxje = bndqjxje;
    }

    public Date getJcsj() {
        return jcsj;
    }

    public void setJcsj(Date jcsj) {
        this.jcsj = jcsj;
    }

    public Date getJxsj() {
        return jxsj;
    }

    public void setJxsj(Date jxsj) {
        this.jxsj = jxsj;
    }

    public Integer getJxpc() {
        return jxpc;
    }

    public void setJxpc(Integer jxpc) {
        this.jxpc = jxpc;
    }

    public Integer getBxjwxj() {
        return bxjwxj;
    }

    public void setBxjwxj(Integer bxjwxj) {
        this.bxjwxj = bxjwxj;
    }

    public BigDecimal getDqcklv() {
        return dqcklv;
    }

    public void setDqcklv(BigDecimal dqcklv) {
        this.dqcklv = dqcklv;
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
