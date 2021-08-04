package com.hzfc.management.yjzx.modules.reports.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 指标
 * </p>
 *
 * @author yxx
 * @since 2020-08-21
 */
@Data
public class Gyxm implements Serializable {

    private static final long serialVersionUID = 1L;

    private String ysxmcode;

    private String lpmc;

    private Long fwcount;

}
