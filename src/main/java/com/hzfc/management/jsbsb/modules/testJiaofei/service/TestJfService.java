package com.hzfc.management.jsbsb.modules.testJiaofei.service;

import com.hzfc.management.jsbsb.modules.testJiaofei.dto.FeeDto;
import com.hzfc.management.jsbsb.modules.testJiaofei.dto.RoomDto;

public interface TestJfService {

    public void computeFeePrice(FeeDto feeDto, RoomDto roomDto);

}
