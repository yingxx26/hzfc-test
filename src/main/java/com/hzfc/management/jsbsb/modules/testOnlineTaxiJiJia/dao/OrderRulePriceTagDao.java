package com.hzfc.management.jsbsb.modules.testOnlineTaxiJiJia.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 标签计费明细操作DAO
 *
 * @date 2018/10/17
 */
@Repository
@RequiredArgsConstructor
public class OrderRulePriceTagDao {

 /*   @NonNull
    private OrderRulePriceTagMapper orderRulePriceTagMapper;

    *//**
     * 新增一组标签计费明细
     *
     * @param orderRulePriceTags 一组标签计费明细
     * @return 影响的记录数
     *//*
    public int insert(List<OrderRulePriceTag> orderRulePriceTags) {
        return orderRulePriceTagMapper.insertList(orderRulePriceTags);
    }

    *//**
     * 删除指定订单ID和类型的订单
     *
     * @param orderId          订单ID
     * @param chargingCategory 订单类型
     * @return 影响的记录数
     *//*
    public int deleteByOrderIdAndCategory(int orderId, ChargingCategoryEnum chargingCategory) {
        return orderRulePriceTagMapper.deleteByOrderIdAndCategory(orderId, chargingCategory.getCode());
    }*/
}
