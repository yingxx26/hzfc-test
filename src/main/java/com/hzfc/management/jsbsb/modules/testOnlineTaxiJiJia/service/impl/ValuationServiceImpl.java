package com.hzfc.management.jsbsb.modules.testOnlineTaxiJiJia.service.impl;


import com.hzfc.management.jsbsb.modules.testOnlineTaxiJiJia.basedto.DriveMeter;
import com.hzfc.management.jsbsb.modules.testOnlineTaxiJiJia.basedto.ForecastDetail;
import com.hzfc.management.jsbsb.modules.testOnlineTaxiJiJia.basedto.PriceMeter;
import com.hzfc.management.jsbsb.modules.testOnlineTaxiJiJia.basedto.request.CurrentPriceRequestDto;
import com.hzfc.management.jsbsb.modules.testOnlineTaxiJiJia.basedto.response.CurrentPriceResponseDto;
import com.hzfc.management.jsbsb.modules.testOnlineTaxiJiJia.constatnt.ChargingCategoryEnum;
import com.hzfc.management.jsbsb.modules.testOnlineTaxiJiJia.dao.cache.PriceCache;
import com.hzfc.management.jsbsb.modules.testOnlineTaxiJiJia.dao.cache.RuleCache;
import com.hzfc.management.jsbsb.modules.testOnlineTaxiJiJia.dto.valuation.charging.Rule;
import com.hzfc.management.jsbsb.modules.testOnlineTaxiJiJia.dto.valuation.discount.DiscountPrice;
import com.hzfc.management.jsbsb.modules.testOnlineTaxiJiJia.entity.OrderRulePrice;
import com.hzfc.management.jsbsb.modules.testOnlineTaxiJiJia.entity.OrderRulePriceDetail;
import com.hzfc.management.jsbsb.modules.testOnlineTaxiJiJia.service.ValuationService;
import com.hzfc.management.jsbsb.modules.testOnlineTaxiJiJia.task.ValuationRequestTask;
import com.hzfc.management.jsbsb.modules.testOnlineTaxiJiJia.task.ValuationTask;
import com.hzfc.management.jsbsb.modules.testOnlineTaxiJiJia.util.PriceHelper;
import com.hzfc.management.jsbsb.modules.testOnlineTaxiJiJia.util.UnitConverter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * ????????????
 *
 * @date 2018/8/14
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ValuationServiceImpl implements ValuationService {

   /* @NonNull
    private OrderDao orderDao;*/

    @NonNull
    private PriceCache priceCache;

    @NonNull
    private RuleCache ruleCache;

    @NonNull
    private ValuationRequestTask requestTask;

    @NonNull
    private ValuationTask valuationTask;

    @Override
    public BigDecimal calcForecastPrice(Integer orderId) {
        //??????????????????
        DriveMeter driveMeter = generateDriveMeter(orderId, ChargingCategoryEnum.Forecast);

        Rule rule = driveMeter.getRule();
        PriceMeter priceMeter = priceCache.get(orderId);
        if (priceMeter == null || rule == null || !ObjectUtils.nullSafeEquals(rule.getId(), priceMeter.getRuleId())) {
            //????????????
            priceMeter = generatePriceMeter(driveMeter);
        }

        //????????????
        priceMeter.setTagPrices(rule != null ? rule.getTagPrices() : null);

        //??????????????????
        BigDecimal totalPrice = PriceHelper.add(priceMeter.getBasicPriceValue(), valuationTask.calcTagPrice(driveMeter));
        priceMeter.setTotalPriceValue(totalPrice);

        //????????????
        priceCache.set(orderId, priceMeter, 1, TimeUnit.HOURS);

        //TODO
        doneForecast(orderId);

        //????????????
        return totalPrice;
    }

    @Override
    public void doneForecast(Integer orderId) {
        //????????????
        PriceMeter priceMeter = priceCache.get(orderId);

        if (priceMeter == null) {
            throw new RuntimeException(ERR_EXPIRE_FORECAST);
        }

        //??????????????????
        priceMeter.getRulePrice().setTotalPrice(priceMeter.getTotalPriceValue());

        //??????DB
        valuationTask.updateToDb(ChargingCategoryEnum.Forecast, priceMeter);
    }

    @Override
    public ForecastDetail requestForecastDetail(Integer orderId) {
        Rule rule = ruleCache.get(orderId);
        PriceMeter priceMeter = priceCache.get(orderId);

        if (priceMeter == null || rule == null) {
            throw new RuntimeException(ERR_EXPIRE_FORECAST);
        }

        DecimalFormat currencyFmt = new DecimalFormat("0.00");

        ForecastDetail detail = new ForecastDetail();
        detail.setTotalPrice(currencyFmt.format(priceMeter.getTotalPriceValue()));
        detail.setPath("" + priceMeter.getRulePrice().getPath());
        detail.setPathPrice(currencyFmt.format(priceMeter.getRulePrice().getPathPrice()));
        detail.setDuration("" + priceMeter.getRulePrice().getDuration());
        detail.setDurationPrice(currencyFmt.format(priceMeter.getRulePrice().getDurationPrice()));
        detail.setSupplementPrice(currencyFmt.format(priceMeter.getRulePrice().getSupplementPrice()));
        detail.setBeyondDistance("" + priceMeter.getRulePrice().getBeyondDistance());
        detail.setBeyondPrice(currencyFmt.format(priceMeter.getRulePrice().getBeyondPrice()));
        detail.setNightTime("" + priceMeter.getRulePrice().getNightTime());
        detail.setNightDistance("" + priceMeter.getRulePrice().getNightDistance());
        detail.setNightPrice(currencyFmt.format(priceMeter.getRulePrice().getNightPrice()));
        detail.setRestDistance("" + priceMeter.getRulePrice().getRestDistance());
        detail.setRestDistancePrice(currencyFmt.format(priceMeter.getRulePrice().getRestDistance()));
        detail.setRestDuration("" + priceMeter.getRulePrice().getRestDuration());
        detail.setRestDurationPrice(currencyFmt.format(priceMeter.getRulePrice().getRestDurationPrice()));
        detail.setDynamicDiscountRate("" + priceMeter.getRulePrice().getDynamicDiscountRate());
        detail.setBasePrice(currencyFmt.format(priceMeter.getRulePrice().getBasePrice()));

        List<ForecastDetail.PeriodRule> periodRule = Optional.ofNullable(priceMeter.getRulePriceDetails()).orElse(new ArrayList<>()).stream().map(d -> {
            ForecastDetail.PeriodRule r = new ForecastDetail.PeriodRule();
            r.setStartHour("" + d.getStartHour()).setEndHour("" + d.getEndHour());
            r.setDistance("" + d.getDistance()).setDistancePrice(currencyFmt.format(d.getDistancePrice()));
            r.setDuration("" + d.getDuration()).setTimePrice(currencyFmt.format(d.getTimePrice()));
            return r;
        }).collect(Collectors.toList());
        detail.setPeriodRule(periodRule);

        List<ForecastDetail.TagRule> tagRule = Optional.ofNullable(priceMeter.getTagPrices()).orElse(new ArrayList<>()).stream().map(d -> {
            ForecastDetail.TagRule t = new ForecastDetail.TagRule();
            t.setTagName(d.getName()).setTagPrice(currencyFmt.format(d.getPrice()));
            return t;
        }).collect(Collectors.toList());
        detail.setTagRule(tagRule);

        return detail;
    }

    @Override
    public CurrentPriceResponseDto calcCurrentPrice(CurrentPriceRequestDto dto) {
        LocalDateTime start = UnitConverter.dateToLocalDateTime(new Date(dto.getStartTime()));
        LocalDateTime end = UnitConverter.dateToLocalDateTime(new Date(dto.getEndTime()));

        DriveMeter driveMeter = generateDriveMeter(dto.getOrderId(), ChargingCategoryEnum.RealTime);
        driveMeter.setCurrentPriceRequestDto(dto);
        driveMeter.setDistance(requestTask.requestDistance(dto.getCarId(), driveMeter.getRule().getKeyRule().getCityCode(), start, end));

        //????????????
        PriceMeter priceMeter = generatePriceMeter(driveMeter);

        //??????????????????
        BigDecimal totalPrice = PriceHelper.add(priceMeter.getBasicPriceValue(), valuationTask.calcTagPrice(driveMeter));
        priceMeter.getRulePrice().setTotalPrice(totalPrice);

        //????????????
        CurrentPriceResponseDto currentPriceResponseDto = new CurrentPriceResponseDto();
        currentPriceResponseDto.setDistance(driveMeter.getDistance().getDistance()).setPrice(totalPrice);
        return currentPriceResponseDto;
    }

    @Override
    public BigDecimal calcSettlementPrice(Integer orderId) {
        //??????????????????
        DriveMeter driveMeter = generateDriveMeter(orderId, ChargingCategoryEnum.Settlement);

        //????????????
        PriceMeter priceMeter = generatePriceMeter(driveMeter);

        //??????????????????
        BigDecimal totalPrice = PriceHelper.add(priceMeter.getBasicPriceValue(), valuationTask.calcTagPrice(driveMeter));
        priceMeter.getRulePrice().setTotalPrice(totalPrice);

        //??????DB
        valuationTask.updateToDb(ChargingCategoryEnum.Settlement, priceMeter);

        //????????????
        ruleCache.delete(orderId);
        priceCache.delete(orderId);

        //????????????
        return priceMeter.getRulePrice().getTotalPrice();
    }

    /**
     * ??????????????????
     *
     * @param orderId          ??????ID
     * @param chargingCategory ??????????????????
     * @return ????????????????????????????????????????????????
     */
    @SneakyThrows
    private DriveMeter generateDriveMeter(Integer orderId, ChargingCategoryEnum chargingCategory) {
        Rule rule = requestTask.requestRule(orderId);
        DriveMeter driveMeter = new DriveMeter(chargingCategory);

        switch (chargingCategory) {
         /*   case Forecast:
                driveMeter.setOrder(orderDao.selectByOrderId(orderId)).setRule(rule).setRequestTask(requestTask);
                driveMeter.setRoute(requestTask.requestRoute(driveMeter));
                break;
            case Settlement:
                driveMeter.setOrder(orderDao.selectByOrderId(orderId)).setRule(rule).setRequestTask(requestTask);
                driveMeter.setDistance(requestTask.requestDistance(driveMeter));
                break;*/
            case RealTime:
                driveMeter.setRule(rule).setRequestTask(requestTask);
                break;
            default:
                break;
        }

        return driveMeter;
    }

    /**
     * ????????????
     *
     * @param driveMeter ????????????
     * @return ??????
     */
    @SneakyThrows
    private PriceMeter generatePriceMeter(DriveMeter driveMeter) {
        //??????????????????
        CompletableFuture<List<OrderRulePriceDetail>> calcPriceDetailFuture = valuationTask.calcDetailPrice(driveMeter);

        //??????????????????
        CompletableFuture<OrderRulePrice> calcPriceFuture = valuationTask.calcMasterPrice(driveMeter);

        //??????????????????  ???calcPriceDetailFuture->details??? ???calcPriceFuture->master???
        BigDecimal price = calcPriceDetailFuture.thenCombine(calcPriceFuture, (details, master) -> {
            //??????????????????
            valuationTask.calcOtherPrice(driveMeter, master, details);

            //??????????????????
            BigDecimal totalPrice = PriceHelper.add(master.getBasePrice(), master.getNightPrice(), master.getBeyondPrice(), master.getPathPrice(), master.getDurationPrice());

            //??????????????????
            master.setSupplementPrice(BigDecimal.ZERO);
            if (totalPrice.compareTo(master.getLowestPrice()) < 0) {
                master.setSupplementPrice(PriceHelper.subtract(master.getLowestPrice(), totalPrice));
                totalPrice = master.getLowestPrice();
            }

            //????????????
            DiscountPrice discount = valuationTask.calcDiscount(driveMeter);
            master.setDynamicDiscount(BigDecimal.ZERO);
            master.setDynamicDiscountRate(0D);
            if (null != discount) {
                master.setDynamicDiscountRate(discount.getDiscount());
                if (discount.getDiscount() < 0 || discount.getDiscount() > 1) {
                    throw new RuntimeException(ERR_DISCOUNT_RATE_RANGE);
                }
                master.setDynamicDiscount(PriceHelper.min(discount.getDiscountMaxPrice(), BigDecimal.valueOf(1 - discount.getDiscount()).multiply(totalPrice)));
            }

            totalPrice = PriceHelper.subtract(totalPrice, master.getDynamicDiscount());
            master.setTotalPrice(totalPrice);

            return master.getTotalPrice();
        }).join();

        //??????????????????
        PriceMeter priceMeter = new PriceMeter();
        priceMeter.setRulePrice(calcPriceFuture.join()).setRulePriceDetails(calcPriceDetailFuture.join())
                .setTagPrices(driveMeter.getRule().getTagPrices()).setBasicPriceValue(price).setRuleId(driveMeter.getRule().getId());

        return priceMeter;
    }


    /**
     * ????????????
     *
     * @param driveMeter ????????????
     * @return ??????
     */
    @SneakyThrows
    public PriceMeter generatePriceMeter_yxxtest(DriveMeter driveMeter) {
        //??????????????????
        CompletableFuture<List<OrderRulePriceDetail>> calcPriceDetailFuture = valuationTask.calcDetailPrice(driveMeter);

        //??????????????????
        CompletableFuture<OrderRulePrice> calcPriceFuture = valuationTask.calcMasterPrice(driveMeter);

        //??????????????????  ???calcPriceDetailFuture->details??? ???calcPriceFuture->master???
        BigDecimal price = calcPriceDetailFuture.thenCombine(calcPriceFuture, (details, master) -> {
            //??????????????????
            valuationTask.calcOtherPrice(driveMeter, master, details);

            //??????????????????
            BigDecimal totalPrice = PriceHelper.add(master.getBasePrice(), master.getNightPrice(), master.getBeyondPrice(), master.getPathPrice(), master.getDurationPrice());

            //??????????????????
            master.setSupplementPrice(BigDecimal.ZERO);
            if (totalPrice.compareTo(master.getLowestPrice()) < 0) {
                master.setSupplementPrice(PriceHelper.subtract(master.getLowestPrice(), totalPrice));
                totalPrice = master.getLowestPrice();
            }

            //????????????
            DiscountPrice discount = null;//valuationTask.calcDiscount(driveMeter);
            master.setDynamicDiscount(BigDecimal.ZERO);
            master.setDynamicDiscountRate(0D);
            if (null != discount) {
                master.setDynamicDiscountRate(discount.getDiscount());
                if (discount.getDiscount() < 0 || discount.getDiscount() > 1) {
                    throw new RuntimeException(ERR_DISCOUNT_RATE_RANGE);
                }
                master.setDynamicDiscount(PriceHelper.min(discount.getDiscountMaxPrice(), BigDecimal.valueOf(1 - discount.getDiscount()).multiply(totalPrice)));
            }

            totalPrice = PriceHelper.subtract(totalPrice, master.getDynamicDiscount());
            master.setTotalPrice(totalPrice);

            return master.getTotalPrice();
        }).join();

        //??????????????????
        PriceMeter priceMeter = new PriceMeter();
        priceMeter.setRulePrice(calcPriceFuture.join()).setRulePriceDetails(calcPriceDetailFuture.join())
                .setTagPrices(driveMeter.getRule().getTagPrices()).setBasicPriceValue(price).setRuleId(driveMeter.getRule().getId());

        return priceMeter;
    }

}
