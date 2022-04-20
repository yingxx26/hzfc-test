package com.hzfc.management.jsbsb.modules.testOnlineTaxiJiJia.entity;

import java.math.BigDecimal;
import java.util.Date;

public class OrderRulePriceDetail {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_order_rule_price_detail.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_order_rule_price_detail.order_id
     *
     * @mbggenerated
     */
    private Integer orderId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_order_rule_price_detail.category
     *
     * @mbggenerated
     */
    private String category;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_order_rule_price_detail.start_hour
     *
     * @mbggenerated
     */
    private Integer startHour;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_order_rule_price_detail.end_hour
     *
     * @mbggenerated
     */
    private Integer endHour;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_order_rule_price_detail.per_kilo_price
     *
     * @mbggenerated
     */
    private BigDecimal perKiloPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_order_rule_price_detail.per_minute_price
     *
     * @mbggenerated
     */
    private BigDecimal perMinutePrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_order_rule_price_detail.duration
     *
     * @mbggenerated
     */
    private Double duration;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_order_rule_price_detail.time_price
     *
     * @mbggenerated
     */
    private BigDecimal timePrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_order_rule_price_detail.distance
     *
     * @mbggenerated
     */
    private Double distance;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_order_rule_price_detail.distance_price
     *
     * @mbggenerated
     */
    private BigDecimal distancePrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_order_rule_price_detail.create_time
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_order_rule_price_detail.update_time
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_order_rule_price_detail.id
     *
     * @return the value of tbl_order_rule_price_detail.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_order_rule_price_detail.id
     *
     * @param id the value for tbl_order_rule_price_detail.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_order_rule_price_detail.order_id
     *
     * @return the value of tbl_order_rule_price_detail.order_id
     *
     * @mbggenerated
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_order_rule_price_detail.order_id
     *
     * @param orderId the value for tbl_order_rule_price_detail.order_id
     *
     * @mbggenerated
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_order_rule_price_detail.category
     *
     * @return the value of tbl_order_rule_price_detail.category
     *
     * @mbggenerated
     */
    public String getCategory() {
        return category;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_order_rule_price_detail.category
     *
     * @param category the value for tbl_order_rule_price_detail.category
     *
     * @mbggenerated
     */
    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_order_rule_price_detail.start_hour
     *
     * @return the value of tbl_order_rule_price_detail.start_hour
     *
     * @mbggenerated
     */
    public Integer getStartHour() {
        return startHour;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_order_rule_price_detail.start_hour
     *
     * @param startHour the value for tbl_order_rule_price_detail.start_hour
     *
     * @mbggenerated
     */
    public void setStartHour(Integer startHour) {
        this.startHour = startHour;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_order_rule_price_detail.end_hour
     *
     * @return the value of tbl_order_rule_price_detail.end_hour
     *
     * @mbggenerated
     */
    public Integer getEndHour() {
        return endHour;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_order_rule_price_detail.end_hour
     *
     * @param endHour the value for tbl_order_rule_price_detail.end_hour
     *
     * @mbggenerated
     */
    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_order_rule_price_detail.per_kilo_price
     *
     * @return the value of tbl_order_rule_price_detail.per_kilo_price
     *
     * @mbggenerated
     */
    public BigDecimal getPerKiloPrice() {
        return perKiloPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_order_rule_price_detail.per_kilo_price
     *
     * @param perKiloPrice the value for tbl_order_rule_price_detail.per_kilo_price
     *
     * @mbggenerated
     */
    public void setPerKiloPrice(BigDecimal perKiloPrice) {
        this.perKiloPrice = perKiloPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_order_rule_price_detail.per_minute_price
     *
     * @return the value of tbl_order_rule_price_detail.per_minute_price
     *
     * @mbggenerated
     */
    public BigDecimal getPerMinutePrice() {
        return perMinutePrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_order_rule_price_detail.per_minute_price
     *
     * @param perMinutePrice the value for tbl_order_rule_price_detail.per_minute_price
     *
     * @mbggenerated
     */
    public void setPerMinutePrice(BigDecimal perMinutePrice) {
        this.perMinutePrice = perMinutePrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_order_rule_price_detail.duration
     *
     * @return the value of tbl_order_rule_price_detail.duration
     *
     * @mbggenerated
     */
    public Double getDuration() {
        return duration;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_order_rule_price_detail.duration
     *
     * @param duration the value for tbl_order_rule_price_detail.duration
     *
     * @mbggenerated
     */
    public void setDuration(Double duration) {
        this.duration = duration;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_order_rule_price_detail.time_price
     *
     * @return the value of tbl_order_rule_price_detail.time_price
     *
     * @mbggenerated
     */
    public BigDecimal getTimePrice() {
        return timePrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_order_rule_price_detail.time_price
     *
     * @param timePrice the value for tbl_order_rule_price_detail.time_price
     *
     * @mbggenerated
     */
    public void setTimePrice(BigDecimal timePrice) {
        this.timePrice = timePrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_order_rule_price_detail.distance
     *
     * @return the value of tbl_order_rule_price_detail.distance
     *
     * @mbggenerated
     */
    public Double getDistance() {
        return distance;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_order_rule_price_detail.distance
     *
     * @param distance the value for tbl_order_rule_price_detail.distance
     *
     * @mbggenerated
     */
    public void setDistance(Double distance) {
        this.distance = distance;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_order_rule_price_detail.distance_price
     *
     * @return the value of tbl_order_rule_price_detail.distance_price
     *
     * @mbggenerated
     */
    public BigDecimal getDistancePrice() {
        return distancePrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_order_rule_price_detail.distance_price
     *
     * @param distancePrice the value for tbl_order_rule_price_detail.distance_price
     *
     * @mbggenerated
     */
    public void setDistancePrice(BigDecimal distancePrice) {
        this.distancePrice = distancePrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_order_rule_price_detail.create_time
     *
     * @return the value of tbl_order_rule_price_detail.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_order_rule_price_detail.create_time
     *
     * @param createTime the value for tbl_order_rule_price_detail.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_order_rule_price_detail.update_time
     *
     * @return the value of tbl_order_rule_price_detail.update_time
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_order_rule_price_detail.update_time
     *
     * @param updateTime the value for tbl_order_rule_price_detail.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}