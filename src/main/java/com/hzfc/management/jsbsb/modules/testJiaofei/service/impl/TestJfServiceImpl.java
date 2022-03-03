package com.hzfc.management.jsbsb.modules.testJiaofei.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hzfc.management.jsbsb.modules.testJiaofei.dto.FeeDto;
import com.hzfc.management.jsbsb.modules.testJiaofei.dto.OwnerCarDto;
import com.hzfc.management.jsbsb.modules.testJiaofei.dto.RoomDto;
import com.hzfc.management.jsbsb.modules.testJiaofei.service.TestJfService;
import com.hzfc.management.jsbsb.modules.testJiaofei.util.DateUtil2;
import com.hzfc.management.jsbsb.modules.testJiaofei.util.StringUtil;
import com.hzfc.management.jsbsb.modules.testJx.constant.JxConstants;
import com.hzfc.management.jsbsb.modules.testJx.dto.TprDqjxgs;
import com.hzfc.management.jsbsb.modules.testJx.dto.TprHqjxgs;
import com.hzfc.management.jsbsb.modules.testJx.dto.TprJxjg;
import com.hzfc.management.jsbsb.modules.testJx.dto.TprJxzhzjbd;
import com.hzfc.management.jsbsb.utils.dateUtils.DateUtil;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * 后台管理员管理Service实现类
 * Created by hzfc on 2018/4/26.
 */
@Service
public class TestJfServiceImpl implements TestJfService {


