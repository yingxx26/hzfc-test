package com.hzfc.management.jsbsb.modules.testparallestreamJx.dto;

import java.math.BigDecimal;

/**
 * @Author yxx
 * @Date 2022/4/22 15:32
 */
public class ZhThjxDto {
    private Long zhcode;
    private BigDecimal bnlx;//退还时，本年产生的利息
    private BigDecimal snlx;//退还时，如果去年的利息还有结。退还时结算
    private BigDecimal zlx;//结息总利息

    public Long getZhcode() {
        return zhcode;
    }

    public void setZhcode(Long zhcode) {
        this.zhcode = zhcode;
    }

    public BigDecimal getBnlx() {
        return bnlx;
    }

    public void setBnlx(BigDecimal bnlx) {
        this.bnlx = bnlx;
    }

    public BigDecimal getSnlx() {
        return snlx;
    }

    public void setSnlx(BigDecimal snlx) {
        this.snlx = snlx;
    }

    public BigDecimal getZlx() {
        return zlx;
    }

    public void setZlx(BigDecimal zlx) {
        this.zlx = zlx;
    }
}
