package com.hzfc.management.jsbsb.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;


import java.util.Date;

/**
 * 优惠券（用户领取的优惠券记录）实体表
 *
 * @AUTHOR zhangxf
 * @CREATE 2020-02-14 11:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coupon {
    /**
     * 自增主键
     */

    private Integer id;

    /**
     * 关联优惠券模版的主键（逻辑外键）
     */
    private Integer templateId;
    /**
     * 领取用户
     */
    private Long userId;

    /**
     * 优惠券码
     */
    private String couponCode;

    /**
     * 领取时间
     */
    @CreatedDate
    private Date assignTime;
    /**
     * 优惠券状态
     */
    private CouponStatus status;

    public Coupon(Integer templateId, Long userId, String couponCode, CouponStatus status) {
        this.templateId = templateId;
        this.userId = userId;
        this.couponCode = couponCode;
        this.status = status;
    }

    public Coupon(Integer id, Integer templateId, Long userId, String couponCode, CouponStatus status) {
        this.id = id;
        this.templateId = templateId;
        this.userId = userId;
        this.couponCode = couponCode;
        this.status = status;
    }
}
