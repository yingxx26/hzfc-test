package com.hzfc.management.jsbsb.modules.testquasarJx.constant;


import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 14-5-13
 * Time: 上午11:27
 * To change this template use File | Settings | File Templates.
 */
public class JxConstants {

    //结息利率
    public static final BigDecimal JX_LLDW = new BigDecimal("0.0001");
    //是否动用-
    public static final Integer SFDY_WDY = 0;//未动用
    public static final Integer SFDY_DY = 1;//动用
    //是否历史-
    public static final Integer SFLS_YES = 1;//是
    public static final Integer SFLS_NO = 0;//否
    //利率 是否有效-
    public static final Integer SFYX_YES = 1;//是
    public static final Integer SFYX_NO = 0;//否
    //利率 利率类型-
    public static final Integer LVLX_DQ = 1;//定期
    public static final Integer LVLX_HQ = 2;//活期
    //结息账户类型
    public static final Integer JXZHLX_BXJ = 1;//保修金
    public static final Integer JXZHLX_WXJ = 2;//维修金
    public static final Integer JXZHLX_ALL = 3;//维修金 + 保修金
    //结息方式
    public static final Integer JXFS_TH = 1;//个人维修金退还
    public static final Integer JXFS_ND = 2;//按 年度 保修金、维修金 批量结息
    //账户资金变动类型
    public static final Integer BDLX_JC = 1;//交存
    public static final Integer BDLX_SY = 2;//使用

    //结息账户异常类型
    public static final String KJX_ZC = "ZC";//可结息 状态=正常
    public static final String KJX_DATA = "DATA";//可结息 状态=数据
    public static final String KJX_QT = "QT";//可结息 状态=其他


    //结息标志   退还
    public static final int JXBZ_TH_BNJX = -999999;//本年结息
    public static final int JXBZ_TH_SNJX = -999998;//上年结息

}
