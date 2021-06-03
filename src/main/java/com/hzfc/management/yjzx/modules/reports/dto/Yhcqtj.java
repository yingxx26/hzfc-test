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
public class Yhcqtj implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cq;

    private Integer bmdjcs;

    private Long fys;

    private Long bmdjrs;

    private Integer yhcs;

    private Integer lycs;

    private String lyb;

    private String pjzql;


}
