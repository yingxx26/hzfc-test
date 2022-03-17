package com.hzfc.management.jsbsb.modules.testOnlineTaxiJiJia.dto.push;

import lombok.Data;

/**
 */
@Data
public class JpushMessage {

    private int messageType;

    private String title;

    private String messageBody;
}