    @Override
    public void computeFeePrice(FeeDto feeDto, RoomDto roomDto) {

        if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(feeDto.getPayerObjType())) { //房屋相关
            computeFeePriceByRoom(feeDto, roomDto);
        } /*else if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(feeDto.getPayerObjType())) {//车位相关
            computeFeePriceByParkingSpace(feeDto);
        } else if (FeeDto.PAYER_OBJ_TYPE_CONTRACT.equals(feeDto.getPayerObjType())) { //房屋相关
            computeFeePriceByContract(feeDto, roomDto);
        }*/
    }


    /**
     * 根据房屋来算单价
     *
     * @param feeDto
     */
    private void computeFeePriceByRoom(FeeDto feeDto, RoomDto roomDto) {
        Map<String, Object> targetEndDateAndOweMonth = getTargetEndDateAndOweMonth(feeDto);
        Date targetEndDate = (Date) targetEndDateAndOweMonth.get("targetEndDate");
        double oweMonth = (double) targetEndDateAndOweMonth.get("oweMonth");

        String computingFormula = feeDto.getComputingFormula();
        Map feePriceAll = getFeePrice(feeDto, roomDto);
        feeDto.setFeePrice(Double.parseDouble(feePriceAll.get("feePrice").toString()));
        //double month = dayCompare(feeDto.getEndTime(), DateUtil.getCurrentDate());
        BigDecimal price = new BigDecimal(feeDto.getFeePrice());
        price = price.multiply(new BigDecimal(oweMonth));
        feeDto.setFeePrice(price.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue());
        feeDto.setDeadlineTime(targetEndDate);

        //动态费用
        if ("4004".equals(computingFormula)
                && FeeDto.FEE_FLAG_ONCE.equals(feeDto.getFeeFlag())
                && !FeeDto.STATE_FINISH.equals(feeDto.getState())) {
            feeDto.setAmountOwed(feeDto.getFeePrice() + "");
            //feeDto.setDeadlineTime(DateUtil.getCurrentDate()); 欠费日期不对先注释
        }
    }

    /**
     * 计算 计费结束时间和 欠费月份（可能存在小数点）
     *
     * @param feeDto
     * @param ownerCarDto
     * @return
     */
    public Map getTargetEndDateAndOweMonth(FeeDto feeDto, OwnerCarDto ownerCarDto) {
        Date targetEndDate = null;
        double oweMonth = 0.0;

        Map<String, Object> targetEndDateAndOweMonth = new HashMap<>();
        //判断当前费用是否已结束
        if (FeeDto.STATE_FINISH.equals(feeDto.getState())) {
            targetEndDate = feeDto.getEndTime();
            targetEndDateAndOweMonth.put("oweMonth", oweMonth);
            targetEndDateAndOweMonth.put("targetEndDate", targetEndDate);
            return targetEndDateAndOweMonth;
        }
        //当前费用为一次性费用
        if (FeeDto.FEE_FLAG_ONCE.equals(feeDto.getFeeFlag())) {
            //先取 deadlineTime
            if (feeDto.getDeadlineTime() != null) {
                targetEndDate = feeDto.getDeadlineTime();
            } else if (!StringUtil.isEmpty(feeDto.getCurDegrees())) {
                targetEndDate = feeDto.getCurReadingTime();
            } else if (feeDto.getImportFeeEndTime() == null) {
                targetEndDate = feeDto.getConfigEndTime();
            } else {
                targetEndDate = feeDto.getImportFeeEndTime();
            }
            //判断当前费用是不是导入费用
            oweMonth = 1.0;
        } else { //周期性费用
            //当前时间
            Date billEndTime = DateUtil2.getCurrentDate();
            String formatbillEndTime = DateUtil.format(billEndTime, "yyyy-MM-dd");
            System.out.println("当前时间billEndTime" + formatbillEndTime);
            //建账时间
            Date startDate = feeDto.getStartTime();
            String formattargetstartDate = DateUtil.format(startDate, "yyyy-MM-dd");
            System.out.println("费用开始时间startDate" + formattargetstartDate);
            //计费起始时间
            Date endDate = feeDto.getEndTime();
            String formattargetendDate = DateUtil.format(endDate, "yyyy-MM-dd");
            System.out.println("费用结束时间endDate" + formattargetendDate);

            //缴费周期
            long paymentCycle = Long.parseLong(feeDto.getPaymentCycle());
            System.out.println("缴费周期paymentCycle" + paymentCycle);
            // 当前时间 - 开始时间  = 月份
            double mulMonth = 0.0;
            mulMonth = dayCompare(startDate, billEndTime);

            // 月份/ 周期 = 轮数（向上取整）
            double round = 0.0;
            if ("1200".equals(feeDto.getPaymentCd())) { // 1200预付费
                round = Math.floor(mulMonth / paymentCycle) + 1;
            } else { //2100后付费
                round = Math.floor(mulMonth / paymentCycle);
            }
            // 轮数 * 周期 * 30 + 开始时间 = 目标 到期时间
            targetEndDate = getTargetEndTime(round * paymentCycle, startDate);//目标结束时间
            String formattargetEndDate = DateUtil.format(targetEndDate, "yyyy-MM-dd");
            System.out.println("费用结束时间targetEndDate" + formattargetEndDate);
            //费用项的结束时间<缴费的结束时间  费用快结束了   取费用项的结束时间
            if (feeDto.getConfigEndTime().getTime() < targetEndDate.getTime()) {
                targetEndDate = feeDto.getConfigEndTime();
                String formattargetEndDate2 = DateUtil.format(targetEndDate, "yyyy-MM-dd");
                System.out.println("费用结束时间2targetEndDate" + formattargetEndDate2);
            }
            //说明欠费
            if (endDate.getTime() < targetEndDate.getTime()) {
                // 目标到期时间 - 到期时间 = 欠费月份
                oweMonth = dayCompare(endDate, targetEndDate);
            }

            if (feeDto.getEndTime().getTime() > targetEndDate.getTime()) {
                targetEndDate = feeDto.getEndTime();
            }
        }
        String formattargetEndDate3 = DateUtil.format(targetEndDate, "yyyy-MM-dd");
        System.out.println("费用结束时间2targetEndDate" + formattargetEndDate3);
        targetEndDateAndOweMonth.put("oweMonth", oweMonth);
        targetEndDateAndOweMonth.put("targetEndDate", targetEndDate);
        return targetEndDateAndOweMonth;
    }

    public Map getTargetEndDateAndOweMonth(FeeDto feeDto) {

//        if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(feeDto.getPayerObjType())) {
//            OwnerCarDto ownerCarDto = new OwnerCarDto();
//            ownerCarDto.setCommunityId(feeDto.getCommunityId());
//            ownerCarDto.setCarId(feeDto.getPayerObjId());
//            List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
//            return getTargetEndDateAndOweMonth(feeDto, ownerCarDtos == null || ownerCarDtos.size() < 1 ? null : ownerCarDtos.get(0));
//        }
        return getTargetEndDateAndOweMonth(feeDto, null);
    }


    public Date getTargetEndTime(double month, Date startDate) {
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(startDate);

        Double intMonth = Math.floor(month);
        endDate.add(Calendar.MONTH, intMonth.intValue());
        double doubleMonth = month - intMonth;
        if (doubleMonth <= 0) {
            return endDate.getTime();
        }
        int futureDay = endDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        Double hour = doubleMonth * futureDay * 24;
        endDate.add(Calendar.HOUR_OF_DAY, hour.intValue());
        return endDate.getTime();
    }

    /**
     * 计算 两个时间点月份
     *
     * @param fromDate 开始时间
     * @param toDate   结束时间
     * @return
     */

    public double dayCompare(Date fromDate, Date toDate) {
        double resMonth = 0.0;
        Calendar from = Calendar.getInstance();
        from.setTime(fromDate);
        Calendar to = Calendar.getInstance();
        to.setTime(toDate);
        //比较月份差 可能有整数 也会负数
        int result = to.get(Calendar.MONTH) - from.get(Calendar.MONTH);
        //比较年差
        int month = (to.get(Calendar.YEAR) - from.get(Calendar.YEAR)) * 12;

        //真实 相差月份
        result = result + month;

        //开始时间  2021-06-01  2021-08-05   result = 2    2021-08-01
        Calendar newFrom = Calendar.getInstance();
        newFrom.setTime(fromDate);
        newFrom.add(Calendar.MONTH, result);
        //如果加月份后 大于了当前时间 默认加 月份 -1 情况 12-19  21-01-10
        //这个是神的逻辑一定好好理解
        if (newFrom.getTime().getTime() > toDate.getTime()) {
            newFrom.setTime(fromDate);
            result = result - 1;
            newFrom.add(Calendar.MONTH, result);
        }

        // t1 2021-08-01   t2 2021-08-05
        long t1 = newFrom.getTime().getTime();
        long t2 = to.getTime().getTime();
        //相差毫秒
        double days = (t2 - t1) * 1.00 / (24 * 60 * 60 * 1000);
        BigDecimal tmpDays = new BigDecimal(days); //相差天数
        BigDecimal monthDay = null;
        Calendar newFromMaxDay = Calendar.getInstance();
        newFromMaxDay.set(newFrom.get(Calendar.YEAR), newFrom.get(Calendar.MONTH), 1, 0, 0, 0);
        newFromMaxDay.add(Calendar.MONTH, 1); //下个月1号
        //在当前月中 这块有问题
        if (toDate.getTime() < newFromMaxDay.getTime().getTime()) {
            monthDay = new BigDecimal(newFrom.getActualMaximum(Calendar.DAY_OF_MONTH));
            return tmpDays.divide(monthDay, 2, BigDecimal.ROUND_HALF_UP).add(new BigDecimal(result)).doubleValue();
        }
        // 上月天数
        days = (newFromMaxDay.getTimeInMillis() - t1) * 1.00 / (24 * 60 * 60 * 1000);
        tmpDays = new BigDecimal(days);
        monthDay = new BigDecimal(newFrom.getActualMaximum(Calendar.DAY_OF_MONTH));
        BigDecimal preRresMonth = tmpDays.divide(monthDay, 2, BigDecimal.ROUND_HALF_UP);

        //下月天数
        days = (t2 - newFromMaxDay.getTimeInMillis()) * 1.00 / (24 * 60 * 60 * 1000);
        tmpDays = new BigDecimal(days);
        monthDay = new BigDecimal(newFromMaxDay.getActualMaximum(Calendar.DAY_OF_MONTH));
        resMonth = tmpDays.divide(monthDay, 2, BigDecimal.ROUND_HALF_UP).add(new BigDecimal(result)).add(preRresMonth).doubleValue();
        return resMonth;
    }

    public Map getFeePrice(FeeDto feeDto, RoomDto roomDto) {
        BigDecimal feePrice = new BigDecimal("0.0");
        BigDecimal feeTotalPrice = new BigDecimal(0.0);
        Map<String, Object> feeAmount = new HashMap<>();
        /*if (Environment.isOwnerPhone(java110Properties)) {
            return getOwnerPhoneFee(feeAmount);
        }*/
        if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(feeDto.getPayerObjType())) { //房屋相关
            String computingFormula = feeDto.getComputingFormula();
            /*if (roomDto == null) {
                roomDto = new RoomDto();
                roomDto.setRoomId(feeDto.getPayerObjId());
                roomDto.setCommunityId(feeDto.getCommunityId());
                List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
                if (roomDtos == null || roomDtos.size() != 1) {
                    throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查到房屋信息，查询多条数据 roomId=" + feeDto.getPayerObjId());
                }
                roomDto = roomDtos.get(0);
            }*/
            if ("1001".equals(computingFormula)) { //面积*单价+附加费
                //feePrice = Double.parseDouble(feeDto.getSquarePrice()) * Double.parseDouble(roomDtos.get(0).getBuiltUpArea()) + Double.parseDouble(feeDto.getAdditionalAmount());
                BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getSquarePrice()));
                BigDecimal builtUpArea = new BigDecimal(Double.parseDouble(roomDto.getBuiltUpArea()));
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                feePrice = squarePrice.multiply(builtUpArea).add(additionalAmount).setScale(3, BigDecimal.ROUND_HALF_EVEN);
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle = new BigDecimal(feeDto.getCycle());
                    feeTotalPrice = (squarePrice.multiply(builtUpArea).add(additionalAmount)).multiply(cycle).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("2002".equals(computingFormula)) { // 固定费用
                //feePrice = Double.parseDouble(feeDto.getAdditionalAmount());
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                feePrice = additionalAmount.setScale(3, BigDecimal.ROUND_HALF_EVEN);
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    feeTotalPrice = additionalAmount.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("3003".equals(computingFormula)) { // 固定费用
                BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getSquarePrice()));
                BigDecimal builtUpArea = new BigDecimal(Double.parseDouble(roomDto.getRoomArea()));
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                feePrice = squarePrice.multiply(builtUpArea).add(additionalAmount).setScale(3, BigDecimal.ROUND_HALF_EVEN);
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    feeTotalPrice = (squarePrice.multiply(builtUpArea).add(additionalAmount)).multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("1101".equals(computingFormula)) { // 租金
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(roomDto.getRoomRent()));
                feePrice = additionalAmount.setScale(3, BigDecimal.ROUND_HALF_EVEN);
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    feeTotalPrice = additionalAmount.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("4004".equals(computingFormula)) {  //动态费用
                feePrice = new BigDecimal(Double.parseDouble(feeDto.getAmount()));
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    BigDecimal amount = new BigDecimal(feeDto.getAmount());
                    feeTotalPrice = amount.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("5005".equals(computingFormula)) {  //(本期度数-上期度数)*单价+附加费
                if (StringUtil.isEmpty(feeDto.getCurDegrees())) {
                    //throw new IllegalArgumentException("抄表数据异常");
                } else {
                    BigDecimal curDegree = new BigDecimal(Double.parseDouble(feeDto.getCurDegrees()));
                    BigDecimal preDegree = new BigDecimal(Double.parseDouble(feeDto.getPreDegrees()));
                    BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getSquarePrice()));
                    BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                    BigDecimal sub = curDegree.subtract(preDegree);
                    feePrice = sub.multiply(squarePrice)
                            .add(additionalAmount)
                            .setScale(2, BigDecimal.ROUND_HALF_EVEN);

                    if (!StringUtil.isEmpty(feeDto.getCycle())) {
                        BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                        feeTotalPrice = (sub.multiply(squarePrice).add(additionalAmount)).multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                    }
                }
            } else if ("6006".equals(computingFormula)) {
                feePrice = new BigDecimal(Double.parseDouble(feeDto.getAmount()));
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    BigDecimal amount = new BigDecimal(feeDto.getAmount());
                    feeTotalPrice = amount.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("7007".equals(computingFormula)) { //自定义公式
                feePrice = computeRoomCustomizeFormula(feeDto, roomDto);
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    feeTotalPrice = feePrice.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("8008".equals(computingFormula)) {  //手动动态费用
                feePrice = new BigDecimal(Double.parseDouble(feeDto.getAmount()));
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    BigDecimal amount = new BigDecimal(feeDto.getAmount());
                    feeTotalPrice = amount.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("9009".equals(computingFormula)) {  //(本期度数-上期度数)*动态单价+附加费
                if (StringUtil.isEmpty(feeDto.getCurDegrees())) {
                    //throw new IllegalArgumentException("抄表数据异常");
                } else {
                    BigDecimal curDegree = new BigDecimal(Double.parseDouble(feeDto.getCurDegrees()));
                    BigDecimal preDegree = new BigDecimal(Double.parseDouble(feeDto.getPreDegrees()));
                    BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getMwPrice()));
                    BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                    BigDecimal sub = curDegree.subtract(preDegree);
                    feePrice = sub.multiply(squarePrice)
                            .add(additionalAmount)
                            .setScale(2, BigDecimal.ROUND_HALF_EVEN);
                    if (!StringUtil.isEmpty(feeDto.getCycle())) {
                        BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                        feeTotalPrice = (sub.multiply(squarePrice).add(additionalAmount)).multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                    }
                }
            } else {
                throw new IllegalArgumentException("暂不支持该类公式");
            }
        } /*else if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(feeDto.getPayerObjType())) {//车位相关
            String computingFormula = feeDto.getComputingFormula();

            OwnerCarDto ownerCarDto = new OwnerCarDto();
            ownerCarDto.setCarTypeCd("1001"); //业主车辆
            ownerCarDto.setCommunityId(feeDto.getCommunityId());
            ownerCarDto.setCarId(feeDto.getPayerObjId());
            List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
            Assert.listOnlyOne(ownerCarDtos, "未找到车辆信息");
            if ("1001".equals(computingFormula)) { //面积*单价+附加费
                ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
                parkingSpaceDto.setCommunityId(feeDto.getCommunityId());
                parkingSpaceDto.setPsId(ownerCarDtos.get(0).getPsId());
                List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
                if (parkingSpaceDtos == null || parkingSpaceDtos.size() < 1) { //数据有问题
                    throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查到停车位信息，查询多条数据");
                }
                BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getSquarePrice()));
                BigDecimal builtUpArea = new BigDecimal(Double.parseDouble(parkingSpaceDtos.get(0).getArea()));
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                feePrice = squarePrice.multiply(builtUpArea).add(additionalAmount).setScale(2, BigDecimal.ROUND_HALF_EVEN);
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    feeTotalPrice = (squarePrice.multiply(builtUpArea).add(additionalAmount)).multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("2002".equals(computingFormula)) { // 固定费用
                //feePrice = Double.parseDouble(feeDto.getAdditionalAmount());
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                feePrice = additionalAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    feeTotalPrice = additionalAmount.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("3003".equals(computingFormula)) { // 固定费用
                BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getSquarePrice()));
                BigDecimal builtUpArea = new BigDecimal(Double.parseDouble(roomDto.getRoomArea()));
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                feePrice = squarePrice.multiply(builtUpArea).add(additionalAmount).setScale(3, BigDecimal.ROUND_HALF_EVEN);
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    feeTotalPrice = (squarePrice.multiply(builtUpArea).add(additionalAmount)).multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("1101".equals(computingFormula)) { // 租金
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(roomDto.getRoomRent()));
                feePrice = additionalAmount.setScale(3, BigDecimal.ROUND_HALF_EVEN);
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    feeTotalPrice = additionalAmount.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("4004".equals(computingFormula)) {
                feePrice = new BigDecimal(Double.parseDouble(feeDto.getAmount()));
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    BigDecimal amount = new BigDecimal(feeDto.getAmount());
                    feeTotalPrice = amount.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("5005".equals(computingFormula)) {
                if (StringUtil.isEmpty(feeDto.getCurDegrees())) {
                    throw new IllegalArgumentException("抄表数据异常");
                } else {
                    BigDecimal curDegree = new BigDecimal(Double.parseDouble(feeDto.getCurDegrees()));
                    BigDecimal preDegree = new BigDecimal(Double.parseDouble(feeDto.getPreDegrees()));
                    BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getSquarePrice()));
                    BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                    BigDecimal sub = curDegree.subtract(preDegree);
                    feePrice = sub.multiply(squarePrice)
                            .add(additionalAmount)
                            .setScale(2, BigDecimal.ROUND_HALF_EVEN);
                    if (!StringUtil.isEmpty(feeDto.getCycle())) {
                        BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                        feeTotalPrice = (sub.multiply(squarePrice).add(additionalAmount)).multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                    }
                }
            } else if ("6006".equals(computingFormula)) {
                feePrice = new BigDecimal(Double.parseDouble(feeDto.getAmount()));
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    BigDecimal amount = new BigDecimal(feeDto.getAmount());
                    feeTotalPrice = amount.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("7007".equals(computingFormula)) { //自定义公式
                feePrice = computeCarCustomizeFormula(feeDto, ownerCarDtos.get(0));
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    feeTotalPrice = feePrice.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("9009".equals(computingFormula)) {  //(本期度数-上期度数)*动态单价+附加费
                if (StringUtil.isEmpty(feeDto.getCurDegrees())) {
                    //throw new IllegalArgumentException("抄表数据异常");
                } else {
                    BigDecimal curDegree = new BigDecimal(Double.parseDouble(feeDto.getCurDegrees()));
                    BigDecimal preDegree = new BigDecimal(Double.parseDouble(feeDto.getPreDegrees()));
                    BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getMwPrice()));
                    BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                    BigDecimal sub = curDegree.subtract(preDegree);
                    feePrice = sub.multiply(squarePrice)
                            .add(additionalAmount)
                            .setScale(2, BigDecimal.ROUND_HALF_EVEN);
                    if (!StringUtil.isEmpty(feeDto.getCycle())) {
                        BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                        feeTotalPrice = (sub.multiply(squarePrice).add(additionalAmount)).multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                    }
                }
            } else {
                throw new IllegalArgumentException("暂不支持该类公式");
            }
        } */
        /*else if (FeeDto.PAYER_OBJ_TYPE_CONTRACT.equals(feeDto.getPayerObjType())) { //合同相关
            String computingFormula = feeDto.getComputingFormula();

            //查询合同关联房屋
            ContractRoomDto contractRoomDto = new ContractRoomDto();
            contractRoomDto.setContractId(feeDto.getPayerObjId());
            contractRoomDto.setCommunityId(feeDto.getCommunityId());
            List<ContractRoomDto> contractRoomDtos = contractRoomInnerServiceSMOImpl.queryContractRooms(contractRoomDto);

            if ("1001".equals(computingFormula)) { //面积*单价+附加费
                //feePrice = Double.parseDouble(feeDto.getSquarePrice()) * Double.parseDouble(roomDtos.get(0).getBuiltUpArea()) + Double.parseDouble(feeDto.getAdditionalAmount());
                BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getSquarePrice()));
                BigDecimal builtUpArea = new BigDecimal(0);
                for (ContractRoomDto tmpContractRoomDto : contractRoomDtos) {
                    builtUpArea = builtUpArea.add(new BigDecimal(Double.parseDouble(tmpContractRoomDto.getBuiltUpArea())));
                }
                feeDto.setBuiltUpArea(builtUpArea.doubleValue() + "");
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                feePrice = squarePrice.multiply(builtUpArea).add(additionalAmount).setScale(3, BigDecimal.ROUND_HALF_EVEN);
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    feeTotalPrice = (squarePrice.multiply(builtUpArea).add(additionalAmount)).multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("2002".equals(computingFormula)) { // 固定费用
                //feePrice = Double.parseDouble(feeDto.getAdditionalAmount());
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
//                BigDecimal roomDount = new BigDecimal(contractRoomDtos.size());
//                additionalAmount = additionalAmount.multiply(roomDount);
                feePrice = additionalAmount.setScale(3, BigDecimal.ROUND_HALF_EVEN);
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    feeTotalPrice = additionalAmount.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("3003".equals(computingFormula)) { // 固定费用
                BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getSquarePrice()));
                BigDecimal builtUpArea = new BigDecimal(Double.parseDouble(roomDto.getRoomArea()));
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                feePrice = squarePrice.multiply(builtUpArea).add(additionalAmount).setScale(3, BigDecimal.ROUND_HALF_EVEN);
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    feeTotalPrice = (squarePrice.multiply(builtUpArea).add(additionalAmount)).multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("1101".equals(computingFormula)) { // 租金
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(roomDto.getRoomRent()));
                feePrice = additionalAmount.setScale(3, BigDecimal.ROUND_HALF_EVEN);
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    feeTotalPrice = additionalAmount.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("4004".equals(computingFormula)) {  //动态费用
                feePrice = new BigDecimal(Double.parseDouble(feeDto.getAmount()));
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    BigDecimal amount = new BigDecimal(feeDto.getAmount());
                    feeTotalPrice = amount.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("5005".equals(computingFormula)) {  //(本期度数-上期度数)*单价+附加费
                if (StringUtil.isEmpty(feeDto.getCurDegrees())) {
                    //throw new IllegalArgumentException("抄表数据异常");
                } else {
                    BigDecimal curDegree = new BigDecimal(Double.parseDouble(feeDto.getCurDegrees()));
                    BigDecimal preDegree = new BigDecimal(Double.parseDouble(feeDto.getPreDegrees()));
                    BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getSquarePrice()));
                    BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                    BigDecimal sub = curDegree.subtract(preDegree);
                    feePrice = sub.multiply(squarePrice)
                            .add(additionalAmount)
                            .setScale(2, BigDecimal.ROUND_HALF_EVEN);
                    if (!StringUtil.isEmpty(feeDto.getCycle())) {
                        BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                        feeTotalPrice = (sub.multiply(squarePrice).add(additionalAmount)).multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                    }
                }
            } else if ("6006".equals(computingFormula)) {
                feePrice = new BigDecimal(Double.parseDouble(feeDto.getAmount()));
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    BigDecimal amount = new BigDecimal(feeDto.getAmount());
                    feeTotalPrice = amount.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("7007".equals(computingFormula)) { //自定义公式
                feePrice = computeContractCustomizeFormula(feeDto, contractRoomDtos);
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    feeTotalPrice = feePrice.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("8008".equals(computingFormula)) {  //手动动态费用
                feePrice = new BigDecimal(Double.parseDouble(feeDto.getAmount()));
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    BigDecimal amount = new BigDecimal(feeDto.getAmount());
                    feeTotalPrice = amount.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("9009".equals(computingFormula)) {  //(本期度数-上期度数)*动态单价+附加费
                if (StringUtil.isEmpty(feeDto.getCurDegrees())) {
                    //throw new IllegalArgumentException("抄表数据异常");
                } else {
                    BigDecimal curDegree = new BigDecimal(Double.parseDouble(feeDto.getCurDegrees()));
                    BigDecimal preDegree = new BigDecimal(Double.parseDouble(feeDto.getPreDegrees()));
                    BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getMwPrice()));
                    BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                    BigDecimal sub = curDegree.subtract(preDegree);
                    feePrice = sub.multiply(squarePrice)
                            .add(additionalAmount)
                            .setScale(2, BigDecimal.ROUND_HALF_EVEN);
                    if (!StringUtil.isEmpty(feeDto.getCycle())) {
                        BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                        feeTotalPrice = sub.multiply(squarePrice).add(additionalAmount).multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                    }
                }
            } else {
                throw new IllegalArgumentException("暂不支持该类公式");
            }
        }*/

        feePrice.setScale(3, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        feeAmount.put("feePrice", feePrice);
        feeAmount.put("feeTotalPrice", feeTotalPrice);
        return feeAmount;
    }

    /**
     * 自定义公式计算
     *
     * @param feeDto
     * @param roomDto
     * @return C 代表房屋对应小区面积
     * F 代表房屋对应楼栋面积
     * U 代表房屋对应单元面积
     * R 代表房屋面积
     * X 代表房屋收费系数（房屋管理中配置）
     * L 代表房屋层数
     */
    private BigDecimal computeRoomCustomizeFormula(FeeDto feeDto, RoomDto roomDto) {

        String value = feeDto.getComputingFormulaText();
        value = value.replace("\n", "")
                .replace("\r", "")
                .trim();

        if (value.contains("C")) { //处理小区面积
            /*CommunityDto communityDto = new CommunityDto();
            communityDto.setCommunityId(feeDto.getCommunityId());
            List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
            if (communityDtos == null || communityDtos.size() < 1) {
                value = value.replace("C", "0");
            } else {
                value = value.replace("C", communityDtos.get(0).getCommunityArea());
            }*/
        } else if (value.contains("F")) { //处理楼栋
            value = value.replace("F", roomDto.getFloorArea());
        } else if (value.contains("U")) { //处理单元
            value = value.replace("U", roomDto.getUnitArea());
        } else if (value.contains("R")) { //处理 房屋面积
            value = value.replace("R", roomDto.getBuiltUpArea());
        } else if (value.contains("X")) {// 处理 房屋系数
            value = value.replace("X", roomDto.getFeeCoefficient());
        } else if (value.contains("L")) {//处理房屋层数
            value = value.replace("L", roomDto.getLayer());
        }

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        BigDecimal valueObj = null;
        try {
            value = engine.eval(value).toString();
            valueObj = new BigDecimal(Double.parseDouble(value));
        } catch (Exception e) {
            //throw new IllegalArgumentException("公式计算异常，公式为【" + feeDto.getComputingFormulaText() + "】,计算 【" + value + "】异常");
            valueObj = new BigDecimal(0);
        }

        if (valueObj.doubleValue() < 0) {
            return new BigDecimal(0);
        }

        return valueObj;

    }
}

