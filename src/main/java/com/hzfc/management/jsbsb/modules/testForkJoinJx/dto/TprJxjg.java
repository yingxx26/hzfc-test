package com.hzfc.management.jsbsb.modules.testForkJoinJx.dto;

import java.math.BigDecimal;
import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 14-4-19
 * Time: 下午3:28
 * To change this template use File | Settings | File Templates.
 */
//结息结果
public class TprJxjg    {

    private Date jxr; // 结息日
    private Date scjxr;// 上次结息日
    private Date czr; //  操作日
    private Integer zhzs; // 账户总数
    private BigDecimal glfy;  //管理费用（扩展）
    private BigDecimal bgfy; // 办公费用（扩展）
    private BigDecimal yzhye; //原账户余额
    private BigDecimal jxlx; // 结息利息
    private BigDecimal zhlx;// 账户利息
    private BigDecimal zhcsgdlx;//账户产生固定利息
    private BigDecimal zhcshqlx;//账户产生活期利息
    private BigDecimal dqye; // 当前余额
    private String nd;   //
    private Integer jxbz; // 结息标识
    private Integer jxfs; // 结息方式：1=个人退还，2=年度结息z 注：个人结息退还的不存结息结果
    private Integer jxsj; // 结息数据来源（本次结息的数据对象）：1=保修金，2=维修金, 为空或者0，保修金和维修金一起结息
    private BigDecimal gdlv; // 固定利率
    private Integer jxzq; //结息周期
    private String bz; //
    private String czy;//操作员
    private String bsd;
//    private Integer jxpc;//结息批次

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

    public String getCzy() {
        return czy;
    }

    public BigDecimal getBgfy() {
        return bgfy;
    }

    public void setBgfy(BigDecimal bgfy) {
        this.bgfy = bgfy;
    }

    public void setCzy(String czy) {
        this.czy = czy;
    }

    public Integer getZhzs() {
        return zhzs;
    }

    public void setZhzs(Integer zhzs) {
        this.zhzs = zhzs;
    }

    public BigDecimal getGlfy() {
        return glfy;
    }

    public void setGlfy(BigDecimal glfy) {
        this.glfy = glfy;
    }

    public Integer getJxsj() {
        return jxsj;
    }

    public void setJxsj(Integer jxsj) {
        this.jxsj = jxsj;
    }

    public Date getScjxr() {
        return scjxr;
    }

    public void setScjxr(Date scjxr) {
        this.scjxr = scjxr;
    }

    public Integer getJxfs() {
        return jxfs;
    }

    public void setJxfs(Integer jxfs) {
        this.jxfs = jxfs;
    }

    public Integer getJxbz() {
        return jxbz;
    }

    public void setJxbz(Integer jxbz) {
        this.jxbz = jxbz;
    }

    public BigDecimal getYzhye() {
        return yzhye;
    }

    public void setYzhye(BigDecimal yzhye) {
        this.yzhye = yzhye;
    }

    public BigDecimal getJxlx() {
        return jxlx;
    }

    public void setJxlx(BigDecimal jxlx) {
        this.jxlx = jxlx;
    }

    public BigDecimal getZhlx() {
        return zhlx;
    }

    public void setZhlx(BigDecimal zhlx) {
        this.zhlx = zhlx;
    }

    public BigDecimal getZhcsgdlx() {
        return zhcsgdlx;
    }

    public void setZhcsgdlx(BigDecimal zhcsgdlx) {
        this.zhcsgdlx = zhcsgdlx;
    }

    public BigDecimal getZhcshqlx() {
        return zhcshqlx;
    }

    public void setZhcshqlx(BigDecimal zhcshqlx) {
        this.zhcshqlx = zhcshqlx;
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

    public Integer getJxzq() {
        return jxzq;
    }

    public void setJxzq(Integer jxzq) {
        this.jxzq = jxzq;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getBsd() {
        return bsd;
    }

    public void setBsd(String bsd) {
        this.bsd = bsd;
    }
}
